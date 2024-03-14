package WizardTD.Game.Board;

import WizardTD.App;
import processing.core.PImage;

/**
 * Represents the shrub tile.
 * <p>
 * By default, objects cannot be placed on the shrub tile, 
 * and entities are unable to walk on them.
 */
public final class Shrub extends Tile {
    /**
     * A static PImage of the shrub tile's sprite.
     */
    private static PImage shrubSprite;

    /**
     * Shrub's constructor
     * @param x The x position of the shrub tile's top-left corner.
     * @param y The y position of the shrub tile's top-left corner.
     */
    public Shrub(float x, float y) {
        this.setPos(x, y);
        this.setCurrentSprite(shrubSprite);
        this.setWalkable(false);
        this.setPlaceable(false);
    }

    /**
     * A static method that loads the shrub sprite.
     * @param app Used to load the sprite.
     */
    public static void loadSprite(App app) {
        shrubSprite = app.loadImage("src/main/resources/WizardTD/shrub.png");
    } 
}
