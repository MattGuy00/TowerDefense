package WizardTD;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import WizardTD.Game.Game;
import WizardTD.Game.Entities.Towers.Tower;
import WizardTD.Game.Player.InputManager.Keybindings;
import WizardTD.Game.UI.Tooltip;
import WizardTD.Game.UI.Buttons.Button;
import WizardTD.Game.UI.Buttons.FFButton;
import WizardTD.Game.UI.Buttons.PauseButton;
import WizardTD.Game.UI.Buttons.TowerButton;
import WizardTD.Game.UI.Buttons.UDamageButton;
import WizardTD.Game.UI.Buttons.URangeButton;
import WizardTD.Game.UI.Buttons.USpeedButton;
import processing.core.PApplet;
import processing.core.PVector;
import processing.data.JSONObject;

public class ButtonTest {
    private static Button button;
    private static FFButton ffButton;
    private static PauseButton pauseButton;
    private static TowerButton towerButton;
    private static URangeButton uRangeButton;
    private static USpeedButton uSpeedButton;
    private static UDamageButton uDamageButton;

    private static JSONObject config;
    private static Game game;

    private static App app;

    @BeforeAll
    static void setup() {
        app = new App();
        app.loop();
        PApplet.runSketch(new String[] { "App" }, app);
        app.setup();
        app.delay(1500);
        config = app.loadJSONObject(app.configPath);
    }

    @BeforeEach
    void setupButton() {
        game = new Game(config);

        button = new Button("", 0, 0);
        ffButton = new FFButton("test", 0, 0, game);
        pauseButton = new PauseButton("test", 0, 0, game);
        towerButton = new TowerButton("test", 0, 0, game);
        uRangeButton = new URangeButton("test", 0, 0, game);
        uSpeedButton = new USpeedButton("test", 0, 0, game);
        uDamageButton = new UDamageButton("test", 0, 0, game);
    }

    @Test
    // Makes sure button's pos is set on instantiation.
    void testPosNotNull() {
        assertNotNull(button.getPos());
    }

    @Test
    // Tests if pos is changed.
    void testPosChanges() {
        PVector oldPos = button.getPos().copy();
        button.setPos(1, 1);

        assertNotEquals(oldPos, button.getPos());
    }

    @Test
    // Tests that the correct button width is returned.
    void testGetCorrectWidth() {
        float testWidth = 25;
        button.setWidth(testWidth);
        
        assertEquals(testWidth, button.getWidth(), "Button has width: " + button.getWidth() +
                     " when it should be: " + testWidth);
    }

    @Test
    // Tests that the correct button height is returned.
    void testGetCorrectHeight() {
        float testHeight = 25;
        button.setHeight(testHeight);
        
        assertEquals(testHeight, button.getHeight(), "Button has width: " + button.getHeight() +
                     " when it should be: " + testHeight);
    }

    @Test
    // Tests that the button is not activated on instantiation.
    void testNotActivatedByDefault() {
        assertFalse(button.isButtonActivated());
    }

    @Test
    // Tests if the tooltip and its text is properly set.
    void testAbleToSetTooltip() {
        String tooltipText = "text";
        button.setTooltip(tooltipText);
        assertTrue(button.getTooltip() instanceof Tooltip);
        assertTrue(tooltipText.equals(button.getTooltip().getText()));
    }

    @Test
    // Test if the button's base and activated colour changes properly.
    void testAbleToChangeColours() {
        PVector baseColour = new PVector(30, 25, 10);
        button.setColour(baseColour.x, baseColour.y, baseColour.z);
        assertEquals(baseColour, button.getColour(),
                     "The button's base colour should be " + baseColour);

        PVector activatedColour = new PVector(50, 37, 50);
        button.setActivatedColour(activatedColour.x, activatedColour.y, activatedColour.z);
        assertEquals(activatedColour, button.getActivatedColour(),
                     "The button's activated colour should be " + activatedColour);
    }

    @Test
    // Tests if the button's base colour resets to its default colour.
    void testAbleToResetBaseColour() {
        button.setColour(100, 100, 100);
        
        PVector baseColour = button.getColour();
        button.resetColour();
        assertNotEquals(baseColour, button.getColour(), 
                        "Button's colour should have reset.");
    }

    @Test
    // Tests if the button activation status changes to true on activation.
    void testButtonActivates() {
        button.activateButton();
        assertTrue(button.isButtonActivated());
    }

    @Test
    // Tests if the button activation status changes to false on deactivation.
    void testButtonDeactivates() {
        button.deactivateButton();
        assertFalse(button.isButtonActivated());
    }

    @Test
    // Tests that the button's text is set correctly.
    void testAbleToSetText() {
        String expectedText = "test text";
        button.setText(expectedText);

        assertTrue(button.getText().equals(expectedText));
    }

