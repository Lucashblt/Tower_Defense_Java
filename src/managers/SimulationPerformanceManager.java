package managers;

import java.awt.Graphics;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

import enemies.Enemy;
import helper.LevelBuild;
import objects.PathPoint;
import objects.Tower;
import scenes.SimulationPerformance;
import static helper.Constants.Tiles.*;
import static helper.Constants.Towers.*;

public class SimulationPerformanceManager {
    
    private SimulationPerformance simulationPerformance;
    private int[][] level;
    private TileManager tileManager;
    private EnemyManager enemyManager;
    private TowerManager towerManager;
    private ProjectileManager projectileManager;
    private PathPoint start, end;
    private PrintWriter performanceWriter;
    private long simulationStartTime;
    private int totalEnemiesSpawned = 0;
    private Random random;
    private boolean simulationRunning = false;
    private boolean isPaused = false;
    private static final long SIMULATION_DURATION = 15 * 60 * 1000;
    private static final long TARGET_TIME_FOR_100K = 10 * 60 * 1000; // 10 minutes pour atteindre 100k vivants
    private static final int TARGET_ALIVE_ENEMIES = 100000; // Objectif: 100k ennemis vivants à 10 min
    private int updateCounter = 0; // Compteur pour optimisations adaptatives 
    
    public SimulationPerformanceManager(SimulationPerformance simulationPerformance, TileManager tileManager) {
        this.simulationPerformance = simulationPerformance;
        this.tileManager = tileManager;
        this.level = LevelBuild.getLevelData();
        this.start = LevelBuild.getStartPoint();
        this.end = LevelBuild.getEndPoint();
        this.random = new Random();
        
        this.enemyManager = new EnemyManager(null, start, end);
        this.enemyManager.setTileManagerAndLevel(tileManager, level);
        this.enemyManager.setSimulationMode(true);
        this.towerManager = new TowerManager(null);
        this.projectileManager = new ProjectileManager(null);
        this.projectileManager.setEnemyManager(enemyManager);
    }
    
    public void startSimulation() {
        if (simulationRunning) {
            return;
        }
        
        simulationRunning = true;
        isPaused = false;
        totalEnemiesSpawned = 0;
        
        // Initialiser le fichier de performance
        initPerformanceFile();
        
        // Placer toutes les tours automatiquement
        placeAllTowers();
        
        // Démarrer le timer
        simulationStartTime = System.currentTimeMillis();
        
        System.out.println("Simulation de performance démarrée!");
    }
    
