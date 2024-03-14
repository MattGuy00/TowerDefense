package WizardTD.Game.UI;

import WizardTD.App;
import processing.core.PGraphics;
import processing.core.PVector;

/**
 * Represents the HealthBar class.
 * <p>
 * This class is to be used to give a visual representation of
 * an entity's current health points.
 */
public final class HealthBar extends UIElement {
    /**
     * The max health points of the monster.
     */
    private final float maxHP;
    /**
     * The current health points of the monster.
     * <p>
     * Used for modifying the width of the current health bar.
     */
    private float currentHP;

    /**
     * HealthBar's constructor
     * @param pos A reference to the entity's center position.
     * @param w The width of the bar.
     * @param h The height of the bar.
     * @param maxHP The entity's max health points.
     * @throws NullPointerException Throws if pos is null.
     */
    public HealthBar(PVector pos, float w, float h, float maxHP) throws NullPointerException {
        if (pos == null) {
            throw new NullPointerException("Unable to instantiate HealthBar because pos is null");
        }

        this.setPos(pos);
        this.setWidth(w);
        this.setHeight(h);
        this.maxHP = maxHP;
        this.currentHP = maxHP;    
    }

    public float getMaxHP() { return this.maxHP; }
    public float getCurrentHP() { return this.currentHP; }

    /**
     * Sets the current health points for display purposes.
     * @param currentHP The current health points of the entity.
     */
    public void setCurrentHP(float currentHP) {
        this.currentHP = currentHP;
    }

    public void drawToLayer(PGraphics layer) {
        layer.fill(255, 0, 0);
        layer.rect(this.getPos().x - this.getWidth() / 2, this.getPos().y - this.getHeight() * 4, 
                   this.getWidth(), this.getHeight());

        // The green bar represents the monster's current HP
        if (currentHP > 0) {
            layer.fill(0, 255, 0);
            layer.rect(this.getPos().x - this.getWidth() / 2, this.getPos().y - this.getHeight() * 4, 
                       this.getWidth() * currentHP / maxHP, this.getHeight());
        }

        // Setting layer back to default settings
        // Or bad things will happen
        layer.fill(App.DEFAULT_FILL_COLOUR);
    }
    
}