package WizardTD;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.LinkedList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import WizardTD.Game.WaveManager;
import WizardTD.Game.Entities.Monsters.Monster;
import processing.data.JSONArray;

public class WaveManagerTest {
    private static String CONFIG_TEXT = "[ { \"duration\": 5,\r\n" + //
            "      \"pre_wave_pause\": 3,\r\n" + //
            "      \"monsters\": [\r\n" + //
            "          {\r\n" + //
            "            \"type\": \"DNE\",\r\n" + //
            "            \"hp\": 2,\r\n" + //
            "            \"speed\": 1,\r\n" + //
            "            \"armour\": 0.5,\r\n" + //
            "            \"mana_gained_on_kill\": 30,\r\n" + //
            "            \"quantity\": 5\r\n" + //
            "          }\r\n" + //
            "      ] } ]";
    private static JSONArray CONFIG_FILE = JSONArray.parse(CONFIG_TEXT);
    private static LinkedList<Monster> monsters;
    private static  WaveManager waveManager;

    @BeforeEach
    void setupWave() {
        waveManager = new WaveManager(CONFIG_FILE, monsters);
    }

    @Test
    // Tests that the wave manager starts with the correct pre wave pause.
    void testCorrectWaveDurationOnCreation() {
        assertEquals(3, waveManager.getCurrentWaveDuration());
    }

    @Test
    // Tests that a new wave starts.
    void testAbleToStartNextWave() {
        int initialAmountOfWaves = waveManager.getWaves().size();
        waveManager.startNextWave();

        int actualNumberOfWaves = waveManager.getWaves().size();
        assertTrue(actualNumberOfWaves < initialAmountOfWaves);
    }

    @Test
    // Tests that the wave duration is correctly set when a new wave is spawned.
    void testWaveDurationSetOnNextWave() {
        waveManager.startNextWave();
        assertEquals(5, waveManager.getCurrentWaveDuration());
    }
    
}
