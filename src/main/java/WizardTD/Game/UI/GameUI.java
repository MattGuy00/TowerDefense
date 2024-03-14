package WizardTD.Game.UI;

import WizardTD.App;
import WizardTD.Game.Game;
import WizardTD.Game.Renderable;
import WizardTD.Game.Player.InputManager;
import WizardTD.Game.UI.Buttons.*;
import processing.core.PGraphics;
import processing.core.PVector;

/**
 * Represents the game's UI.
 */
public class GameUI implements Renderable {
    private final Game game;

    /**
     * The width of the bar above the board.
     */
    private final float topBarWidth = App.WIDTH;
    /**
     * The height of the bar above the board.
     */
    private final float topBarHeight = App.TOPBAR;
    /**
     * The position of the top bar's top-left corner
     */
    private final PVector topBarOrigin = new PVector(0, 0);

    /**
     * The width of the bar to the side of the board.
     */
    private final float sideBarWidth = App.SIDEBAR;
    /**
     * The Height of the bar to the side of the board.
     */
    private final float sideBarHeight = App.LEVEL_HEIGHT;
    /**
     * The position of the side bar's top-left corner
     */
    private final PVector sideBarOrigin = new PVector(App.LEVEL_WIDTH, topBarHeight);

    /**
     * The default colour of both top and side bars
     */
    private final PVector UIBarsColour = new PVector(108,147,92);
    /**
     * The outline thickness of the top and side bar.
     */
    private float borderThickness = 0;

    /**
     * The visual representation of the mana pool.
     */
    private final ManaBar manaBar;
    /**
     * Displays info from the wave manager.
     */
    private final WaveTimer waveTimer;

    /**
     * The button used for fast-forwarding the game.
     */
    private final Button ffButton;
    /**
     * The button used for pausing the game.
     */
    private final Button pauseButton;
    /**
     * The button used for building towers.
     */
    private final Button buildTowerButton;
    /**
     * The button used for upgrading the range of towers.
     */
    private final Button upgradeRangeButton;
    /**
     * The button used for upgrading the firing speed of towers.
     */
    private final Button upgradeSpeedButton;
    /**
     * The button used for upgrading the damage of towers.
     */
    private final Button upgradeDamageButton;
    /**
     * The button used for increasing the mana pool's mana cap.
     */
    private final Button increaseManaPoolButton;

    /**
     * The x position of all buttons top-left corner.
     */
    private final float buttonX = this.sideBarOrigin.x + 5;
    /**
     * The base y position of a button's top-left corner.
     */
    private final float buttonY = this.topBarHeight + 5;
    /**
     * How much each button is offset in the y direction.
     */
    private final float buttonYOffset = 50;
    /**
     * An array used to hold buttons for drawing.
     */
    private final Button[] buttons = new Button[7];
    /**
     * A UI element that displays the cost of upgrading towers.
     */
    private final UpgradeCostChart towerCostTooltip;

    /**
     * A menu that displays key rebinding.
     */
    private final RebindKeyMenu rebindKeyMenu;

    /**
     * The constructor for the {@code GameUI} class.
     * @param game The game that the UI is used for.
     * @param towerManager for the tower upgrade cost.
     * @param inputManager To get input for towerCostTooltip.
     * @param manaPool For the mana spell button.
     */
    public GameUI(Game game, InputManager inputManager) {
        this.game = game;

        this.manaBar = new ManaBar(380, 5, 350, 25);
        this.waveTimer = new WaveTimer(5, 10, 250, 100);

        this.ffButton = new FFButton( "FF", this.buttonX, this.buttonY, game);
        buttons[0] = this.ffButton;

        this.pauseButton = new PauseButton( "P", this.buttonX, 
                                           this.buttonY + this.buttonYOffset + 10, game);
        buttons[1] = this.pauseButton;

        this.buildTowerButton = new TowerButton("T", this.buttonX, 
                                           this.buttonY + (this.buttonYOffset + 10) * 2, game);
        buttons[2] = this.buildTowerButton;

        this.upgradeRangeButton = new URangeButton("U1", this.buttonX, 
                                                   this.buttonY + (this.buttonYOffset + 10) * 3, game);
        buttons[3] = this.upgradeRangeButton;

        this.upgradeSpeedButton = new USpeedButton("U2", this.buttonX, 
                                                   this.buttonY + (this.buttonYOffset + 10) * 4, game);
        buttons[4] = this.upgradeSpeedButton;

        this.upgradeDamageButton = new UDamageButton("U3", this.buttonX, 
                                                     this.buttonY + (this.buttonYOffset + 10) * 5, game);
        buttons[5] = this.upgradeDamageButton;

        this.increaseManaPoolButton = new ManaSpellButton("M", this.buttonX, 
                                                          this.buttonY + (this.buttonYOffset + 10) * 6, game);
        buttons[6] = this.increaseManaPoolButton;

        this.towerCostTooltip = new UpgradeCostChart(inputManager, 655, 500);
        this.rebindKeyMenu = new RebindKeyMenu(256, 296, 192, 96);
    }

    /**
     * Gets the UI's mana bar.
     * @return The mana bar.
     */
    public ManaBar getManaBar() { return this.manaBar; }
    /**
     * Gets the UI's wave timer.
     * @return The wave timer.
     */
    public WaveTimer getWaveTimer() { return this.waveTimer; }

    public void drawToLayer(PGraphics layer) {
        drawUIBars(layer);

        for (Button b : buttons) {
            b.drawToLayer(layer);
        }
        
        this.manaBar.drawToLayer(layer);
        this.waveTimer.drawToLayer(layer);
        this.towerCostTooltip.drawToLayer(layer);
        this.rebindKeyMenu.drawToLayer(layer);

        if (this.game.isGameOver()) {
            this.drawGameOverScreen(layer);
        }
    }

    public RebindKeyMenu getRebindKeyMenu() { return this.rebindKeyMenu; }

    /**
     * Draws the top and side bar to the layer.
     * @param layer the layer that the top and side bar are drawn to.
     */
    private void drawUIBars(PGraphics layer) {
        layer.fill(this.UIBarsColour.x, this.UIBarsColour.y, this.UIBarsColour.z);
        layer.strokeWeight(borderThickness);
        layer.rect(this.topBarOrigin.x, this.topBarOrigin.y, this.topBarWidth, this.topBarHeight);
        layer.rect(this.sideBarOrigin.x, this.sideBarOrigin.y, this.sideBarWidth, this.sideBarHeight);

        layer.strokeWeight(App.DEFAULT_STROKE_WEIGHT);
    }

    /**
     * Draws a semi-transparent game won screen to the layer.
     * @param layer the layer that the screen is drawn to.
     */
    private void drawGameOverScreen(PGraphics layer) {
        layer.strokeWeight(0);
        layer.fill(20, 200);
        layer.rect(0, 40, 640, 640);

        layer.textSize(20);
        String text = "";
        if (game.isGameWon()) {
            text = "YOU WIN";
            layer.fill(0, 255, 0);
        } else {
            text = "YOU LOSE";
            layer.fill(255, 0, 0);
        }

        float offset = layer.textDescent() + layer.textAscent();
        layer.text(text, 320 - offset, 288 + offset, 200, 200);
        layer.text("Press 'r' to restart!", 276 - offset, 320 + offset, 200, 200);

        layer.textSize(App.DEFAULT_TEXT_SIZE);
        layer.strokeWeight(App.DEFAULT_STROKE_WEIGHT);
    }
}
