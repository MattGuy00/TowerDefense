package WizardTD.Game.UI;

import WizardTD.App;
import WizardTD.Game.Entities.Towers.Tower;
import WizardTD.Game.Player.InputManager;
import WizardTD.Game.Player.InputManager.Keybindings;
import WizardTD.Game.UI.Buttons.Button;
import processing.core.PGraphics;

/**
 * Represents the UpgradeCostChart class.
 * <p>
 * This class is a child class of UIElement.
 * It is used to show the cost of upgrading the tower
 * to the player.
 */
public class UpgradeCostChart extends UIElement {
    /**
     * For getting the tower that is being hovered over.
     */
    private final InputManager inputManager;
    /**
     * For checking if the upgrade range button is active.
     */
    private final Button upgradeRangeButton;
    /**
     * For checking if the upgrade speed button is active.
     */
    private final Button upgradeSpeedButton;
    /**
     * For checking if the upgrade damage button is active.
     */
    private final Button upgradeDamageButton;

    /**
     * The UpgradeCostChart constructor.
     * <p>
     * Constructs a chart that dynamically resizes
     * based on the currently selected upgrades.
     * @param inputManager For getting the tower thats being hovered over.
     * @param x x-position of the chart.
     * @param y x-position of the chart.
     */
    public UpgradeCostChart(InputManager inputManager, float x, float y) {
        this.inputManager = inputManager;
        this.upgradeRangeButton = this.inputManager.getBoundButtons()
                                                   .get(Keybindings.UPGRADE_RANGE_KEY);

        this.upgradeSpeedButton = this.inputManager.getBoundButtons()
                                                   .get(Keybindings.UPGRADE_SPEED_KEY);
                                                   
        this.upgradeDamageButton = this.inputManager.getBoundButtons()
                                                   .get(Keybindings.UPGRADE_DAMAGE_KEY);
        this.setPos(x, y);
    }

    public void drawToLayer(PGraphics layer) {
        if (!this.upgradeRangeButton.isButtonActivated() &&
            !this.upgradeSpeedButton.isButtonActivated() &&
            !this.upgradeDamageButton.isButtonActivated()) {
                return;
            }

        if (this.inputManager.getTowerUnderCursor() == null) return;
        
        float prevTextSize = layer.textSize;
        layer.textSize(12);

        
        this.drawUpgradeCost(layer, this.inputManager.getTowerUnderCursor());

        layer.textSize(prevTextSize);
    }

    /**
     * Draws the cost of upgrading the tower to the layer.
     * @param layer The layer that the chart is drawn to
     * @param tower The tower that is being hovered over.
     */
    private void drawUpgradeCost(PGraphics layer, Tower tower) {
        String text = "Upgrade cost\n";
        float cost = 0;
        int linesOfText = 1;

        if (this.upgradeRangeButton.isButtonActivated()) {
            text += "range: " + tower.getRangeUpgradeCost() + '\n';
            cost += tower.getRangeUpgradeCost();
            ++linesOfText;
        }

        if (this.upgradeSpeedButton.isButtonActivated()) {
            text += "speed: " + tower.getFiringSpeedUpgradeCost() + '\n';
            cost += tower.getFiringSpeedUpgradeCost();
            ++linesOfText;
        }

        if (this.upgradeDamageButton.isButtonActivated()) {
            text += "damage: " + tower.getDamageUpgradeCost() + '\n';
            cost += tower.getDamageUpgradeCost();
            ++linesOfText;
        }
        

        text += "total: " + cost;
        ++linesOfText;
        
        this.drawChartBackground(layer, text, linesOfText);

        layer.fill(0);
        layer.text(text, this.getPos().x, this.getPos().y + layer.textAscent() + layer.textDescent(),
                100, 150);

        layer.fill(App.DEFAULT_FILL_COLOUR);
    }

    /**
     * Draws the chart's background to the layer.
     * @param layer The layer that the background is drawn to.
     * @param text For dynamically resizing the chart.
     * @param linesOfText For dynamically resizing the chart.
     */
    private void drawChartBackground(PGraphics layer, String text, int linesOfText) {
        float rectX = this.getPos().x - 2;
        float rectY = this.getPos().y + layer.textAscent() + layer.textDescent();
        float rectWidth = layer.textWidth(text) + layer.strokeWeight;
        // offset height by number of newlines in text
        float rectHeight = (layer.textAscent() + layer.textDescent() + linesOfText - 1) * 
                            linesOfText;

        layer.fill(255);
        layer.rect(rectX, rectY, rectWidth, rectHeight);

        float topLineY = rectY + layer.textAscent() + layer.textDescent();
        layer.line(rectX, topLineY, rectX + rectWidth, topLineY);

        float bottomLineY = this.getPos().y + rectHeight - rectHeight / (linesOfText + 11);
        layer.line(rectX, bottomLineY, rectX + rectWidth, bottomLineY);

    }
    
}
