package managers;

import scenes.Playing;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import enemies.Enemy;
import helper.LoadSave;
import static helper.Constants.Directions.*;
import static helper.Constants.Tiles.*;

public class EnemyManager {
    
    private Playing playing;
    private BufferedImage[] enemyImgs;
    private ArrayList<Enemy> enemies = new ArrayList<>();
    private float speed = 0.5f;

    public EnemyManager(Playing playing) {
        this.playing = playing;
        enemyImgs = new BufferedImage[4];
        addEnemy(0 * 32, 19 * 32);
        loadEnemyImgs();
    }

    
    private void loadEnemyImgs() {
        BufferedImage atlas = LoadSave.getSpriteAtlas();
        
        for (int i = 0; i < 4; i++) {
            enemyImgs[i] = atlas.getSubimage(i * 32, 32, 32, 32);
        }
    }

    public void addEnemy(int x, int y) {
        enemies.add(new Enemy(x, y, 100, 0, 0));
    }

    public void update() {
        for (Enemy e : enemies) {
            updateEnemyMove(e);
        }
    }

    public void updateEnemyMove(Enemy e) {
        int newX = (int)(e.getX() + getSpeedXAndWidth(e.getLastDir()));
        int newY = (int)(e.getY() + getSpeedYAndHeight(e.getLastDir()));

        if(getTileType(newX, newY) == ROAD_TILE) {
            e.move(speed, e.getLastDir());
        }else if(isAtEnd(e)) {
            //enemy reached the end
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
            // case LEFT:
            //     if(xCord > 0)
            //         xCord--;
            //     break;
            // case UP:
            //     if(yCord > 0)
            //         yCord--;
            //     break;
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
