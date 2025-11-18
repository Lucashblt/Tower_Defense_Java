package scenes;

import static helper.Constants.Tiles.GRASS_TILE;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;

import enemies.Enemy;
import helper.LevelBuild;
import main.Game;
import managers.EnemyManager;
import managers.ProjectileManager;
import managers.TileManager;
import managers.TowerManager;
import managers.WaveManager;
import objects.PathPoint;
import objects.Tower;
import ui.ActionBar;

public class Playing extends GameScene implements SceneMethods {

    private int[][] level;
    private TileManager tileManager;
    private EnemyManager enemyManager;
    private TowerManager towerManager;
    private ProjectileManager projectilManager;
    private WaveManager waveManager;
    private ActionBar actionBar;
    private PathPoint start, end;
    private Tower selectedTower;
    private int mouseX, mouseY;
    private int goldTick = 0;
    private boolean gamePaused = false;

    public Playing(Game game) {
        super(game);
        level = LevelBuild.getLevelData();
        start = LevelBuild.getStartPoint();
        end = LevelBuild.getEndPoint();
        tileManager = new TileManager();
        actionBar = new ActionBar(0, 640, 640, 100, this);
        enemyManager = new EnemyManager(this, start, end);
        towerManager = new TowerManager(this);
        projectilManager = new ProjectileManager(this);
        waveManager = new WaveManager(this);
    }

    public void update() {
        if(!gamePaused){
            waveManager.update();

            goldTick++;
            if(goldTick % (60*3) == 0) {
                actionBar.addGold(1);
            }

            if(isAllEnemiesDead()) {
                if(isThereMoreWaves()){
                    waveManager.startWaveTimer();
                    if(isWaveTimerOver()){
                        waveManager.increaseWaveIndex();
                        enemyManager.getEnemies().clear();
                        waveManager.resetEnemyIndex();
                    }
                }
            }

            if(isTimeForNewEnemy()) {
                spawnEnemy();
            }

            enemyManager.update();
            towerManager.update();
            projectilManager.update();
        }       
    }

    private boolean isWaveTimerOver(){
        return waveManager.isWaveTimerOver();
    }

    private boolean isThereMoreWaves(){
        return waveManager.isThereMoreWaves();
    }

    private boolean isAllEnemiesDead() {
        if(waveManager.isThereMoreEnemiesInWave()) {
            return false;
        }
        for(Enemy e : enemyManager.getEnemies()) {
            if(e.isAlive()) {
                return false;
            }
        }
        return true;
    }

    private void spawnEnemy() {
        enemyManager.spawnEnemy(waveManager.getNextEnemy());
    }

    private boolean isTimeForNewEnemy() {
        if(waveManager.isTimeForNewEnemy()){
            if(waveManager.isThereMoreEnemiesInWave()){
                return true;
            }
        }
        return false;
    }

    public boolean isGamePaused() {
        return gamePaused;
    }

    public void setGamePaused(boolean gamePaused) {
        this.gamePaused = gamePaused;
    }

    @Override
    public void render(Graphics g) {
        drawLevel(g);
        actionBar.draw(g);
        enemyManager.draw(g);
        towerManager.draw(g);
        projectilManager.draw(g);
        drawSelectedTower(g);
        drawHilightedTile(g);
    }

    private void drawHilightedTile(Graphics g) {
        if (selectedTower != null) {
            g.setColor(Color.WHITE);
            g.drawRect(mouseX, mouseY, 32, 32);
        }
    }

    private void drawSelectedTower(Graphics g) {
        if (selectedTower != null) {
            g.drawImage(towerManager.getTowerImgs()[selectedTower.getTowerType()], mouseX, mouseY, null);
        }
    }

    private void drawLevel(Graphics g) {
        for ( int y = 0; y < level.length; y++) {
            for ( int x = 0; x < level[y].length; x++) {
                int id = level[y][x];
                g.drawImage(tileManager.getSprite(id), x * 32, y * 32, null);
            }
        }
    }

    public TowerManager getTowerManager() {
        return towerManager;
    }

    public int getTileType(int x, int y) {
        int xCord = x / 32;
        int yCord = y / 32;

        if (xCord < 0 || xCord > 19)
            return 0;
        if (yCord < 0 || yCord > 19)
            return 0;

        int id = level[y / 32][x / 32];
        return game.getTileManager().getTile(id).getTileType();
    }

    
    public void setSelectedTower(Tower selectedTower) {
        this.selectedTower = selectedTower;
    }

    
    public void shootEnemy(Tower t, Enemy e) {
        projectilManager.newProjectile(t, e);
    }
        
    private Tower getTowerAt(int x, int y) {
        return towerManager.getTowerAt(x, y);
    }

    private void removeGold(int towerType) {
        actionBar.payForTower(towerType);
    }

    @Override
    public void mouseClicked(int x, int y) {
        if (y >= 640)
			actionBar.mouseClicked(x, y);
        else {
            if (selectedTower != null) {
                if(getTileType(mouseX, mouseY) == GRASS_TILE) {
                    if(getTowerAt(mouseX, mouseY) == null) {
                        towerManager.addTower(selectedTower, mouseX, mouseY);
                        removeGold(selectedTower.getTowerType());
                        this.selectedTower = null;
                    }
                    return;
                }
            } else {
                Tower t = getTowerAt(mouseX, mouseY);
                if (t != null) {
                    actionBar.displayTowerInfo(t);
                } else {
                    actionBar.resetDisplayedTower();
                }
            }
        }
    }

    @Override
    public void mouseMoved(int x, int y) {
        if (y >= 640)
            actionBar.mouseMoved(x, y);
        else {
			mouseX = (x / 32) * 32;
			mouseY = (y / 32) * 32;
		}
    }  
    @Override
    public void mousePressed(int x, int y) {
        if (y >= 640)
            actionBar.mousePressed(x, y);
    }  
    @Override
    public void mouseReleased(int x, int y) {
        actionBar.mouseReleased(x, y);
    }

    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            this.selectedTower = null;
        }
    }

    public void rewardPlayer(int reward) {
        actionBar.addGold(reward);
    }

    public EnemyManager getEnemyManager() {
        return enemyManager;
    }

    public WaveManager getWaveManager() {
        return waveManager;
    }

    public void removeOneLive() {
        actionBar.removeOneLive();
    }

    public void resetAll() {
        enemyManager.reset();
        towerManager.reset();
        projectilManager.reset();
        waveManager.reset();
        actionBar.reset();

        mouseX = 0;
        mouseY = 0;
        goldTick = 0;
        gamePaused = false;
        selectedTower = null;
    }
}
