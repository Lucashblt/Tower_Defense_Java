package enemies;

import static helper.Constants.Enemies.*;

import managers.EnemyManager;

public class Wolf extends Enemy {

    public Wolf(float x, float y, int ID, EnemyManager enemyManager) {
        super(x, y, ID, WOLF, enemyManager);
    }

    public Wolf(float x, float y, int ID, EnemyManager enemyManager, int waveNumber) {
        super(x, y, ID, WOLF, enemyManager, waveNumber);
    }
}
