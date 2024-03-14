package WizardTD;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import WizardTD.Game.UI.HealthBar;
import processing.core.PVector;

public class HealthBarTest {
    private static final float MAX_HP = 125;
    private static final float WIDTH = 100;
    private static final float HEIGHT = 10;
    private static final PVector POS = new PVector(5, 10);

    HealthBar testHealthBar = new HealthBar(POS, WIDTH, HEIGHT, MAX_HP);
    
    @Test
    void testInstantiateWithNullPos() {
        Exception exception = assertThrows(NullPointerException.class,
                                           () -> {new HealthBar(null, WIDTH, HEIGHT, MAX_HP); });

        String expectedMessage = "Unable to instantiate HealthBar because pos is null";
        String actualMessage = exception.getMessage();
        
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void testInstantiateWithCorrectPos() {
        assertEquals(POS, testHealthBar.getPos(), "HealthBar has pos: " + testHealthBar.getPos() +
                     " when it should be " + POS);
    }

    @Test
    // Checks that the health bar is initially set with the correct HP.
    void testInstantiateWithCorrectHP() {
        assertEquals(MAX_HP, testHealthBar.getMaxHP());
        assertEquals(MAX_HP, testHealthBar.getCurrentHP());
    }

    @Test
    void testAbleToSetHP() {
        float newHP = 33;
        testHealthBar.setCurrentHP(newHP);
        assertEquals(newHP, testHealthBar.getCurrentHP());
    }

    @Test
    void testHasCorrectWidth() {
        assertEquals(WIDTH, testHealthBar.getWidth());
    }

    @Test
    void testHasCorrectHeight() {
        assertEquals(HEIGHT, testHealthBar.getHeight());
    }
}

