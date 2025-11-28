package managers;

import scenes.Playing;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import enemies.Bat;
import enemies.Enemy;
import enemies.Knight;
import enemies.Orc;
import enemies.Wolf;
import helper.LoadSave;
import helper.ObjectPool;
import helper.SpatialGrid;
import objects.PathPoint;

import static helper.Constants.Directions.*;
import static helper.Constants.Tiles.*;
import static helper.Constants.Enemies.*;

public class EnemyManager {
    
    private Playing playing;
    private TileManager tileManager;
    private int[][] level;
    private BufferedImage[] enemyImgs;
    
    // Object Pooling pour réduire les allocations
    private ObjectPool<Orc> orcPool;
    private ObjectPool<Wolf> wolfPool;
    private ObjectPool<Bat> batPool;
    private ObjectPool<Knight> knightPool;
    
    // Liste unifiée des ennemis actifs
    private ArrayList<Enemy> activeEnemies = new ArrayList<>();
    
    // Spatial partitioning pour optimiser les recherches
    private SpatialGrid spatialGrid;
    
    private PathPoint start, end;
    private final int HPBARWIDTH = 20;
    private BufferedImage slowEffect;
    private int currentWaveNumber = 0;
    private int totalSpawned = 0;
    private boolean isSimulationMode = false;
    
    // Cache pour éviter les allocations répétées
    private List<Enemy> nearbyEnemiesCache = new ArrayList<>();

    public EnemyManager(Playing playing, PathPoint start, PathPoint end) {
        this.playing = playing;
        this.start = start;
        this.end = end;
        
        orcPool = new ObjectPool<>(() -> new Orc(0, 0, 0, this, 0), 15000, 60000);
        wolfPool = new ObjectPool<>(() -> new Wolf(0, 0, 0, this, 0), 15000, 60000);
        batPool = new ObjectPool<>(() -> new Bat(0, 0, 0, this, 0), 15000, 60000);
        knightPool = new ObjectPool<>(() -> new Knight(0, 0, 0, this, 0), 15000, 60000);
        
        spatialGrid = new SpatialGrid(0, new Rectangle(0, 0, 640, 640));
        
        enemyImgs = new BufferedImage[4];
        loadEnemyImgs();
        loadSlowEffect();
    }
    
    public void setSimulationMode(boolean isSimulationMode) {
        this.isSimulationMode = isSimulationMode;
    }
    
    public boolean isSimulationMode() {
        return isSimulationMode;
    }
    
    public void setTileManagerAndLevel(TileManager tileManager, int[][] level) {
        this.tileManager = tileManager;
        this.level = level;
    }

    public void update() {
        // Reconstruire le spatial grid à chaque frame
        spatialGrid.clear();
        
        // OPTIMISATION : Traiter par lots pour éviter les latences
        int batchSize = Math.min(1000, activeEnemies.size()); // Traiter max 1000 ennemis par lot
        
        // Mise à jour et insertion dans le grid
        for (int i = activeEnemies.size() - 1; i >= 0; i--) {
            Enemy e = activeEnemies.get(i);
            if (e.isAlive()) {
                updateEnemyMove(e);
                // OPTIMISATION : N'insérer dans le spatial grid que tous les N ennemis
                if (i % 3 == 0 || activeEnemies.size() < 10000) {
                    spatialGrid.insert(e);
                }
            } else {
                // Retourner l'ennemi au pool approprié
                returnEnemyToPool(e);
                activeEnemies.remove(i);
            }
        }
    }
    
    /**
     * Retourne un ennemi au pool approprié selon son type
     */
    private void returnEnemyToPool(Enemy e) {
        switch (e.getEnemyType()) {
            case ORC:
                orcPool.free((Orc) e);
                break;
            case WOLF:
                wolfPool.free((Wolf) e);
                break;
            case BAT:
                batPool.free((Bat) e);
                break;
            case KNIGHT:
                knightPool.free((Knight) e);
                break;
        }
    }

    public void draw(Graphics g) {
        // Ne dessiner qu'un sous-ensemble d'ennemis si trop nombreux
        int drawInterval = 1;
        if (activeEnemies.size() > 50000) {
            drawInterval = 4; // Dessiner 1 ennemi sur 4
        } else if (activeEnemies.size() > 20000) {
            drawInterval = 2; // Dessiner 1 ennemi sur 2
        }
        
        for (int i = 0; i < activeEnemies.size(); i += drawInterval) {
            Enemy e = activeEnemies.get(i);
            if (e.isAlive()) {
                float x = e.getX();
                float y = e.getY();
                if (x >= -32 && x <= 672 && y >= -32 && y <= 832) {
                    drawEnemy(e, g);
                    if (activeEnemies.size() < 30000) {
                        drawHealthBar(e, g);
                    }
                    drawEffects(e, g);
                }
            }
        }
    }

    private void drawEffects(Enemy e, Graphics g) {
        if(e.isSlowed()) {
            g.drawImage(slowEffect, (int)e.getX(), (int)e.getY(), null);
        }
    }

    private void drawHealthBar(Enemy e, Graphics g) {
        g.setColor(Color.RED);
        g.fillRect((int)e.getX() + 16 - (getNewBarWidth(e) / 2), (int)e.getY() - 10, getNewBarWidth(e), 3);
    }

    private int getNewBarWidth(Enemy e) {
        return (int)(HPBARWIDTH * e.getHealthBarFloat());
    }

    private void loadEnemyImgs() {
        BufferedImage atlas = LoadSave.getSpriteAtlas();
        
        for (int i = 0; i < 4; i++) {
            enemyImgs[i] = atlas.getSubimage(i * 32, 32, 32, 32);
        }
    }

    private void loadSlowEffect() {
        slowEffect =  LoadSave.getSpriteAtlas().getSubimage(9 * 32, 32 * 2, 32, 32);
    }

