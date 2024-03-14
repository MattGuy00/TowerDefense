package WizardTD.Game;


import java.util.LinkedList;

import WizardTD.App;
import WizardTD.Game.Entities.Monsters.Monster;
import WizardTD.Game.UI.WaveTimer;
import processing.data.JSONArray;

/**
 * Represents the {@code WaveManager} class.
 * <p>
 * The wave manager handles the logic of starting waves,
 * ending waves, and moving monsters into the active monsters list.
 */
public class WaveManager {
    /**
     * The active monsters on the board.
     */
    private LinkedList<Monster> activeMonsters;
    /**
     * The waves that have yet to start.
     */
    private LinkedList<Wave> waves = new LinkedList<>();
    /**
     * A visual timer for the waves
     */
    private WaveTimer waveTimer;
    /**
     * The current wave.
     */
    private Wave currentWave;
    /**
     * The current wave's duration.
     */
    private float currentWaveDuration;
    /**
     * The monsters that in the wave, but haven't
     * been added to the active monster list.
     */
    private LinkedList<Monster> currentWaveMonsters = new LinkedList<>();

    /**
     * The number of frames since creation.
     */
    private int frameCount = 0;
    /**
     * For tracking the number of finished waves.
     */
    private int waveNumber = 1;

    /**
     * The total number of waves in the game.
     */
    private int totalWaves = 0;

    /**
     * The speed multiplier.
     */
    private float speedMultiplier = 1;

    /**
     * {@code WaveManager}'s constructor.
     * Loads all waves in the game for later use.
     * @param configsForWaves A {@code JSONArray} that is passed to the waves. 
     * @param activeMonsters The active monsters on the board.
     */
    public WaveManager(JSONArray configsForWaves, LinkedList<Monster> activeMonsters) {
        loadWaves(configsForWaves);
        this.totalWaves = waves.size();
        // Gets the first wave's pre wave pause
        this.currentWaveDuration = this.waves.peek().getTimeBetweenWaves();
        this.activeMonsters = activeMonsters;
    }

    /**
     * Returns the duration of the current wave.
     * @return The duration of the current wave.
     */
    public float getCurrentWaveDuration() { return this.currentWaveDuration; }
    /**
     * Checks if the current wave has finished.
     * @return {@code true} if the all the monsters in the wave have been spawned,
     * otherwise {@code false}.
     */
    public boolean isWaveFinished() { 
        return this.currentWaveDuration <= 0 && this.waves.isEmpty() ; 
    }

    /**
     * Gets the waves that have yet to be spawned.
     * @return The waves after the current wave.
     */
    public LinkedList<Wave> getWaves() { return this.waves; }
    /**
     * Gets the wave's position in the queue.
     * @return The position of the wave in the queue.
     */
    public int getWaveNumber()  { return this.waveNumber; }
    /**
     * Checks if all waves have been completed.
     * @return {@code true} if the current wave is the last one or if there are no waves left,
     * otherwise {@code false}.
     */
    public boolean areWavesFinished() { 
        return this.waveNumber >= this.totalWaves && 
               this.waves.isEmpty() && this.currentWaveMonsters.isEmpty(); 
    }

    /**
     * Adds a wave timer to the wave manager.
     * @param waveTimer The wave timer that is added.
     * @see WaveTimer
     */
    public void addWaveTimer(WaveTimer waveTimer) { this.waveTimer = waveTimer; }

    /**
     * Sets the speed multiplier of the wave manager.
     * @param speedMulti The new speed multiplier.
     */
    public void setSpeedMultiplier(float speedMulti) {
        this.speedMultiplier = speedMulti;
    }

    /**
     * Executes the logic for starting waves.
     * <p>
     * This should be called on every frame.
     */
    public void tick() {
        if (this.currentWaveDuration <= 0) {
            startNextWave();
            return;
        } 

        countDownTimeBetweenWaves();
        spawnMonstersInWave();
    }

    /**
     * Counts down the time between the current wave and the next.
     */
    private void countDownTimeBetweenWaves() {
        if (this.waves.isEmpty()) {
            return;
        }

        if (this.waves.size() <= 1 && this.waveTimer != null) {
            this.waveTimer.finalWave(true);
        }

        this.currentWaveDuration -= this.speedMultiplier / App.FPS;

        if (this.waveTimer != null) {
            this.waveTimer.setTime(this.currentWaveDuration);
        }
    }

    /**
     * Starts the next wave in the wave list.
     */
    public void startNextWave() {
        if (this.waves.isEmpty()) {
            return;
        }

        this.currentWave = waves.poll();
        this.currentWaveMonsters = currentWave.getMonstersInWave();
        float nextWavesPreWavePause = this.waves.peek() != null ? 
                                      this.waves.peek().getTimeBetweenWaves() : 0;
        this.currentWaveDuration = currentWave.getWaveDuration() + nextWavesPreWavePause;
        
        if (this.waves.size() >= 1 && this.waveTimer != null) {
            this.waveTimer.setCurrentWave(++this.waveNumber);
        } 
        
    }

    /**
     * Spawns the monsters in wave based on the given time between each spawn.
     */
    public void spawnMonstersInWave() {      
        if (this.currentWaveMonsters.isEmpty()) {
            frameCount = 0;
            return;
        }
       
        if (Math.round(frameCount % this.currentWave.getFramesBetweenEachMonsterSpawn()) == 0) {
            this.activeMonsters.add(this.currentWaveMonsters.poll());
        }
        frameCount += this.speedMultiplier;
    }

    /**
     * Loads all waves in the game.
     * @param configsForWaves The {@code JSONArray} passed to each wave.
     */
    private void loadWaves(JSONArray configsForWaves) {
        for (int i = 0; i < configsForWaves.size(); i++) {
            waves.add(new Wave(configsForWaves.getJSONObject(i)));
        }
    }  
}
