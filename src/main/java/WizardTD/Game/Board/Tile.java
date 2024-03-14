package WizardTD.Game.Board;

import WizardTD.Game.Renderable;
import WizardTD.Game.Entities.Entity;
import processing.core.PGraphics;
import processing.core.PImage;
import processing.core.PVector;

/**
 * Tile is the abstract base class for all objects that are tiles,
 * which are to be used in the board class.
 */
public abstract class Tile implements Renderable {
    /**
     * The tile's current sprite.
     */
    private PImage currentSprite;
    /**
     * The position of the tile's top-left corner.
     */
    private PVector pos = new PVector();

    /**
     * Sets whether the tile can be walked on.
     */
    private boolean walkable = false;
    /**
     * Sets whether objects can be placed on the tile.
     */
    private boolean placeable = false;
    /**
     * Sets whether the tile is currently occupied.
     */
    private boolean occupied = false;

    /**
     * @see Entity#getPos()
     */
    public PVector getPos() { return this.pos; }
    /**
     * Sets the position of the tile's top-left corner.
     * @param x The x position of the tile's top-left corner.
     * @param y The y position of the tile's top-left corner.
     */
    public void setPos(float x, float y) { this.pos.set(x, y); }

    /**
     * @see Entity#getCenterPos()
     */
    public PVector getCenterPos() {
        float xOffset = this.currentSprite.width / 2;
        float yOffset = this.currentSprite.height / 2;
        return new PVector(this.pos.x + xOffset, this.pos.y + yOffset);
    }

    /**
     * Gets the tile's current sprite.
     * @return the tile's sprite.
     */
    public PImage getCurrentSprite() { return this.currentSprite; }
    /**
     * Sets the tile's sprite.
     * @param sprite The sprite that tile is changed to.
     */
    public void setCurrentSprite(PImage sprite) { this.currentSprite = sprite; }

    /**
     * Gets the tile's walkable status.
     * @return true if the tile is walkable, false otherwise.
     */
    public boolean isWalkable() { return this.walkable; }
    /**
     * Sets the tile's walkable status.
     * @param walkable The new status.
     */
    public void setWalkable(boolean walkable) { this.walkable = walkable; }

    /**
     * Gets the tile's placeability status
     * @return true if objects can be placed on the tile, false otherwise.
     */
    public boolean isPlaceable() { return this.placeable; }
    /**
     * Sets the tile's placeability
     * @param placeable The new placeability.
     */
    public void setPlaceable(boolean placeable) { this.placeable = placeable; }

    /**
     * Gets the tile's occupation status.
     * @return true if an object occupies the tile, false otherwise.
     */
    public boolean isOccupied() { return this.occupied; }
    /**
     * Sets the tile's occupation status.
     * @param occupied The tiles new occupation status.
     */
    public void setOccupied(boolean occupied) { this.occupied = occupied; }

    public void drawToLayer(final PGraphics layer) {
        layer.image(this.currentSprite, this.pos.x, this.pos.y);   
    }
}
