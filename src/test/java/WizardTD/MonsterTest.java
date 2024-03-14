package WizardTD;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import WizardTD.Game.Board.Board;
import WizardTD.Game.Board.Path;
import WizardTD.Game.Board.Tile;
import WizardTD.Game.Entities.Monsters.Beetle;
import WizardTD.Game.Entities.Monsters.Monster;
import processing.core.PApplet;
import processing.core.PVector;

public class MonsterTest {
    private static App app;
    private static final int HP = 100;
    private static final float SPEED = 4f;
    private static final float ARMOUR = 0.5f;
    private static final int MANA_ON_DEATH = 30;
    private static Board board;
    private static Monster monster;

    @BeforeAll
    static void setup() {
        app = new App();
        app.loop();
        PApplet.runSketch(new String[] { "App" }, app);
        app.setup();
        app.delay(1000); // to give time to initialise stuff before drawing begins
        board = new Board("level1.txt");
        
        Monster.findMonsterPaths(board);
    }

    @BeforeEach
    void setupMonster() {
        monster = new Monster(HP, SPEED, ARMOUR, MANA_ON_DEATH);
    }
    
    @Test
    void testExceptionOnNegativeHealth() {
        Exception exception = assertThrows(IllegalArgumentException.class,
                                           () -> {new Monster(-1, SPEED, ARMOUR, MANA_ON_DEATH); });

        String expectedMessage = "initial HP must be > 0.";
        String actualMessage = exception.getMessage();
        
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void testExceptionOnNoHealth() {
        Exception exception = assertThrows(IllegalArgumentException.class,
                                           () -> {new Monster(0, SPEED, ARMOUR, MANA_ON_DEATH); });

        String expectedMessage = "initial HP must be > 0.";
        String actualMessage = exception.getMessage();
        
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void testExceptionOnNegativeSpeed() {
        Exception exception = assertThrows(IllegalArgumentException.class,
                                           () -> {new Monster(HP, -1, ARMOUR, MANA_ON_DEATH); });

        String expectedMessage = "Initial speed must be > 0.";
        String actualMessage = exception.getMessage();
        
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void testExceptionOnNoSpeed() {
        Exception exception = assertThrows(IllegalArgumentException.class,
                                           () -> {new Monster(HP, 0, ARMOUR, MANA_ON_DEATH); });

        String expectedMessage = "Initial speed must be > 0.";
        String actualMessage = exception.getMessage();
        
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void testExceptionOnNegativeArmour() {
        Exception exception = assertThrows(IllegalArgumentException.class,
                                           () -> {new Monster(HP, SPEED, -1, MANA_ON_DEATH); });

        String expectedMessage = "Armour must be >= 0.";
        String actualMessage = exception.getMessage();
        
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void testExceptionOnNegativeManaOnDeath() {
        Exception exception = assertThrows(IllegalArgumentException.class,
                                           () -> {new Monster(HP, SPEED, ARMOUR, -1); });

        String expectedMessage = "Mana on death must be >= 0.";
        String actualMessage = exception.getMessage();
        
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void testMonsterHasValidPath() {
        assertTrue(monster.getPath().size() > 0);
    }

    @Test
    void testMonsterMovesAlongPath() {
        Monster test = new Monster(HP, SPEED, ARMOUR, MANA_ON_DEATH);
        PVector expectedEndPos = board.getWizHouse().getCenterPos();

        while (test.getMoveIter() < test.getPath().size()) {
            test.tick();
        }

        PVector actualEndPos = test.getCenterPos();
        assertEquals(expectedEndPos, actualEndPos);
    }

    @Test
    void testAbleToSetPath() {
        Monster test = new Monster(HP, SPEED, ARMOUR, MANA_ON_DEATH);
        ArrayList<Tile> expectedPath = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            expectedPath.add(new Path(i, i));
        }

        test.setPath(expectedPath);
        ArrayList<Tile> actualPath = test.getPath();

        assertEquals(expectedPath, actualPath);
    }

    @Test
    // Tests the beetle's ability to move along tiles.
    void testBeetleMovesTowardsDest() {
        Beetle beetle = new Beetle(HP, SPEED, ARMOUR, MANA_ON_DEATH);
        beetle.move();
        
        while (beetle.getMoveIter() < beetle.getPath().size()) {
            beetle.move();
            
            float initialDistFromTile = beetle.getCenterPos()
                                              .dist(beetle.getCurrentDest().getCenterPos());

            float actualDistFromTile = beetle.getCenterPos()
                                             .dist(beetle.getCurrentDest().getCenterPos());

            assertTrue(actualDistFromTile <= initialDistFromTile);
        }
    }

    @Test
    // Tests that the beetle correctly respawns.
    void testBeetleRespawns() {
        Beetle beetle = new Beetle(HP, SPEED, ARMOUR, MANA_ON_DEATH);
        int expectedMoveIter = beetle.getMoveIter();
        PVector expectedPos = beetle.getStartingTile().getCenterPos();

        beetle.setMoveIter(10);
        beetle.setCenterPos(-1, -1);

        beetle.respawn();

        int actualMoveIter = beetle.getMoveIter();
        PVector actualPos = beetle.getCenterPos();

        assertEquals(expectedMoveIter, actualMoveIter);
        assertEquals(expectedPos, actualPos);
    }

    @Test
    // tests that beetle's death animation works.
    void testPlayDeathAnimation() {
        Beetle beetle = new Beetle(HP, SPEED, ARMOUR, MANA_ON_DEATH);
        int lengthOfAnimation = beetle.getDeathAnimImages().length * 4;

        for (int i = 0; i < lengthOfAnimation; i++) {
            beetle.playDeathAnim(app.g);
        }

        assertTrue(beetle.getDeathImageiter() >= beetle.getDeathAnimImages().length);
        assertTrue(beetle.isDead());
    }

}
