package enemies;

import java.awt.Rectangle;

import static helper.Constants.Directions.*;

public abstract class Enemy {

    private float x, y;
    private Rectangle bounds;
    private int health, ID, enemyType, lastDir;

    public Enemy(float x, float y, int ID, int enemyType) {
        this.x = x;
        this.y = y;
        this.ID = ID;
        this.enemyType = enemyType;
        bounds = new Rectangle((int)x, (int)y, 32, 32);
        lastDir = -1;
    }

    public Enemy(float x, float y, int health, int ID, int enemyType) {
        this.x = x;
        this.y = y;
        this.health = health;
        this.ID = ID;
        this.enemyType = enemyType;
        bounds = new Rectangle((int)x, (int)y, 32, 32);
        lastDir = RIGHT;
    }

    public void move(float speed, int dir) {
        switch (dir) {
            case LEFT:
                this.x -= speed;
                lastDir = LEFT;
                break;
            case RIGHT:
                this.x += speed;
                lastDir = RIGHT;
                break;
            case UP:
                this.y -= speed;
                lastDir = UP;
                break;
            case DOWN:
                this.y += speed;
                lastDir = DOWN;
                break;
        
            default:
                break;
        }
    }

    public void setPosition(int x, int y) {
        //This is for position correction
        this.x = x;
        this.y = y;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public Rectangle getBounds() {
        return bounds;
    }

    public int getHealth() {
        return health;
    }

    public int getID() {
        return ID;
    }

    public int getEnemyType() {
        return enemyType;
    }

    public int getLastDir() {
        return lastDir;
    }    
}
