package enemies;

import static helper.Constants.Enemies.*;

import managers.EnemyManager;

public class Knight extends Enemy {

    public Knight(float x, float y, int ID, EnemyManager enemyManager) {
        super(x, y, ID, KNIGHT, enemyManager);
    }
}
