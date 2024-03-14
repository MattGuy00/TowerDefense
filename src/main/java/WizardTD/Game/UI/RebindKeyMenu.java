package WizardTD.Game.UI;

import WizardTD.App;
import processing.core.PGraphics;

/**
 * Represents the {@code RebindKeyMenu} class.
 * <p>
 * This is a menu for visually the player's ability to rebind keys.
 */
public class RebindKeyMenu extends UIElement {
    /**
     * The button's current binding.
     */
    private char boundKey = ' ';
    /**
     * Status of the menu's visibility.
     */
    private boolean showRebindMenu = false;

    /**
     * {@code RebindKeyMenu}'s constructor.
     * @param x x-position of the menu.
     * @param y y-position of the menu.
     * @param width The width of the menu.
     * @param height The height of the menu.
     */
    public RebindKeyMenu(float x, float y, float width, float height) {
        this.setPos(x, y);
        this.setWidth(width);
        this.setHeight(height);
    }

    /**
     * Sets the status of {@code showRebindMenu} to {@code true}.
     * @param boundKey The buttons current key.
     */
    public void showRebindMenu(char boundKey) { 
        this.showRebindMenu = true; 
        this.boundKey = boundKey;
    }

    /**
     * Sets the status of {@code showRebindMenu} to {@code false}.
     */
    public void hideRebindMenu() { 
        this.showRebindMenu = false; 
    }

    @Override
    public void drawToLayer(PGraphics layer) {
        if (!this.showRebindMenu) {
            return;
        }

        layer.strokeWeight(3);
        layer.stroke(131,86,56);
        layer.fill(171,120,78);
        layer.rect(this.getPos().x, this.getPos().y, this.getWidth(), this.getHeight());
        layer.fill(200);

        layer.textSize(12);
        layer.text("This button is bound to: " + boundKey + "\n\nRight click again to rebind this key",
                   this.getPos().x + 20, this.getPos().y + 20, this.getWidth() - 10, this.getHeight());

        layer.strokeWeight(App.DEFAULT_STROKE_WEIGHT);
        layer.stroke(App.DEFAULT_STROKE_COLOUR);
        layer.fill(App.DEFAULT_FILL_COLOUR);
        layer.textSize(App.DEFAULT_TEXT_SIZE);
        
    }
}
