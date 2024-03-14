package WizardTD.Game.UI.Buttons;

import WizardTD.App;
import WizardTD.Game.UI.Tooltip;
import WizardTD.Game.UI.UIElement;
import processing.core.PGraphics;
import processing.core.PVector;

/**
 * Represents the {@code Button} class.
 * <p>
 * Buttons are a renderable object that can be interacted with.
 */
public class Button extends UIElement {
    /**
     * The text that is displayed to the right of the button.
     */
    private String textDescription = " ";
    /**
     * Tracks the button's activation status.
     */
    private boolean isButtonActivated = false;
    /**
     * The colour of the button when it is activated.
     */
    private PVector activatedColour = new PVector(255, 195, 0);
    /**
     * The default colour of the button.
     */
    private PVector colour = new PVector(208, 208, 208);
    /**
     * The current colour of the button.
     */
    private PVector currentColour = this.colour.copy();

    /**
     * An optional tooltip for the button.
     */
    private Tooltip tooltip = null;  

    /**
     * {@code Button}'s constructor.
     * @param text The text to display on top of the button.
     * @param x The x position of the button's top-left corner.
     * @param y the y position of the button's top-left corner.
     */
    public Button(String text, float x, float y) {
        this.setText(text);
        this.setPos(x, y);

        this.setWidth(45);
        this.setHeight(45);
    }

    /**
     * Gets the activation status of the button.
     * @return The activation status of the button.
     */
    public boolean isButtonActivated() { return this.isButtonActivated; }
    /**
     * Sets the activation status of the button.
     * @param activated The new activation status.
     */
    public void setButtonActivated(boolean activated) { this.isButtonActivated = activated; }

    /**
     * Gets the button's text description.
     * @return The button's text description.
     */
    public String getTextDescription() { return this.textDescription; }
    /**
     * Sets the text that is displayed to the right of the button.
     * @param text the text that is to be displayed.
     */
    public void setTextDescription(String text) { this.textDescription = text; }

    /**
     * Creates a new tooltip for the button.
     * @param tooltipText The text that the tooltip displays.
     */
    public void setTooltip(String tooltipText) { 
        this.tooltip = new Tooltip(tooltipText, this.getPos().x - 75, this.getPos().y); 
    }

    /**
     * Gets the button's tooltip.
     * @return The buttons tooltip, or null if a tooltip has not been set.
     */
    public Tooltip getTooltip() { return this.tooltip; }

    /**
     * Gets the current colour of the button.
     * @return The current colour of the button.
     */
    public PVector getColour() { return this.currentColour; }

    /**
     * Sets the current colour of the button.
     * @param r The red value from (0-255).
     * @param g The green value from (0-255).
     * @param b The blue value from (0-255).
     */
    public void setColour(float r, float g, float b) {
        this.currentColour.x = r;
        this.currentColour.y = g;
        this.currentColour.z = b;
    }

    /**
     * Resets the colour of the button to its default value.
     */
    public void resetColour() { this.currentColour = this.colour.copy(); }

    /**
     * Gets the colour of the button when it is activated.
     * @return The activated colour.
     */
    public PVector getActivatedColour() { return this.activatedColour; }
    /**
     * Sets the the colour that is shown when the button is activated.
     * @param r The red value from (0-255).
     * @param g The green value from (0-255).
     * @param b The blue value from (0-255).
     */
    public void setActivatedColour(float r, float g, float b) {
        this.activatedColour.x = r;
        this.activatedColour.y = g;
        this.activatedColour.z = b;
    }

    /**
     * Activates the button.
     */
    public void activateButton() { isButtonActivated = true; }

    /**
     * Deactivates the button.
     */
    public void deactivateButton() { isButtonActivated = false; }

    /**
     * Shows the button's tooltip.
     */
    public void showTooltip() { 
        if (this.tooltip == null) {
            return;
        }

        this.tooltip.show();
    }
    /**
     * Hides the button's tooltip.
     */
    public void hideTooltip() {
        if (this.tooltip == null) { 
            return;
        }

        this.tooltip.hide();
    }

    public void drawToLayer(PGraphics layer) {
        layer.strokeWeight(this.getBorderThickness());
        layer.stroke(58,97,42);

        if (isButtonActivated) {
            this.drawButton(layer, this.activatedColour.x, this.activatedColour.y, this.activatedColour.z);
        } else {
            this.drawButton(layer, this.currentColour.x, this.currentColour.y, this.currentColour.z);
        }

        drawButtonText(layer);
        drawTextDescription(layer);

        layer.strokeWeight(App.DEFAULT_STROKE_WEIGHT);
        layer.stroke(App.DEFAULT_STROKE_COLOUR);

        if (this.tooltip == null) {
            return;
        }

        this.tooltip.drawToLayer(layer);
    }

    /**
     * Draws the button to the layer.
     * @param layer The layer that the button is drawn to.
     * @param r red colour of the button.
     * @param g green colour of the button.
     * @param b blue colour of the button.
     */
    private void drawButton(PGraphics layer, float r, float g, float b) {
        layer.fill(r, g, b);
        layer.rect(this.getPos().x, this.getPos().y, this.getWidth(), this.getHeight());

        layer.strokeWeight(0);
        layer.fill(r / 1.3f, g / 1.3f, b / 1.3f);
        layer.rect(this.getPos().x + this.getBorderThickness(), this.getPos().y + this.getHeight() - 8,
                   this.getWidth() - this.getBorderThickness(), 7);

        layer.strokeWeight(App.DEFAULT_STROKE_WEIGHT);
        layer.fill(App.DEFAULT_FILL_COLOUR);
    }

    /**
     * Draws the text on top of the button to the specified layer.
     * @param layer The layer that the text is drawn to.
     */
    private void drawButtonText(PGraphics layer) {
        layer.fill(50);
        layer.textSize(20);
        layer.text(this.getText(), this.getPos().x + 10, this.getPos().y + 15, this.getWidth(), this.getHeight());
    }

    /**
     * Draws the text to the right of the button to the specified layer.
     * @param layer The layer that the description is drawn to.
     */
    private void drawTextDescription(PGraphics layer) {
        layer.textSize(12);
        layer.text(textDescription, this.getPos().x + this.getWidth() + 4, this.getPos().y,
                   layer.textWidth(this.textDescription), this.getHeight());
    }
}
