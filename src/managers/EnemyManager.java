package managers;

import scenes.Playing;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.nio.file.Path;
import java.util.ArrayList;

import enemies.Bat;
import enemies.Enemy;
import enemies.Knight;
import enemies.Orc;
import enemies.Wolf;
import helper.LoadSave;
import objects.PathPoint;
import objects.Tile;

import static helper.Constants.Directions.*;
import static helper.Constants.Tiles.*;
import static helper.Constants.Enemies.*;

public class EnemyManager {
    
    private Playing playing;
    private BufferedImage[] enemyImgs;
    private ArrayList<Enemy> enemies = new ArrayList<>();
    private PathPoint start, end;

    public EnemyManager(Playing playing, PathPoint start, PathPoint end) {
        this.playing = playing;
        this.start = start;
        this.end = end;
        enemyImgs = new BufferedImage[4];
        addEnemy(ORC);
        addEnemy(WOLF);
        addEnemy(BAT);
        addEnemy(KNIGHT);
        loadEnemyImgs();
    }

    
    private void loadEnemyImgs() {
        BufferedImage atlas = LoadSave.getSpriteAtlas();
        
        for (int i = 0; i < 4; i++) {
            enemyImgs[i] = atlas.getSubimage(i * 32, 32, 32, 32);
        }
    }

    public void addEnemy(int enemyType) {
        int x = start.getxCord() * 32;
        int y = start.getyCord() * 32;
        switch (enemyType) {
            case ORC:
                enemies.add(new Orc(x, y, 0));
                break;
            case WOLF:
                enemies.add(new Wolf(x, y, 0));
                break;
            case BAT:
                enemies.add(new Bat(x, y, 0));
                break;
            case KNIGHT:
                enemies.add(new Knight(x, y, 0));
                break;
            default:
                break;
        }
    }

    public void update() {
        for (int i = 0; i < enemies.size(); ) {
            Enemy e = enemies.get(i);
            updateEnemyMove(e);
            if (isAtEnd(e)) {
                enemies.remove(i);
            } else {
                i++;
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
        } else {
            setNewDirectionAndMove(e);
        }
    }

    private int getTileType(int x, int y) {
        return playing.getTileType(x, y);
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

    public void draw(Graphics g) {
        for (Enemy e : enemies) {
            drawEnemy(e, g);
        }
    }


    private void drawEnemy(Enemy e, Graphics g) {
        g.drawImage(enemyImgs[e.getEnemyType()], (int)e.getX(), (int)e.getY(), null);
    }
}
