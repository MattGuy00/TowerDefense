package WizardTD;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import WizardTD.Game.Game;
import WizardTD.Game.Board.Tile;
import WizardTD.Game.Entities.Towers.Tower;
import WizardTD.Game.Player.InputManager;
import WizardTD.Game.Player.InputManager.Keybindings;
import WizardTD.Game.UI.Buttons.Button;
import processing.core.PApplet;
import processing.core.PVector;
import processing.data.JSONObject;

public class InputManagerTest {
    private static JSONObject config;
    private static App app;
    private static Game game;
    private static InputManager inputManager;

    @BeforeAll
    static void setupApp() {
        app = new App();
        app.loop();
        PApplet.runSketch(new String[] { "App" }, app);
        app.setup();
        app.delay(1000);
        config = app.loadJSONObject(app.configPath);
    }

    @BeforeEach
    void setup() {
        game = new Game(config); 
        inputManager = game.getInputManager();    
    }

    @Test
    // Tests that the player enters the rebind menu on right click.
    void testOnRightClick() {
        for (Map.Entry<Keybindings, Button> entry : inputManager.getBoundButtons().entrySet()) {
            Button boundButton = entry.getValue();

            inputManager.onRightClick(boundButton.getPos().x + 3, boundButton.getPos().y + 3);
            assertTrue(inputManager.isPlayerInRebindMenu());
            
            inputManager.onRightClick(boundButton.getPos().x + 3, boundButton.getPos().y + 3);
            assertFalse(inputManager.isPlayerInRebindMenu());
        }
    }

    @Test
    // Tests that buttons activate when clicked, except for increase mana button.
    void testOnLeftClickButtonActivates() {
        for (Map.Entry<Keybindings, Button> entry : inputManager.getBoundButtons().entrySet()) {
            Button boundButton = entry.getValue();
            
            if (entry.getKey() == Keybindings.INCREASE_MANA_KEY) {
                continue;
            }

            inputManager.onLeftClick(boundButton.getPos().x + 2, boundButton.getPos().y + 2);
            assertTrue(boundButton.isButtonActivated());
        }
    }

    @Test
    // Tests that a tower is placed when the build tower button is clicked on.
    void testOnLeftClickTowerIsPlaced() {
        Button boundButton = inputManager.getBoundButtons().get(Keybindings.BUILD_TOWER_KEY);
            
        inputManager.onLeftClick(boundButton.getPos().x + 2, boundButton.getPos().y + 2);
        
        HashMap<PVector, Tile> board = game.getBoard().getLevelTiles();
        Tile validTile = null;
        for (Map.Entry<PVector, Tile> entry : board.entrySet()) {
            if (!entry.getValue().isOccupied() && entry.getValue().isPlaceable()) {
                validTile = entry.getValue();
                break;
            }
        }

        int expectedSize = game.getTowerManager().getTowers().size() + 1;

        inputManager.onMouseMoved(validTile.getCenterPos().x, validTile.getCenterPos().y);
        inputManager.onLeftClick(validTile.getCenterPos().x, validTile.getCenterPos().y);

        int actualSize = game.getTowerManager().getTowers().size();

        assertEquals(expectedSize, actualSize);
        assertFalse(boundButton.isButtonActivated());
    }

    @Test
    // Tests for the ability for the player to upgrade a tower on left click.
    void testCanUpgradeTowerOnLeftClick() {
        Button boundButton = inputManager.getBoundButtons().get(Keybindings.BUILD_TOWER_KEY);
            
        inputManager.onLeftClick(boundButton.getPos().x + 2, boundButton.getPos().y + 2);
        
        HashMap<PVector, Tile> board = game.getBoard().getLevelTiles();
        Tile validTile = null;
        for (Map.Entry<PVector, Tile> entry : board.entrySet()) {
            if (!entry.getValue().isOccupied() && entry.getValue().isPlaceable()) {
                validTile = entry.getValue();
                break;
            }
        }

        inputManager.onMouseMoved(validTile.getCenterPos().x, validTile.getCenterPos().y);

        int initialRangeLevel = inputManager.getTowerUnderCursor().getRangeLevel();
        int initialSpeedLevel = inputManager.getTowerUnderCursor().getFiringSpeedLevel();
        int initialDamageLevel = inputManager.getTowerUnderCursor().getDamageLevel();

        inputManager.onLeftClick(validTile.getCenterPos().x, validTile.getCenterPos().y);

        inputManager.onButtonPress(Keybindings.UPGRADE_RANGE_KEY.key());
        inputManager.onButtonPress(Keybindings.UPGRADE_SPEED_KEY.key());
        inputManager.onButtonPress(Keybindings.UPGRADE_DAMAGE_KEY.key());

        inputManager.tickActiveButtons();
        inputManager.onLeftClick(validTile.getCenterPos().x, validTile.getCenterPos().y);

        int actualRangeLevel = inputManager.getTowerUnderCursor().getRangeLevel();
        int actualSpeedLevel = inputManager.getTowerUnderCursor().getFiringSpeedLevel();
        int actualDamageLevel = inputManager.getTowerUnderCursor().getDamageLevel();

        assertNotEquals(initialRangeLevel, actualRangeLevel);
        assertNotEquals(initialSpeedLevel, actualSpeedLevel);
        assertNotEquals(initialDamageLevel, actualDamageLevel);
    }