    private void initPerformanceFile() {
        try {
            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String filename = "performance_simulation_" + timestamp + ".txt";
            performanceWriter = new PrintWriter(new FileWriter(filename), true);
            
            performanceWriter.println("=== SIMULATION DE PERFORMANCE - TOWER DEFENSE ===");
            performanceWriter.println("Date: " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
            performanceWriter.println("Durée: 5 minutes");
            performanceWriter.println("Format: Temps(min:sec:ms), Total Ennemis, FPS, UPS, Ennemis actifs, Tours actives, Projectiles actifs, Mémoire utilisée (MB)");
            performanceWriter.println("================================================");
            performanceWriter.println();
            
        } catch (IOException e) {
            System.err.println("Erreur lors de la création du fichier de performance: " + e.getMessage());
        }
    }
    
    private void placeAllTowers() {
        for (int y = 0; y < level.length; y++) {
            for (int x = 0; x < level[y].length; x++) {
                int id = level[y][x];
                int tileType = tileManager.getTile(id).getTileType();
                
                if (tileType == GRASS_TILE) {
                    int towerType = determineTowerType(x, y);
                    if (towerType != -1) {
                        int towerId = towerManager.getTowerCount();
                        Tower tower = new Tower(x * 32, y * 32, towerId, towerType);
                        // Upgrade au tier 3
                        tower.upgradeTower();
                        tower.upgradeTower();
                        towerManager.addTowerDirect(tower);
                    }
                }
            }
        }
        System.out.println("Toutes les tours ont été placées! Total: " + towerManager.getTowerCount());
    }
    
    private int determineTowerType(int x, int y) {
        // Vérifier si à côté de la route
        if (isNextToRoad(x, y)) {
            return CANON_TOWER;
        }
        
        // Vérifier si à côté d'un canon
        if (isNextToCanon(x, y)) {
            return WIZARD_TOWER;
        }
        
        // Par défaut, tour d'archer
        return ARCHER_TOWER;
    }
    
    private boolean isNextToRoad(int x, int y) {
        // Vérifier les 4 cases adjacentes
        int[][] directions = {{0, -1}, {0, 1}, {-1, 0}, {1, 0}};
        
        for (int[] dir : directions) {
            int newX = x + dir[0];
            int newY = y + dir[1];
            
            if (newX >= 0 && newX < level[0].length && newY >= 0 && newY < level.length) {
                int id = level[newY][newX];
                int tileType = tileManager.getTile(id).getTileType();
                if (tileType == ROAD_TILE || tileType == START_TILE || tileType == END_TILE) {
                    return true;
                }
            }
        }
        return false;
    }
    
    private boolean isNextToCanon(int x, int y) {
        // Vérifier les 4 cases adjacentes pour des canons
        int[][] directions = {{0, -1}, {0, 1}, {-1, 0}, {1, 0}};
        
        for (int[] dir : directions) {
            int newX = (x + dir[0]) * 32;
            int newY = (y + dir[1]) * 32;
            
            Tower tower = towerManager.getTowerAt(newX, newY);
            if (tower != null && tower.getTowerType() == CANON_TOWER) {
                return true;
            }
        }
        return false;
    }
    
    public void update() {
        if (!simulationRunning || isPaused) {
            return;
        }
        
        updateCounter++;
        long currentTime = System.currentTimeMillis();
        long elapsedTime = currentTime - simulationStartTime;
        
        // Vérifier si la simulation est terminée (15 minutes)
        if (elapsedTime >= SIMULATION_DURATION) {
            endSimulation();
            return;
        }
        
        // Logger les performances toutes les secondes
        if (updateCounter % 60 == 0) { // 60 updates = 1 seconde
            logPerformance(elapsedTime / 1000);
        }
        
        // Spawner en continu
        handleSpawning(currentTime);
        
        // Mettre à jour les managers à chaque frame
        enemyManager.update();
        updateTowersAndShoot();
        projectileManager.update();
    }
    
    private void updateTowersAndShoot() {
        // Créer une copie pour éviter ConcurrentModificationException
        ArrayList<Tower> towers = new ArrayList<>(towerManager.getTowers());
        
        // OPTIMISATION : Limiter le nombre de tours actives si trop d'ennemis
        int maxTowersToUpdate = towers.size();
        // int aliveEnemies = enemyManager.getAmountOfAliveEnemies();
        // if (aliveEnemies > 100000) {
        //     maxTowersToUpdate = Math.min(30, towers.size()); // Seulement 30 tours
        // } else if (aliveEnemies > 50000) {
        //     maxTowersToUpdate = Math.min(60, towers.size()); // Seulement 60 tours
        // }
        
        for (int towerIndex = 0; towerIndex < maxTowersToUpdate; towerIndex++) {
            Tower t = towers.get(towerIndex);
            t.update();
            
            if (!t.isCooldownOver()) {
                continue; // Skip si cooldown pas fini
            }
            
            // Utiliser le spatial grid pour récupérer SEULEMENT les ennemis proches
            java.util.List<Enemy> nearbyEnemies = enemyManager.getEnemiesNear(t.getX(), t.getY(), (int)t.getRange());
            
            // Attaquer le premier ennemi à portée
            for (Enemy e : nearbyEnemies) {
                if (e.isAlive()) {
                    if (isEnemyInRange(t, e)) {
                        projectileManager.newProjectile(t, e);
                        t.resetCooldown();
                        break; // Une seule cible à la fois
                    }
                }
            }
        }
    }
    
    private boolean isEnemyInRange(Tower t, Enemy e) {
        // Optimisation : Comparer les carrés des distances pour éviter sqrt()
        // (x2-x1)² + (y2-y1)² <= range²
        int dx = (int)t.getX() - (int)e.getX();
        int dy = (int)t.getY() - (int)e.getY();
        int distanceSquared = dx * dx + dy * dy;
        return distanceSquared <= t.getRangeSquared();
    }
    
    private void handleSpawning(long currentTime) {
        // Calculer le temps écoulé et le ratio de progression
        long elapsedTime = currentTime - simulationStartTime;
        double progressRatio = Math.min(1.0, (double)elapsedTime / TARGET_TIME_FOR_100K);
        
        // Calculer le nombre cible d'ennemis vivants basé sur la progression
        int targetAliveEnemies = (int)(TARGET_ALIVE_ENEMIES * progressRatio);
        int currentAliveEnemies = enemyManager.getAmountOfAliveEnemies();
        
        // Calculer combien d'ennemis spawner pour atteindre la cible
        // On ajoute un buffer pour compenser les morts
        int deficit = targetAliveEnemies - currentAliveEnemies;
        
        // Spawn adaptatif : plus on est loin de la cible, plus on spawn
        int enemiesToSpawn = 0;
        if (deficit > 10000) {
            enemiesToSpawn = 50; // Très loin de la cible
        } else if (deficit > 5000) {
            enemiesToSpawn = 30; // Loin de la cible
        } else if (deficit > 1000) {
            enemiesToSpawn = 15; // Un peu loin
        } else if (deficit > 0) {
            enemiesToSpawn = 8; // Proche de la cible
        } else if (deficit > -5000) {
            enemiesToSpawn = 3; // Au-dessus mais on maintient
        }
        // Si très au-dessus (deficit < -5000), on ne spawn pas
        
        // Spawner les ennemis
        for (int i = 0; i < enemiesToSpawn; i++) {
            spawnEnemy();
        }
    }
    
    private void spawnEnemy() {
        int enemyType = random.nextInt(4); // 0-3 pour ORC, BAT, KNIGHT, WOLF
        enemyManager.addEnemy(enemyType);
        totalEnemiesSpawned++;
    }
    
    private void logPerformance(long seconds) {
        if (performanceWriter == null) {
            return;
        }
        
        // Calculer le temps écoulé depuis le début
        long elapsedMs = System.currentTimeMillis() - simulationStartTime;
        long minutes = elapsedMs / 60000;
        long secs = (elapsedMs % 60000) / 1000;
        long milliseconds = elapsedMs % 1000;
        
        Runtime runtime = Runtime.getRuntime();
        long usedMemory = (runtime.totalMemory() - runtime.freeMemory()) / (1024 * 1024);
        int activeEnemies = enemyManager.getAmountOfAliveEnemies();
        int activeTowers = towerManager.getTowerCount();
        int activeProjectiles = projectileManager.getProjectileCount();
        
        // Obtenir FPS et UPS réels depuis Game
        int fps = simulationPerformance.getGame().getCurrentFPS();
        int ups = simulationPerformance.getGame().getCurrentUPS();
        
        performanceWriter.printf("%02d:%02d:%03d, %d, %d, %d, %d, %d, %d, %d%n",
                minutes, secs, milliseconds, totalEnemiesSpawned, fps, ups, activeEnemies, activeTowers, activeProjectiles, usedMemory);
    }
    
    private void endSimulation() {
        simulationRunning = false;
        
        if (performanceWriter != null) {
            performanceWriter.println();
            performanceWriter.println("================================================");
            performanceWriter.println("Simulation terminée!");
            performanceWriter.println("Total d'ennemis générés: " + totalEnemiesSpawned);
            performanceWriter.println("================================================");
            performanceWriter.close();
        }
        
        System.out.println("Simulation de performance terminée!");
        System.out.println("Total d'ennemis générés: " + totalEnemiesSpawned);
    }
    
    public void draw(Graphics g) {
        // Dessiner le niveau (optimisé : pas de changement nécessaire, c'est statique)
        for (int y = 0; y < level.length; y++) {
            for (int x = 0; x < level[y].length; x++) {
                int id = level[y][x];
                g.drawImage(tileManager.getSprite(id), x * 32, y * 32, null);
            }
        }
        
        // Dessiner les éléments du jeu (déjà optimisés dans les managers)
        enemyManager.draw(g);
        towerManager.draw(g);
        projectileManager.draw(g);
    }
    
    public void togglePause() {
        isPaused = !isPaused;
    }
    
    public boolean isPaused() {
        return isPaused;
    }
    
    public boolean isSimulationRunning() {
        return simulationRunning;
    }
    
    public long getElapsedTime() {
        if (!simulationRunning) {
            return 0;
        }
        return System.currentTimeMillis() - simulationStartTime;
    }
    
    public int getTotalEnemiesSpawned() {
        return totalEnemiesSpawned;
    }
    
    public int getAliveEnemiesCount() {
        return enemyManager.getAmountOfAliveEnemies();
    }
    
    public EnemyManager getEnemyManager() {
        return enemyManager;
    }
    
    public TowerManager getTowerManager() {
        return towerManager;
    }
    
    public ProjectileManager getProjectileManager() {
        return projectileManager;
    }
    
    public void reset() {
        simulationRunning = false;
        isPaused = false;
        totalEnemiesSpawned = 0;
        
        if (performanceWriter != null) {
            performanceWriter.close();
            performanceWriter = null;
        }
        
        enemyManager.reset();
        towerManager.reset();
        projectileManager.reset();
    }
}
