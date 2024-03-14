package WizardTD.Game.Board;

import WizardTD.App;
import processing.core.PImage;

/**
 * Represents the grass tiles.
 * Grass tiles are walkable and placeable by default.
 */
public final class Grass extends Tile {
    /**
     * A static PImage of the grass class's sprite
     */
    private static PImage grassSprite;

    /**
     * Grass constructor
     * @param x The x position of the tile's top-left corner.
     * @param y The y position of the tile's top-left corner.
     */
    public Grass(float x, float y) {
        this.setPos(x, y);
        this.setCurrentSprite(grassSprite);

        this.setWalkable(false);
        this.setPlaceable(true);
    }

    /**
     * A static method that loads the grass sprite.
     * @param app Used to load the image
     */
    public static void loadSprite(App app) {
        grassSprite = app.loadImage("src/main/resources/WizardTD/grass.png");
    }
}