    @Test
    void testActiveButtonsTick() {
        inputManager.tickActiveButtons();

        HashMap<PVector, Tile> board = game.getBoard().getLevelTiles();
        Tile validTile = null;
        for (Map.Entry<PVector, Tile> entry : board.entrySet()) {
            if (!entry.getValue().isOccupied() && entry.getValue().isPlaceable()) {
                validTile = entry.getValue();
                break;
            }
        }

        inputManager.onButtonPress(Keybindings.BUILD_TOWER_KEY.key());
        inputManager.onMouseMoved(validTile.getCenterPos().x, validTile.getCenterPos().y);
        
        int initialRangeLevel = inputManager.getTowerUnderCursor().getRangeLevel();
        int initialSpeedLevel = inputManager.getTowerUnderCursor().getFiringSpeedLevel();
        int initialDamageLevel = inputManager.getTowerUnderCursor().getDamageLevel();

        inputManager.onButtonPress(Keybindings.UPGRADE_RANGE_KEY.key());
        inputManager.onButtonPress(Keybindings.UPGRADE_SPEED_KEY.key());
        inputManager.onButtonPress(Keybindings.UPGRADE_DAMAGE_KEY.key());

        inputManager.tickActiveButtons();
        inputManager.onMouseMoved(validTile.getCenterPos().x, validTile.getCenterPos().y);

        int actualRangeLevel = inputManager.getTowerUnderCursor().getRangeLevel();
        int actualSpeedLevel = inputManager.getTowerUnderCursor().getFiringSpeedLevel();
        int actualDamageLevel = inputManager.getTowerUnderCursor().getDamageLevel();

        assertNotEquals(initialRangeLevel, actualRangeLevel);
        assertNotEquals(initialSpeedLevel, actualSpeedLevel);
        assertNotEquals(initialDamageLevel, actualDamageLevel);
    }

    @Test
    void testManaIncreasesOnButtonPress() {
        float initialMana = game.getManaPool().getCurrentMana();

        inputManager.onButtonPress(Keybindings.INCREASE_MANA_KEY.key());
        float actualMana = game.getManaPool().getCurrentMana();

        assertNotEquals(initialMana, actualMana);
        assertFalse(inputManager.getBoundButtons()
                                .get(Keybindings.INCREASE_MANA_KEY)
                                .isButtonActivated());
    }

    @Test
    void testCanRebindKey() {
        Button boundButton = inputManager.getBoundButtons().get(Keybindings.FF_KEY);
        inputManager.onRightClick(boundButton.getPos().x + 3, boundButton.getPos().y + 3);
        
        char expectedKey = 'z';
        inputManager.onButtonPress(expectedKey);
        char actualKey = Keybindings.FF_KEY.key();

        assertEquals(expectedKey, actualKey);
    }

    @Test
    void testGetNameOfBoundKey() {
        Keybindings expectedKey = Keybindings.FF_KEY;
        Keybindings actualKey = Keybindings.getNameOfBoundKey(expectedKey.key());

        assertEquals(expectedKey, actualKey);
    }

    @Test
    void testCanPressUnonButtonPress() {
        for (Map.Entry<Keybindings, Button> entry : inputManager.getBoundButtons().entrySet()) {
            if (entry.getKey() == Keybindings.INCREASE_MANA_KEY) {
                continue;
            }

            inputManager.onButtonPress(entry.getKey().key());
            assertTrue(entry.getValue().isButtonActivated());
        }

      

        for (Map.Entry<Keybindings, Button> entry : inputManager.getBoundButtons().entrySet()) {
            if (entry.getKey() == Keybindings.INCREASE_MANA_KEY) {
                continue;
            }

            inputManager.onButtonPress(entry.getKey().key());
            assertFalse(entry.getValue().isButtonActivated());
        }

    }

   

    @Test
    void testNoButtonsPressedWhenCursorOnBoard() {
        for (Map.Entry<Keybindings, Button> entry : inputManager.getBoundButtons().entrySet()) {
            Button b = entry.getValue();
            inputManager.activateButtonUnderCursor(App.LEVEL_WIDTH - 10, App.LEVEL_HEIGHT - 10);
            assertFalse(b.isButtonActivated());
        }

    } 
    
    @Test
    void testGetClosestButton() {
        for (Map.Entry<Keybindings, Button> entry : inputManager.getBoundButtons().entrySet()) {
            Button expectedButton = entry.getValue();
            Button actualButton = inputManager.getButtonUnderCursor(expectedButton.getPos().x, 
                                                                    expectedButton.getPos().y);
            assertEquals(expectedButton, actualButton);
        }
    }

    @Test
    void testMouseHoverOverButton() {
        for (Map.Entry<Keybindings, Button> entry : inputManager.getBoundButtons().entrySet()) {
            PVector initialColour = entry.getValue().getColour().copy();

            inputManager.onMouseMoved(entry.getValue().getPos().x + 2, 
                                    entry.getValue().getPos().y + 2);

            inputManager.onMouseMoved(entry.getValue().getPos().x + 2, 
                                    entry.getValue().getPos().y + 2);

            PVector actualColour = entry.getValue().getColour().copy();

            assertNotEquals(initialColour, actualColour);
        }  
    }

    @Test
    void testMouseMovedOnBoardWithNoTower() {
        Tower expectedTower = inputManager.getTowerUnderCursor();

        inputManager.onMouseMoved(50, 50);
        Tower actualTower = inputManager.getTowerUnderCursor();
        assertEquals(expectedTower, actualTower);
    }
    
    
}