    @Test
    // Tests that the buttons text description is set correctly.
    void testAbleToSetTextDescription() {
        String expectedDescription = "test description";
        button.setTextDescription(expectedDescription);

        assertTrue(button.getTextDescription().equals(expectedDescription));
    }

    @Test
    // Tests that the button's tooltip visibilty is correctly set.
    void testAbleTosetToolTipVisibility() {
        button.setTooltip("test");

        button.showTooltip();
        assertTrue(button.getTooltip().isVisible() == true);

        button.hideTooltip();
        assertTrue(button.getTooltip().isVisible() == false);
    }

    @Test
    // Basic test to check that the button draws without crashing.
    void testAbleToDrawButtonWithoutCrash() {
        button.drawToLayer(app.g);
    }

    @Test
    // Tests that the button binds to FF_KEY in inputManager.
    void testButtonBindsToFFKey() {
        Button actualButton = game.getInputManager()
                                    .getBoundButtons()
                                    .get(Keybindings.FF_KEY);

        assertEquals(ffButton, actualButton);
    }

    @Test
    // Tests that the button increases the game's speed on activation.
    void testButtonIncreasesGameSpeedOnActivate() {
        float initialGameSpeed = game.getGameSpeed();

        ffButton.activateButton();

        float actualGameSpeed = game.getGameSpeed();
        assertTrue(actualGameSpeed > initialGameSpeed);
    }

    @Test
    // Tests that the button sets the game's speed back to its original state.
    void testButtonDecreasesGameSpeedOnDeactivate() {
        float expectedGameSpeed = game.getGameSpeed();

        ffButton.activateButton();
        ffButton.deactivateButton();

        float actualGameSpeed = game.getGameSpeed();
        assertEquals(expectedGameSpeed, actualGameSpeed);
    }

    @Test
    // Tests that the button binds to PAUSE_KEY in inputManager.
    void testButtonBindsToPauseKey() {
        Button actualButton = game.getInputManager()
                                    .getBoundButtons()
                                    .get(Keybindings.PAUSE_KEY);

        assertEquals(pauseButton, actualButton);
    }

    @Test
    // Tests that the button pauses the game on activation.
    void testButtonPausesGameOnActivate() {
        pauseButton.activateButton();

        boolean pauseState = game.isGamePaused();
        assertTrue(pauseState);
    }

    @Test
    // Tests that the button unpauses the game on deactivation.
    void testButtonUnpausesGameOnDeactivate() {
        boolean expectedPauseState = game.isGamePaused();

        pauseButton.activateButton();
        pauseButton.deactivateButton();

        boolean actualPauseState = game.isGamePaused();
        assertEquals(expectedPauseState, actualPauseState);
    }

    @Test
    // Tests that the ffbutton binds to BUILD_TOWER_KEY in inputManager.
    void testButtonBindsToBuildTowerKey() {
        Button actualButton = game.getInputManager()
                                    .getBoundButtons()
                                    .get(Keybindings.BUILD_TOWER_KEY);

        assertEquals(towerButton, actualButton);
    }

    @Test
    // Test that the button is initialised with a tooltip.
    void testButtonHasTooltip() {
        assertNotNull(towerButton.getTooltip());
    }

    @Test
    // Tests that the button initialises an unbuilt tower on activation.
    void testButtonInitUnbuiltTowerOnActivate() {
        towerButton.activateButton();

        Tower t = game.getTowerManager().getUnbuiltTower();
        assertNotNull(t);
    }

    @Test
    // Tests that the button deactivates the unbuilt tower on deactivation.
    void testButtonDeactivatesUnbuiltTowerOnDeactivate() {

        towerButton.activateButton();
        towerButton.deactivateButton();

        Tower t = game.getTowerManager().getUnbuiltTower();
        assertNull(t);
    } 

    @Test
    // Tests that the binds to UPGRADE_RANGE_KEY in inputManager.
    void testButtonBindsToUpgradeRangeKey() {
        Button actualButton = game.getInputManager()
                                    .getBoundButtons()
                                    .get(Keybindings.UPGRADE_RANGE_KEY);

        assertEquals(uRangeButton, actualButton);
    }

    @Test
    // Tests that the binds to UPGRADE_SPEED_KEY in inputManager.
    void testButtonBindsToUpgradeSpeedKey() {
        Button actualButton = game.getInputManager()
                                    .getBoundButtons()
                                    .get(Keybindings.UPGRADE_SPEED_KEY);

        assertEquals(uSpeedButton, actualButton);
    }

    @Test
    // Tests that the button binds to UPGRADE_SPEED_KEY in inputManager.
    void testButtonBindsToUpgradeDamageKey() {
        Button actualButton = game.getInputManager()
                                    .getBoundButtons()
                                    .get(Keybindings.UPGRADE_DAMAGE_KEY);

        assertEquals(uDamageButton, actualButton);
    }
}
