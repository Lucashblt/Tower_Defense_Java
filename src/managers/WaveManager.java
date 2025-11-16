package managers;

import java.util.ArrayList;

import events.Wave;
import scenes.Playing;

public class WaveManager {
    
    private Playing playing;
    private ArrayList<Wave> waves = new ArrayList<>();
    private int enemySpawnTickLimit = 60 * 1;
    private int enemySpawnTick = 0;
    private int enemyIndex = 0, waveIndex = 0;

    public WaveManager(Playing playing) {
        this.playing = playing;
        createWaves();
    }

    public void update() {
        if(enemySpawnTick < enemySpawnTickLimit)
            enemySpawnTick++;
    }

    public int getNextEnemy(){
        enemySpawnTick = 0;
        return waves.get(waveIndex).getEnemyList().get(enemyIndex++);
    }

    private void createWaves() {
        waves.add(new Wave(new ArrayList<Integer>() {{
            add(0);
            add(1);
            add(2);
            add(3);
        }}));
    }

    public ArrayList<Wave> getWaves() {
        return waves;
    }

    public boolean isTimeForNewEnemy() {
        return enemySpawnTick >= enemySpawnTickLimit;
    }

    public boolean isThereMoreEnemiesInWave() {
        return enemyIndex < waves.get(waveIndex).getEnemyList().size();
    } 
}
