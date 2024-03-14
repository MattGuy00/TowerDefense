package WizardTD;

import java.util.Map;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import WizardTD.Game.Game;
import WizardTD.Game.Board.Grass;
import WizardTD.Game.Board.Tile;
import WizardTD.Game.Player.InputManager;
import WizardTD.Game.Player.InputManager.Keybindings;
import WizardTD.Game.UI.UpgradeCostChart;
import processing.core.PApplet;
import processing.core.PVector;
import processing.data.JSONObject;

public class UpgradeCostChartTest {
    private static JSONObject config;
    private static App app;
    private static Game game;
    private static InputManager inputManager;
    private static UpgradeCostChart upgradeCostChart;

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
    void setupChart() {
        game = new Game(config);
        inputManager = game.getInputManager();
        upgradeCostChart = new UpgradeCostChart(inputManager, 0, 0);
    }

    @Test
    // Basic test to check that the upgrade chart is able to draw without crashing.
    void testUpgradeChartDrawsWithoutCrash() {
        upgradeCostChart.drawToLayer(app.g);

        inputManager.onButtonPress(Keybindings.BUILD_TOWER_KEY.key());
        
        for (Map.Entry<PVector, Tile> entry : game.getBoard().getLevelTiles().entrySet()) {
            if (entry.getValue() instanceof Grass) {
                inputManager.onMouseMoved(entry.getKey().x, entry.getKey().y);
                break;
            }
        }

        inputManager.onButtonPress(Keybindings.UPGRADE_RANGE_KEY.key());
        inputManager.onButtonPress(Keybindings.UPGRADE_SPEED_KEY.key());
        inputManager.onButtonPress(Keybindings.UPGRADE_DAMAGE_KEY.key());

        inputManager.tickActiveButtons();
        upgradeCostChart.drawToLayer(app.g);
    }
    
}
