package WizardTD.Game.UI;

import WizardTD.App;
import processing.core.PGraphics;
import processing.core.PVector;

/**
 * Represents the ManaBar UI element.
 * <p>
 * ManaBar's are designed to be used in conjunction with a {@code ManaPool} class.
 */
public final class ManaBar extends UIElement {
    /**
     * The colour of the bar's text.
     */
    private PVector textColour = new PVector(  187, 192, 194 );
    /**
     * The colour of the background bar.
     */
    private PVector backBarColour = new PVector( 49, 52, 53 );
    /**
     * The colour of the mana bar.
     */
    private PVector manaBarColour = new PVector(  14, 127, 173 );

    /**
     * The current mana cap.
     */
    private float maxMana;
    /**
     * The amount of mana remaining.
     */
    private float currentMana;

    /**
     * ManaBar's constructor. Constructs a mana bar at the given position,
     * with the given dimensions.
     * @param x x-position of the bar's top-left corner.
     * @param y y-position of the bar's top-left corner.
     * @param width The width of the mana bar.
     * @param height The height of the mana bar.
     */
    public ManaBar(float x, float y, float width, float height) {
        this.setPos(x, y);
        this.setWidth(width);
        this.setHeight(height);
    }

    /**
     * Sets the current mana displayed on the bar equal to the given parameter.
     * <p>
     * If the parameter is greater than the mana cap,
     * the current mana is set to the mana cap.
     * <p>
     * If parameter is a negative value, the current mana
     * is set to 0.
     * @param mana 
     */
    public void setCurrentMana(float mana) { 
        if (mana < 0) {
            this.currentMana = 0;
        } else if (mana > this.maxMana) {
            this.currentMana = this.maxMana;
        } else {
            this.currentMana = mana;
        }  
    }

    /**
     * Sets the total mana displayed on the bar.
     * @param mana The new mana cap.
     */
    public void setMaxMana(float mana) { this.maxMana = mana; }
    
    public void drawToLayer(PGraphics layer) {
        layer.strokeWeight(this.getBorderThickness());

        layer.strokeWeight(0);
        this.drawBackgroundBar(layer);
        this.drawManaBar(layer);
        this.drawText(layer);

        layer.strokeWeight(App.DEFAULT_STROKE_WEIGHT);
        layer.textSize(App.DEFAULT_TEXT_SIZE);
    }
    
    /**
     * Draws the background bar to the specified layer.
     * @param layer The layer to which the bar is drawn.
     */
    private void drawBackgroundBar(PGraphics layer) {
        layer.stroke(50, 50, 50);
        layer.fill(this.backBarColour.x, this.backBarColour.y, this.backBarColour.z);
        layer.rect(this.getPos().x, this.getPos().y, this.getWidth(), this.getHeight());
    }

    /**
     * Draws the mana bar to the specified layer.
     * @param layer The layer to which the mana bar is drawn.
     */
    private void drawManaBar(PGraphics layer) {
        if (this.currentMana > 0) {
            layer.fill(this.manaBarColour.x, this.manaBarColour.y, this.manaBarColour.z);
            layer.rect(this.getPos().x, this.getPos().y,
                       this.getWidth() * this.currentMana / this.maxMana, this.getHeight());
            
            layer.fill(  43, 150, 193  );
            layer.rect(this.getPos().x, this.getPos().y,
                       this.getWidth() * this.currentMana / this.maxMana, this.getHeight() / 2);

            layer.fill( 52, 197, 255 );
            layer.rect(this.getPos().x, this.getPos().y,
                       this.getWidth() * this.currentMana / this.maxMana, this.getHeight() / 4);
        }
    }

    /**
     * Draws the text to the specified layer.
     * @param layer The layer to which the text is drawn.
     */
    private void drawText(PGraphics layer) {
        layer.textSize(this.getTextSize());
        layer.fill(this.textColour.x, this.textColour.y, this.textColour.z);
        
        String manaBarText = Math.round(currentMana) + " / " + Math.round(maxMana);
        layer.text(manaBarText, this.getPos().x + 100, this.getPos().y,
                   this.getWidth(), this.getHeight());
    }
}