    private int getTileType(int x, int y) {
        if (playing != null) {
            return playing.getTileType(x, y);
        } else if (tileManager != null && level != null) {
            // Calculer le type de tuile directement
            int xCord = x / 32;
            int yCord = y / 32;
            
            if (xCord < 0 || xCord >= level[0].length)
                return 0;
            if (yCord < 0 || yCord >= level.length)
                return 0;
            
            int id = level[yCord][xCord];
            return tileManager.getTile(id).getTileType();
        }
        return 0;
    }

    private void setNewDirectionAndMove(Enemy e){
        int dir = e.getLastDir();

        int xCord = (int)(e.getX() / 32);
        int yCord = (int)(e.getY() / 32);

        fixEnemyPosition(e, dir, xCord, yCord);

        if(isAtEnd(e))
            return;

        if (dir == LEFT || dir == RIGHT) {
            int newY = (int)(e.getY() + getSpeedYAndHeight(UP, e.getEnemyType()));
            int typeUp = getTileType((int) e.getX(), newY);
            if (typeUp == ROAD_TILE || typeUp == END_TILE) {
                e.move(GetSpeed(e.getEnemyType()), UP);
            } else {
                e.move(GetSpeed(e.getEnemyType()), DOWN);
            }
        } else {
            int newX = (int)(e.getX() + getSpeedXAndWidth(RIGHT, e.getEnemyType()));
            int typeRight = getTileType(newX, (int) e.getY());
            if (typeRight == ROAD_TILE || typeRight == END_TILE) {
                e.move(GetSpeed(e.getEnemyType()), RIGHT);
            } else {
                e.move(GetSpeed(e.getEnemyType()), LEFT);
            }
        }
    }

    private void fixEnemyPosition(Enemy e, int dir, int xCord, int yCord) {
        // Snap to nearest grid line to avoid backward/forward teleport
        int snapX = Math.round(e.getX() / 32f) * 32;
        int snapY = Math.round(e.getY() / 32f) * 32;
        e.setPosition(snapX, snapY);
    }

    private boolean isAtEnd(Enemy e) {
        // Check by tile coordinates
        int ex = (int)(e.getX() / 32);
        int ey = (int)(e.getY() / 32);
        return ex == end.getxCord() && ey == end.getyCord();
    }

    private float getSpeedXAndWidth(int dir, int enemyType) {
        if(dir == LEFT)
            return -GetSpeed(enemyType);
        else if(dir == RIGHT)
            return GetSpeed(enemyType) + 32;
        return 0;
    }

    private float getSpeedYAndHeight(int dir, int enemyType) {
        if(dir == UP)
            return -GetSpeed(enemyType);
        else if(dir == DOWN)
            return GetSpeed(enemyType) + 32;
        return 0;
    }

    private void drawEnemy(Enemy e, Graphics g) {
        g.drawImage(enemyImgs[e.getEnemyType()], (int)e.getX(), (int)e.getY(), null);
    }

    
    public void spawnEnemy(int nextEnemy) {
        addEnemy(nextEnemy);
    }
    
    public void addEnemy(int enemyType) {
        int x = start.getxCord() * 32;
        int y = start.getyCord() * 32;
        int enemyIndex = isSimulationMode ? totalSpawned : currentWaveNumber;
        
        Enemy enemy = null;
        
        // Obtenir un ennemi du pool approprié
        switch (enemyType) {
            case ORC:
                enemy = orcPool.obtain();
                break;
            case WOLF:
                enemy = wolfPool.obtain();
                break;
            case BAT:
                enemy = batPool.obtain();
                break;
            case KNIGHT:
                enemy = knightPool.obtain();
                break;
        }
        
        if (enemy != null) {
            enemy.reuse(x, y, enemyIndex);
            activeEnemies.add(enemy);
            if (isSimulationMode) {
                totalSpawned++;
            }
        }
    }

    public void updateEnemyMove(Enemy e) {
        if(e.getLastDir() == -1)
            setNewDirectionAndMove(e);

        int newX = (int)(e.getX() + getSpeedXAndWidth(e.getLastDir(), e.getEnemyType())); 
        int newY = (int)(e.getY() + getSpeedYAndHeight(e.getLastDir(), e.getEnemyType()));

        int nextType = getTileType(newX, newY);
        if(nextType == ROAD_TILE || nextType == END_TILE) {
            e.move(GetSpeed(e.getEnemyType()), e.getLastDir());
        } else if(isAtEnd(e)){
            e.kill();
            if (playing != null) {
                playing.removeOneLive();
            }
        } else {
            setNewDirectionAndMove(e);
        }
    }

    public ArrayList<Enemy> getEnemies() {
        return activeEnemies;
    }
    
    /**
     * Retourne les ennemis proches d'une position donnée (optimisé avec spatial partitioning)
     */
    public List<Enemy> getEnemiesNear(float x, float y, int range) {
        nearbyEnemiesCache.clear();
        return spatialGrid.retrieve(nearbyEnemiesCache, x, y, range);
    }

    public int getAmountOfAliveEnemies() {
        int size = 0;
        for (Enemy e : activeEnemies) {
            if (e.isAlive())
                size++;
        }
        return size;
    }

    public void rewardPlayer(int reward) {
        if (playing != null) {
            playing.rewardPlayer(reward);
        }
    }

    public void setCurrentWaveNumber(int waveNumber) {
        this.currentWaveNumber = waveNumber;
    }

    public void reset() {
        // Retourner tous les ennemis aux pools
        for (Enemy e : activeEnemies) {
            returnEnemyToPool(e);
        }
        activeEnemies.clear();
        spatialGrid.clear();
        currentWaveNumber = 0;
        totalSpawned = 0;
    }

}
