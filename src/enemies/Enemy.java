package enemies;

import java.awt.Rectangle;

import static helper.Constants.Directions.*;

public abstract class Enemy {

    protected float x, y;
    protected Rectangle bounds;
    protected int health, ID, enemyType, lastDir;
    protected int maxHealth;
    protected boolean alive = true;
    protected int slowTickLimit = 120;
	protected int slowTick = slowTickLimit;

    public Enemy(float x, float y, int ID, int enemyType) {
        this.x = x;
        this.y = y;
        this.ID = ID;
        this.enemyType = enemyType;
        bounds = new Rectangle((int)x, (int)y, 32, 32);
        lastDir = -1;
        setStartHealth();
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

    private void setStartHealth() {
        this.health = helper.Constants.Enemies.GetStartHealth(enemyType);
        this.maxHealth = this.health;
    }

    public void hurt(int damage) {
        this.health -= damage;
        if (this.health <= 0) {
            this.alive = false;
        }
    }

    public void move(float speed, int dir) {
        lastDir = dir;

        if(slowTick < slowTickLimit) {
            slowTick++;
            speed *= 0.5f;
        }

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

        updateHitbox();
    }

    private void updateHitbox() {
        this.bounds.x = (int) this.x;
        this.bounds.y = (int) this.y;
    }

    public void setPosition(int x, int y) {
        //This is for position correction
        this.x = x;
        this.y = y;
    }

    public void slow() {
		slowTick = 0;
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

    public float getHealthBarFloat() {
        return (float)health / (float)maxHealth;
    }

    public boolean isAlive() {
        return alive;
    }

    public boolean isSlowed() {
        return slowTick < slowTickLimit;
    }
}
