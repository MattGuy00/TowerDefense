package WizardTD.Game.UI;

import WizardTD.App;
import processing.core.PGraphics;

/**
 * Represents the {@code Tooltip} class.
 * <p>
 * This class is a child class of {@code UIElement},
 * and is used for displaying a tooltip to the screen.
 * @see UIElement
 */
public class Tooltip extends UIElement {
    /**
     * Tracks the number of lines of 
     * text that the tooltip has.
     */
    private int numOfLines = 1;

    /**
     * The colour of the tooltip's background.
     */
    private float backgroundColour = 255;

    /**
     * The width of the tooltip's background.
     */
    private float backgroundWidth;
    /**
     * The height of the tooltip's background.
     */
    private float backgroundHeight;

    /**
     * The status of the tooltips visibility.
     */
    private boolean isVisible = false;

    /**
     * {@code Tooltip}'s constructor.
     * <p>
     * Constructs a tooltip with black text 
     * and a white background.
     * @param text The text that this tooltip shows.
     * @param x x-position of the tooltip.
     * @param y y-position of the tooltip.
     */
    public Tooltip(String text, float x, float y) {
        this.setPos(x, y);
        this.setText(text);
        this.setTextSize(App.DEFAULT_TEXT_SIZE);
        this.setBorderThickness(App.DEFAULT_STROKE_WEIGHT);   
    }

    public boolean isVisible() { return this.isVisible; }

    @Override
    public void setText(String text) { 
        super.setText(text);
        this.countNumOfLines();
    }

    /**
     * Counts the number of lines of the tooltip's text
     */
    private void countNumOfLines() {
        for (String s : this.getText().split("")) {
            if (s.equals("\n")) {
                ++numOfLines;
            }
        }
    }

    /**
     * Sets the visibility of the tooltip to {@code true}.
     */
    public void show() { this.isVisible = true; }
    /**
     * Sets the visibility of the tooltip to {@code false}.
     */
    public void hide() { this.isVisible = false; }

    public void drawToLayer(PGraphics layer) {
        if (!this.isVisible) {
            return;
        }
        
        drawBackground(layer);
        drawText(layer);

        layer.strokeWeight(App.DEFAULT_STROKE_WEIGHT);
        layer.textSize(App.DEFAULT_TEXT_SIZE);
    }

    /**
     * Draws the tooltip's text to the given layer.
     * @param layer The layer that the text is drawn to.
     */
    private void drawText(PGraphics layer) {
        layer.textSize(this.getTextSize());
        layer.fill(App.DEFAULT_FILL_COLOUR);
        layer.text(this.getText(), this.getPos().x, this.getPos().y, 
                   layer.textWidth(this.getText()) + layer.textSize, this.backgroundHeight);
    }

    /**
     * Draws and dynamically resizes the tooltip's background
     *  to the given layer.
     * @param layer The layer that the background is drawn to.
     */
    private void drawBackground(PGraphics layer) {
        this.backgroundWidth = layer.textWidth(this.getText()) + layer.strokeWeight + 2;
        this.backgroundHeight = (layer.textAscent() + 
                                 layer.textDescent() + this.numOfLines) * this.numOfLines;
        
        layer.fill(this.backgroundColour);
        layer.strokeWeight(this.getBorderThickness());
        layer.rect(this.getPos().x - layer.strokeWeight, this.getPos().y,
                   this.backgroundWidth, this.backgroundHeight);
    }   
}
