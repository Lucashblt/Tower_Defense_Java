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
    private static final long SIMULATION_DURATION = 5 * 60 * 1000; // 5 minutes en millisecondes
    private static final int ENEMIES_PER_UPDATE = 10; // Spawner 10 ennemis par update pour atteindre 25k vivants
    
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
        
        long currentTime = System.currentTimeMillis();
        long elapsedTime = currentTime - simulationStartTime;
        
        // Vérifier si la simulation est terminée (5 minutes)
        if (elapsedTime >= SIMULATION_DURATION) {
            endSimulation();
            return;
        }
        
        // Spawner en continu
        handleSpawning(currentTime);
        
        // Mettre à jour les managers
        enemyManager.update();
        updateTowersAndShoot();
        projectileManager.update();
        
        // Logger les performances toutes les secondes
        if (elapsedTime % 1000 < 17) { // Approximativement chaque seconde
            logPerformance(elapsedTime / 1000);
        }
    }
    
    private void updateTowersAndShoot() {
        ArrayList<Tower> towers = towerManager.getTowers();
        // Créer une copie pour éviter ConcurrentModificationException
        ArrayList<Enemy> enemies = new ArrayList<>(enemyManager.getEnemies());
        
        for (Tower t : towers) {
            t.update();
            // Attaquer les ennemis proches
            for (Enemy e : enemies) {
                if (e.isAlive()) {
                    if (isEnemyInRange(t, e)) {
                        if (t.isCooldownOver()) {
                            projectileManager.newProjectile(t, e);
                            t.resetCooldown();
                            break; // Une seule cible à la fois
                        }
                    }
                }
            }
        }
    }
    
    private boolean isEnemyInRange(Tower t, Enemy e) {
        int range = helper.Utilz.getHypoDistance((int)t.getX(), (int)t.getY(), (int)e.getX(), (int)e.getY());
        return range <= t.getRange();
    }
    
    private void handleSpawning(long currentTime) {
        // Spawn massif : 10 ennemis par update (60 updates/sec = 600 ennemis/sec)
        for (int i = 0; i < ENEMIES_PER_UPDATE; i++) {
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
        // Dessiner le niveau
        for (int y = 0; y < level.length; y++) {
            for (int x = 0; x < level[y].length; x++) {
                int id = level[y][x];
                g.drawImage(tileManager.getSprite(id), x * 32, y * 32, null);
            }
        }
        
        // Dessiner les éléments du jeu
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
