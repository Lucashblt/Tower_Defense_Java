package managers;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import enemies.Enemy;
import helper.LoadSave;
import objects.Tower;
import scenes.Playing;

public class TowerManager {
    
    private Playing playing;
    private BufferedImage[] towerImgs;
    private ArrayList<Tower> towers = new ArrayList<>();
    private int towerIdCounter = 0;

    public TowerManager(Playing playing) {
        this.playing = playing;
        
        loadTowerImgs();
    }

    private void loadTowerImgs() {
        BufferedImage atlas = LoadSave.getSpriteAtlas();
        towerImgs = new BufferedImage[3];

        for (int i = 0; i < 3; i++) {
            towerImgs[i] = atlas.getSubimage( (4 + i ) * 32, 32, 32, 32);
        }
    }

    private void attackEnemyIfClose(Tower t) {
        if (playing == null) {
            return;
        }
        
        // Utiliser le spatial grid pour ne récupérer que les ennemis proches
        // Au lieu de vérifier TOUS les ennemis, on ne vérifie que ceux dans le rayon de la tour
        EnemyManager enemyManager = playing.getEnemyManager();
        java.util.List<Enemy> nearbyEnemies = enemyManager.getEnemiesNear(t.getX(), t.getY(), (int)t.getRange());
        
        // Parcourir uniquement les ennemis proches (réduit drastiquement les itérations)
        for (int i = 0; i < nearbyEnemies.size(); i++) {
            Enemy e = nearbyEnemies.get(i);
            if (e.isAlive()) {
                if (isEnemyInRange(t, e)) {
                    if (t.isCooldownOver()) {
                        playing.shootEnemy(t, e);
                        t.resetCooldown();
                        return; // Une tour ne tire qu'une fois par cooldown
                    }
                }
            }
        }
    }

    private boolean isEnemyInRange(Tower t, Enemy e) {
        int range  = helper.Utilz.getHypoDistance((int)t.getX(), (int)t.getY(), (int)e.getX(), (int)e.getY());
        return range <= t.getRange();
    }

    public void addTower(Tower selectedTower, int x, int y) {
        towers.add(new Tower(x, y, towerIdCounter++, selectedTower.getTowerType()));
    }

    public void addTowerDirect(Tower tower) {
        towers.add(tower);
    }

    public void update() {
        for (Tower t : towers) {
            t.update();
            attackEnemyIfClose(t);            
        }
    }

    public void draw(Graphics g){
        for (Tower t : towers) {
            g.drawImage(towerImgs[t.getTowerType()], (int)t.getX(), (int)t.getY(), null);
        }
    }

    public BufferedImage[] getTowerImgs() {
        return towerImgs;
    }

    public Tower getTowerAt(int x, int y) {
        for (Tower t : towers) {
            if( t.getX() == x && t.getY() == y) {
                return t;
            }
        }
        return null;
    }

    public void sellTower(Tower displayedTower) {
        for (int i = 0; i < towers.size(); i++) {
            if(towers.get(i).getId() == displayedTower.getId()) {
                towers.remove(i);
                return;
            }
        }
    }

    public void upgradeTower(Tower displayedTower) {
        // add more dmg and range
        for (Tower t : towers) {
            if(t.getId() == displayedTower.getId()) {
                t.upgradeTower();
                return;
            }
        }
    }

    public int getTowerCount() {
        return towers.size();
    }

    public ArrayList<Tower> getTowers() {
        return towers;
    }

    public void reset() {
        towers.clear();
        towerIdCounter = 0;
    }
}
