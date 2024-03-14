package WizardTD.Game.Board;

import WizardTD.App;
import processing.core.PImage;

/**
 * Represents The path tile.
 * <p>
 * Path tiles are walkable but not placeable by default.
 * Monsters will walk along this tile.
 */
public final class Path extends Tile {
    /**
     * The straight path sprite. Runs from top to bottom by default.
     */
    private static PImage pathStraightSprite;
    /**
     * The bent path sprite. Runs from left to bottom in an elbow bend.
     */
    private static PImage pathBendSprite;
    /**
     * This path sprite is shaped like a T.
     */
    private static PImage pathTSprite;
    /**
     * The cross sprite runs to all edges and is shaped like a +.
     */
    private static PImage pathCrossSprite;

    /**
     * Path's constructor
     * @param x The x position of the path's top-left corner.
     * @param y The y position of the path's top-left corner.
     */
    public Path(float x, float y) {
       this.setPos(x, y);
       this.setCurrentSprite(pathStraightSprite);
       this.setWalkable(true);
       this.setPlaceable(false);
    }

    /**
     * A static method that loads the path sprite.
     * @param app Used to load the sprite
     */
    public static void loadSprite(App app) {
        if (app == null) throw new NullPointerException("Failed to load path sprites because app is null.");

        pathStraightSprite = app.loadImage("src/main/resources/WizardTD/path0.png");
        pathBendSprite = app.loadImage("src/main/resources/WizardTD/path1.png");
        pathTSprite = app.loadImage("src/main/resources/WizardTD/path2.png");
        pathCrossSprite = app.loadImage("src/main/resources/WizardTD/path3.png");
    }

    /**
     * Converts the current sprite to a bent path sprite.
     */
    public void convertToBend() { this.setCurrentSprite(pathBendSprite); }
    /**
     * Converts the current sprite to a T-intersection path sprite.
     */
    public void convertToT() { this.setCurrentSprite(pathTSprite); }
    /**
     * Converts the current sprite to a + shaped path sprite.
     */
    public void convertToCross() { this.setCurrentSprite(pathCrossSprite); }

    /**
     * Rotates the current sprite by the specified angle.
     * @see App#rotateImageByDegrees(PImage, double)
     * @param app Used tfor rotatint the image.
     * @param angle The angle betwen 0-360 degrees.
     */
    public void rotateImage(App app, double angle) {
        this.setCurrentSprite(app.rotateImageByDegrees(this.getCurrentSprite(), angle));
    }
}
