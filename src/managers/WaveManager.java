package managers;

import java.util.ArrayList;

import events.Wave;
import scenes.Playing;

public class WaveManager {
    
    private Playing playing;
    private ArrayList<Wave> waves = new ArrayList<>();
    private int enemySpawnTickLimit = 60 * 1;
    private int enemySpawnTick = enemySpawnTickLimit, enemyIndex = 0, waveIndex = 0, waveTick = 0;
    private int waveTickLimit = 60 * 5 ;
    private boolean waveStartTimer = false, waveTickTimerOver = false;

    public WaveManager(Playing playing) {
        this.playing = playing;
        createWaves();
    }

    public void update() {
        if(enemySpawnTick < enemySpawnTickLimit)
            enemySpawnTick++;

        if(waveStartTimer) {
            waveTick++;
            if(waveTick >= waveTickLimit) {
                waveTickTimerOver = true;
            }
        }
    }

    public void increaseWaveIndex() {
        waveIndex++;
        waveTick = 0;
        waveStartTimer = false;
        waveTickTimerOver = false;
    }

    public void startWaveTimer() {
        waveStartTimer = true;
    }

    public int getNextEnemy(){
        enemySpawnTick = 0;
        return waves.get(waveIndex).getEnemyList().get(enemyIndex++);
    }

    private void createWaves() {
        waves.add(new Wave(new ArrayList<Integer>() {{
            add(0);
            add(0);
            add(0);
            add(0);
        }}));
        waves.add(new Wave(new ArrayList<Integer>() {{
            add(1);
            add(1);
            add(1);
            add(1);
        }}));
        waves.add(new Wave(new ArrayList<Integer>() {{
            add(0);
            add(1);
            add(2);
            add(3);
        }}));    

        // generate additional waves up to 10 total waves
        int totalWaves = 10; // total number of waves desired
        java.util.Random rnd = new java.util.Random();
        int baseEnemies = 5; // starting number of enemies for wave 1/2
        // we already added 3 waves above (indexes 0,1,2), so start from index 3
        for (int w = 3; w < totalWaves; w++) {
            int enemiesCount = baseEnemies + (w - 2) * 2; // increase enemy count with wave index
            ArrayList<Integer> enemyList = new ArrayList<>();
            for (int e = 0; e < enemiesCount; e++) {
                enemyList.add(rnd.nextInt(4)); // random enemy id in [0,3]
            }
            waves.add(new Wave(enemyList));
        }
    }

    public void resetEnemyIndex() {
        enemyIndex = 0;
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

    public boolean isThereMoreWaves(){
        return waveIndex + 1 < waves.size();
    }

    public boolean isWaveTimerOver(){
        return waveTickTimerOver;
    }

    public int getWaveIndex() {
        return waveIndex;
    }

    public float getTimeLeft(){
        float ticksLeft = (waveTickLimit - waveTick);
        return ticksLeft / 60.0f;
    }

    public boolean isWaveTimerStarted(){
        return waveStartTimer;
    }

    public void reset() {
        waves.clear();
        createWaves();
        waveIndex = 0;
        enemyIndex = 0;
        waveTick = 0;
        waveStartTimer = false;
        waveTickTimerOver = false;
        enemySpawnTick = enemySpawnTickLimit;
    }
}
