package WizardTD;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import WizardTD.Game.Game;
import WizardTD.Game.Entities.Monsters.Moag;
import WizardTD.Game.Entities.Monsters.Monster;
import processing.core.PApplet;
import processing.core.PVector;
import processing.data.JSONObject;

public class GameTest {
    private static JSONObject config;
    private static Game game;

    private static App app;

    @BeforeAll
    static void setup() {
        app = new App();
        app.loop();
        PApplet.runSketch(new String[] { "App" }, app);
        app.setup();
        app.delay(1000);
        config = app.loadJSONObject(app.configPath);
    }

    @BeforeEach
    void setupGame() {
        game = new Game(config );
    }

    @Test
    // Barebones test to see if game will crash when clicking on any of pixels in the window.
    void testGameMousePressedWithoutCrashing() {
        for (int y = 0; y < App.HEIGHT; y++) {
            for (int x = 0; x < App.WIDTH; x++) {
                game.mousePressed(App.LEFT, x, y);
            }
        }

        for (int y = 0; y < App.HEIGHT; y++) {
            for (int x = 0; x < App.WIDTH; x++) {
                game.mousePressed(App.RIGHT, x, y);
            }
        }
    }

    @Test
    void testGameTicksAliveMonsters() {
        game.getActiveMonsters().clear();
        game.getActiveMonsters().add(new Monster(100, 1, 1, 10));

        PVector startPos = game.getActiveMonsters().get(0).getPos().copy();
        game.tickMonsters();
        game.tickMonsters();
        game.tickMonsters();
        game.tickMonsters();
        PVector endPos = game.getActiveMonsters().get(0).getPos().copy();

        assertNotEquals(startPos, endPos);
    }

    @Test
    // Tests that mana is removed and the monster respawns when it reaches the house.
    void testMonsterReachesHouse() {
        game.getActiveMonsters().clear();
        game.getActiveMonsters().add(new Monster(100, 1, 1, 10));

        PVector expectedPos = game.getActiveMonsters().get(0).getStartingTile().getCenterPos();
        PVector housePos = game.getBoard().getWizHouse().getCenterPos();
        game.getActiveMonsters().get(0).setCenterPos(housePos.x, housePos.y);

        float initialMana = game.getManaPool().getCurrentMana();

        game.tickMonsters();

        PVector actualPos = game.getActiveMonsters().get(0).getCenterPos();
        float actualMana = game.getManaPool().getCurrentMana();

        assertEquals(expectedPos, actualPos);
        assertTrue(actualMana < initialMana);
    }

    @Test
    void testManaAddedOnMonsterDeath() {
        game.getActiveMonsters().clear();
        game.getActiveMonsters().add(new Monster(100, 1, 1, 10));
        
        float initialMana = game.getManaPool().getCurrentMana();
        game.getActiveMonsters().get(0).setDead(true);

        game.tickMonsters();

        float actualMana = game.getManaPool().getCurrentMana();
        assertTrue(actualMana > initialMana);
    }

    @Test
    void testMoagAddedToActiveMonstersOnDeath() {
        game.getActiveMonsters().clear();
        int numOfMonstersInMoag = 10;
        game.getActiveMonsters().add(new Moag(numOfMonstersInMoag, 100, 1, 1, 10));
        game.getActiveMonsters().get(0).setDead(true);

        game.tickMonsters();
        int numberOfActiveMonsters = game.getActiveMonsters().size();

        assertEquals(numOfMonstersInMoag, numberOfActiveMonsters);
    }
}
