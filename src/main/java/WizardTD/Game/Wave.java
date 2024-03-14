package WizardTD.Game;

import java.util.LinkedList;

import WizardTD.App;
import WizardTD.Game.Entities.Monsters.Beetle;
import WizardTD.Game.Entities.Monsters.Gremlin;
import WizardTD.Game.Entities.Monsters.Moag;
import WizardTD.Game.Entities.Monsters.Monster;
import WizardTD.Game.Entities.Monsters.Worm;
import processing.data.JSONArray;
import processing.data.JSONObject;

/**
 * Represents the {@code Wave} class.
 * <p>
 * A wave object loads all data about a wave, including its monsters. 
 * <p>
 * This data is read from a {@code JSONObject}.
 */
public class Wave {
    /**
     * The duration of the wave.
     */
    private final float waveDuration;
    /**
     * The amount of time before the next wave starts.
     */
    private final float timeBetweenWaves;
    /**
     * The monsters in the wave.
     */
    private LinkedList<Monster> monsters = new LinkedList<>();

    /**
     * The frames between each monster spawn.
     */
    private float framesBetweenMonsterSpawn;
    
    /**
     * {@code Wave}'s constructor.
     * <p>
     * Constructs a wave object that holds the relevant information
     * about a wave.
     * @param waveInfo The {@code JSONObject} that {@code Wave} reads from.
     */
    public Wave(JSONObject waveInfo) {
        this.waveDuration = waveInfo.getInt("duration");
        this.timeBetweenWaves = waveInfo.getFloat("pre_wave_pause");
        loadMonsters(waveInfo.getJSONArray("monsters"));

        // How many frames should pass before we spawn the next monster
        this.framesBetweenMonsterSpawn = (App.FPS * this.waveDuration) / this.monsters.size();
        
    }

    /**
     * Gets the duration of the wave.
     * @return The duration of the wave.
     */
    public float getWaveDuration() { return this.waveDuration; }
    /**
     * Gets the time before the next wave spawns.
     * @return The time before the next wave spawns.
     */
    public float getTimeBetweenWaves() { return this.timeBetweenWaves; }
    /**
     * Gets the monsters that are in the wave.
     * @return The monsters that are in the wave.
     */
    public LinkedList<Monster> getMonstersInWave() { return this.monsters; }
    /**
     * Gets the number of frames between each monster's spawn.
     * @return The number of frames between each monster's spawn.
     */
    public float getFramesBetweenEachMonsterSpawn() { return this.framesBetweenMonsterSpawn; }

    /**
     * Loads the monsters in the wave using the given 
     * {@code JSONArray}.
     * @param monsterInfo The {@code JSONArray} used to load the monsters.
     */
    private void loadMonsters(JSONArray monsterInfo) {
        for (int i = 0; i < monsterInfo.size(); i++) {
            JSONObject m = monsterInfo.getJSONObject(i);
            String monsterType = m.getString("type");
            int hp = m.getInt("hp");
            float speed = m.getFloat("speed");
            float armour = m.getFloat("armour");
            int manaGainedOnKill = m.getInt("mana_gained_on_kill");
            int numberOfMonsters = m.getInt("quantity");
            int monstersInMoag = m.getInt("monsters_in_moag", 0);

            for (int j = 0; j < numberOfMonsters; j++) {
                if ("gremlin".equals(monsterType)) {
                    monsters.add(new Gremlin(hp, speed, armour, manaGainedOnKill));
                } else if ("worm".equals(monsterType)) {
                    monsters.add(new Worm(hp, speed, armour, manaGainedOnKill));
                } else if ("beetle".equals(monsterType)) {
                    monsters.add(new Beetle(hp, speed, armour, manaGainedOnKill));
                } else if ("moag".equals(monsterType)) {
                    monsters.add(new Moag(monstersInMoag, hp, speed, armour, manaGainedOnKill));
                }
            }
        } 
    }
}
