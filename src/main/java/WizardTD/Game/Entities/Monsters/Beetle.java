package WizardTD.Game.Entities.Monsters;

import WizardTD.App;
import WizardTD.Game.UI.HealthBar;
import processing.core.PImage;

/**
 * This class represents the beetle monster.
 * Beetle is a child class of Monster.
 */
public class Beetle extends Monster {
    /**
     * A static PImage array that holds the death sprites for the death animation.
     */
    private static PImage[] deathAnimImages = new PImage[5];
    /**
     * A static PImage array that holds the default beetle sprite and its rotated versions.
     */
    private static PImage[] rotatedSprites = new PImage[4];

    /**
     *  Beetle class's sole constructor.
     * @param initialHP The health points that the beetle spawns with.
     * @param speed The default movement speed that beetle spawns with.
     * @param armour This and the innateArmour variable are added to set the beetle's default armour.
     * @param manaOnDeath How much mana is gained on the beetle's death.
     * @throws IllegalArgumentException If initialHP param is <= 0, or if any of the other params are < 0.
     */
    public Beetle(int initialHP, float speed, float armour, int manaOnDeath) throws IllegalArgumentException {
        super(initialHP, speed, armour, manaOnDeath);
        this.setXOffset(rotatedSprites[0].width / 2);
        this.setYOffset(rotatedSprites[0].height / 2);
        this.setCurrentSprite(rotatedSprites[0]);
        this.setDeathAnimImages(deathAnimImages);
        
        float height = 4;
        this.setHealthBar(new HealthBar(this.getCenterPos(), 
                                        rotatedSprites[0].width,
                                        height, initialHP));
    }

    /**
     * Moves and rotates the beetle towards the current destination.
     */
    @Override
    public void moveTowardsDest() {
        // Finds the direction in which the monster should move, then moves it
        float yDiff = this.getCenterPos().y - this.getCurrentDest().getCenterPos().y;
        float xDiff = this.getCenterPos().x - this.getCurrentDest().getCenterPos().x;

        boolean up = yDiff > 0;
        boolean down = yDiff < 0;
        boolean left = xDiff > 0;
        boolean right = xDiff < 0;

        float moveSpeed = this.getSpeed() * this.getSpeedMultiplier();
        if (up) {
            this.updatePos(0, -moveSpeed);
            this.setCurrentSprite(rotatedSprites[0]);
        } else if (down) {
            this.updatePos(0, moveSpeed);
            this.setCurrentSprite(rotatedSprites[1]);
        }

        if (left) {
            this.updatePos(-moveSpeed, 0);
            this.setCurrentSprite(rotatedSprites[2]);
        } else if (right) {
            this.updatePos(moveSpeed, 0);
            this.setCurrentSprite(rotatedSprites[3]);
        }
    }

    /**
     * Static method that loads the default and rotated beetle sprites, as well as the death sprites.
     * This must be called before a beetle is spawned.
     * @param app Allows us to load and rotate the images.
     */
    public static void loadSprites(App app) {
        PImage beetleSprite = app.loadImage("src/main/resources/WizardTD/beetle.png");
        rotatedSprites[0] = beetleSprite; // Upward facing sprite
        rotatedSprites[1] = app.rotateImageByDegrees(beetleSprite, 180); // Downward facing sprite
        rotatedSprites[2] = app.rotateImageByDegrees(beetleSprite, 270); // Left facing sprite
        rotatedSprites[3] = app.rotateImageByDegrees(beetleSprite, 90); // Right facing sprite
        
        deathAnimImages[0] = app.loadImage("src/main/resources/WizardTD/gremlin3.png");
        deathAnimImages[1] = app.loadImage("src/main/resources/WizardTD/gremlin4.png");
        deathAnimImages[2] = app.loadImage("src/main/resources/WizardTD/gremlin4.png");
        deathAnimImages[3] = app.loadImage("src/main/resources/WizardTD/gremlin5.png");
        deathAnimImages[4] = app.loadImage("src/main/resources/WizardTD/gremlin5.png");
    }
}
