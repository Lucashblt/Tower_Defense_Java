package enemies;

import java.awt.Rectangle;

import helper.Poolable;
import managers.EnemyManager;

import static helper.Constants.Directions.*;
import static helper.Constants.Enemies.*;

public abstract class Enemy implements Poolable {

    protected float x, y;
    protected Rectangle bounds;
    protected int health, ID, enemyType, lastDir;
    protected int maxHealth;
    protected boolean alive = true;
    protected int slowTickLimit = 120;
	protected int slowTick = slowTickLimit;
    protected EnemyManager enemyManager;
    protected int enemyIndex; // Index de spawn (buff tous les 100 ennemis)

    public Enemy(float x, float y, int ID, int enemyType, EnemyManager enemyManager) {
        this(x, y, ID, enemyType, enemyManager, 0);
    }

    public Enemy(float x, float y, int ID, int enemyType, EnemyManager enemyManager, int enemyIndex) {
        this.x = x;
        this.y = y;
        this.ID = ID;
        this.enemyType = enemyType;
        this.enemyManager = enemyManager;
        this.enemyIndex = enemyIndex;
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
        if (enemyManager != null && enemyManager.isSimulationMode()) {
            // Buffs tous les 100 ennemis
            int buffLevel = enemyIndex / 100;
            this.health = helper.Constants.Enemies.GetStartHealthSimulation(enemyType, buffLevel);
        } else {
            // Normal mode utilise toujours les waves (compatibilité)
            this.health = GetStartHealth(enemyType, enemyIndex);
        }
        this.maxHealth = this.health;
    }

    public void hurt(int damage) {
        this.health -= damage;
        if (this.health <= 0) {
            this.alive = false;
            enemyManager.rewardPlayer(GetEnemyReward(enemyType));
        }
    }

    public void kill() {
        // when the enemy reaches the end
        this.alive = false;
        this.health = 0;
    }

    public void move(float speed, int dir) {
        lastDir = dir;

        if(slowTick < slowTickLimit) {
            slowTick++;
            speed *= 0.5f;
        }

        // Apply speed buff
        if (enemyManager != null && enemyManager.isSimulationMode()) {
            // Simulation mode: 0.01% buff tous les 100 ennemis
            int buffLevel = enemyIndex / 100;
            float buffMultiplier = 1.0f + (buffLevel * 0.0001f);
            speed *= buffMultiplier;
        } else {
            // Normal mode: 10% buff every 5 waves
            int buffLevel = enemyIndex / 5;
            float buffMultiplier = 1.0f + (buffLevel * 0.10f);
            speed *= buffMultiplier;
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

    @Override
    public boolean isActive() {
        return alive;
    }

    @Override
    public void reset() {
        this.alive = false;
        this.health = 0;
        this.x = 0;
        this.y = 0;
        this.lastDir = -1;
        this.slowTick = slowTickLimit;
        this.bounds.setLocation(0, 0);
    }

    /**
     * Réinitialise l'ennemi avec de nouvelles valeurs pour réutilisation
     */
    public void reuse(float x, float y, int enemyIndex) {
        this.x = x;
        this.y = y;
        this.enemyIndex = enemyIndex;
        this.alive = true;
        this.lastDir = -1;
        this.slowTick = slowTickLimit;
        this.bounds.setLocation((int)x, (int)y);
        setStartHealth();
    }
}
