package WizardTD.Game.Entities;

import processing.core.PVector;

/**
 * This is an interface for Entities.
 * An entity is an object that is rendered and executes logic every frame.
 */
public abstract class Entity {
    /**
     * The position of the entity's top-left corner.
     */
    private PVector pos = new PVector();
    /**
     * The position of the entity from the center of its sprite
     */
    private PVector centerPos = new PVector();

     /**
     * By how much the monster's speed is multiplied.
     */
    private float speedMultiplier = 1;

    /**
     * The width of the entity's sprite divided by 2.
     */
    private float xOffset = 0;
    /**
     * The height of the ntity's sprite divided by 2.
     */
    private float yOffset = 0;

    /**
     * Gets the position of the entity.
     * @return Returns the position of the entity's top-left corner.
     */
    public PVector getPos() { return this.pos; };
    public void setPos(float x, float y) {
        this.pos.set(x, y);
        this.centerPos.set(x + this.xOffset, y + this.yOffset);
    }

    public void updatePos(float x, float y) {
        this.pos.add(x, y);
        this.centerPos.add(x, y);
    }

    /**
     * Gets the center position of the entity.
     * @return Returns the position of the entity's center.
     */
    public PVector getCenterPos() { return this.centerPos; };
    /**
     * Sets the center position of the monster.
     * @param x The new x-position.
     * @param y The new y-position.
     */
    public void setCenterPos(float x, float y) { 
        this.centerPos.set(x, y); 
        this.pos.set(x - xOffset, y - yOffset);
    }

    /**
     * Gets the offset of the x-position.
     * @return The offset of the x-position.
     */
    public float getXOffset() { return this.xOffset;}
    /**
     * Sets the offset of the x-position.
     * @param offset The new offset.
     */
    public void setXOffset(float offset) { this.xOffset = offset; }

    /**
     * Gets the offset of the y-position.
     * @return The offset of the y-position.
     */
    public float getYOffset() { return this.yOffset; }
    /**
     * Sets the offset of the y-position.
     * @param offset The new offset.
     */
    public void setYOffset(float offset) { this.yOffset = offset; }

    /**
     * This method is intended to execute the entity's logic every frame.
     */
    public abstract void tick();

    /**
     * Gets the monster's speed multiplier.
     * @return The monster's speed multiplier.
     */
    public float getSpeedMultiplier() { return this.speedMultiplier; }
    /**
     * This method sets the speed multiplier of the entity.
     * @param multiplier By how much the entity's speed is to be multiplied. Default is 1.0.
     */
    public void setSpeedMultiplier(float multiplier) { this.speedMultiplier = multiplier; }

}
