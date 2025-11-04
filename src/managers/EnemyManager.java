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
    private float speed = 0.5f;
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
        for (Enemy e : enemies) {
            updateEnemyMove(e);
        }
    }

    public void updateEnemyMove(Enemy e) {
        if(e.getLastDir() == -1)
            setNewDirectionAndMove(e);

        int newX = (int)(e.getX() + getSpeedXAndWidth(e.getLastDir()));
        int newY = (int)(e.getY() + getSpeedYAndHeight(e.getLastDir()));

        if(getTileType(newX, newY) == ROAD_TILE) {
            e.move(speed, e.getLastDir());
        }else if(isAtEnd(e)) {
            //enemy reached the end
            System.err.println("Enemy reached the end");
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
            int newY = (int)(e.getY() + getSpeedYAndHeight(UP));
            if (getTileType((int) e.getX(), newY) == ROAD_TILE) {
                e.move(speed, UP);
            } else {
                e.move(speed, DOWN);
            }
        } else {
            int newX = (int)(e.getX() + getSpeedXAndWidth(RIGHT));
            if (getTileType(newX, (int) e.getY()) == ROAD_TILE) {
                e.move(speed, RIGHT);
            } else {
                e.move(speed, LEFT);
            }
        }
    }

    private void fixEnemyPosition(Enemy e, int dir, int xCord, int yCord) {
        switch (dir) {
            case RIGHT:    
                if (xCord < 19) {
                    xCord++;
                }
                break;  
            case DOWN:
                if (yCord < 19) {
                    yCord++;
                }
                break;        
            default:
                break;
        }
        e.setPosition(xCord * 32, yCord * 32);
    }

    private boolean isAtEnd(Enemy e) {
        if (e.getX() == end.getxCord() && e.getY() == end.getyCord()) {
            return true;
        }
        return false;
    }

    private float getSpeedXAndWidth(int dir) {
        if(dir == LEFT)
            return -speed;
        else if(dir == RIGHT)
            return speed + 32;
        return 0;
    }

    private float getSpeedYAndHeight(int dir) {
        if(dir == UP)
            return -speed;
        else if(dir == DOWN)
            return speed + 32;
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
