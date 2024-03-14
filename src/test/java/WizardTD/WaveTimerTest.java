package WizardTD;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import WizardTD.Game.UI.WaveTimer;
import processing.core.PVector;

public class WaveTimerTest {
    private static final float WIDTH = 2;
    private static final float HEIGHT = 100;
    private static final PVector POS = new PVector(3, 55);
    private static final WaveTimer timer = new WaveTimer(POS.x, POS.y, WIDTH, HEIGHT);

    @Test
    // Tests that the wave timer is correctly instantiated.
    void testTimerInstantiatesCorrectly() {
       assertEquals(POS, timer.getPos());
       assertEquals(WIDTH, timer.getWidth());
       assertEquals(HEIGHT, timer.getHeight());
    }

    @Test
    // Tests that the time is correctly set.
    void testAbleToSetTime() {
        float newTime = 32.34f;
        timer.setTime(newTime);
        assertEquals(newTime, timer.getTime());
    }

    @Test
    // Tests that the wave count is correctly set.
    void testAbleToSetCurrentWaveCount() {
        int newCount = 31;
        timer.setCurrentWave(newCount);
        assertEquals(newCount, timer.getWavePos());
    }

    @Test
    // Tests that the timer's final wave status is correctly set.
    void testSetFinalWaveStatus() {
        boolean finalWave = true;
        timer.finalWave(finalWave);
        assertTrue(timer.isFinalWave() == finalWave);
    }
}
