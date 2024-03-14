package WizardTD.Game.Board;

import WizardTD.App;
import processing.core.PGraphics;
import processing.core.PImage;

/**
 * Represents the wizard house tile.
 * <p>
 * By default, objects cannot be placed on this tile, and entities can walk on this tile.
 * <p>
 * Ultimately, all monsters attemp to path towards this tile.
 */
public final class WizHouse extends Tile {
    /**
     * A static PImage of the wizard house sprite.
     */
    private static PImage wizHouseSprite;

    /**
     * WizHouse's constructor
     * @param x The x position of the wizard house tile's top-left corner.
     * @param y The y position of the wizard house tile's top-left corner.
     */
    public WizHouse(float x, float y) {
       this.setPos(x, y);
       this.setCurrentSprite(wizHouseSprite);
       this.setPlaceable(false);
       this.setWalkable(true);
    }

    @Override
    public void drawToLayer(PGraphics layer) {
        layer.image(this.getCurrentSprite(), this.getPos().x - 8, this.getPos().y - 8);
    }

    /**
     * A static method that loads the wizard house sprite.
     * @param app Used to load the sprite.
     */
    public static void loadSprite(App app) {
        wizHouseSprite = app.loadImage("src/main/resources/WizardTD/wizard_house.png");
    }
}
