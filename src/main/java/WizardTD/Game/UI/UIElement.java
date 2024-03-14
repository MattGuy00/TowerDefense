package WizardTD.Game.UI;

import WizardTD.Game.Renderable;
import processing.core.PVector;

/**
 * An abstract base class that represents all UI elements.
 * <p>
 * A UI element is an object that is rendered to the screen as 
 * part of the game's UI.
 */
public abstract class UIElement implements Renderable {
    /**
     * The position of the bar's top-left corner.
     */
    private PVector pos = new PVector();
    /**
     * The width of the bar.
     */
    private float width;
    /**
     * The height of the bar.
     */
    private float height;

    /**
     * The thickness of the bar's outline.
     */
    private float borderThickness = 2;

    /**
     * The text that the UI element optionally has.
     */
    private String text = "";
    /**
     * The size of the text displayed on the bar.
     */
    private float textSize = 20;
    
    public PVector getPos() { return this.pos; }
    public void setPos(float x, float y) { this.pos.set(x, y); }
    public void setPos(PVector pos) { this.pos = pos; }

    /**
     * Gets the text that the UI element has.
     * @return Empty string by default, or the text
     * that the UI element has.
     */
    public String getText() { return this.text; }
    /**
     * Sets the text of the UI element.
     * @param text The new text.
     */
    public void setText(String text) {  this.text = text; }

    /**
     * Gets the width of the UI element.
     * @return The UI element's width.
     */
    public float getWidth() { return this.width; }
    /**
     * Sets the width of the UI element.
     * @param width The new width of the element.
     */
    public void setWidth(float width) { this.width = width; }

    /**
     * Gets the height of the UI element.
     * @return The element's height.
     */
    public float getHeight() { return this.height; }
    /**
     * Sets the height of the UI element.
     * @param height The element's new height.
     */
    public void setHeight(float height) { this.height = height; }

    /**
     * Gets the thickness of the outline around the UI element.
     * The thickness is 2.0 by default.
     * @return The elements outline thickness.
     */
    public float getBorderThickness() { return this.borderThickness; }
    /**
     * Sets the thickness of the outline around the UI element.
     * @param thickness the element's new outline thickness.
     */
    public void setBorderThickness(float thickness) { this.borderThickness = thickness; }

    /**
     * Gets the size of the text in the UI element.
     * @return The size of the text in the UI element.
     */
    public float getTextSize() { return this.textSize; }
    /**
     * Sets the size of the text in the UI element.
     * @param size The new size of the text.
     */
    public void setTextSize(float size) { this.textSize = size; }
}
