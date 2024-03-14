package WizardTD.Game.Entities.Monsters;

import WizardTD.App;
import WizardTD.Game.UI.HealthBar;
import processing.core.PImage;

/**
 * Represents the Gremlin monster.
 */
public class Gremlin extends Monster {
    /**
     * A static PImage of the Gremlin sprite.
     */
    private static PImage gremlinSprite;
    /**
     * A static PImage array for the Gremlin's death animation.
     */
    private static PImage[] gremlinDeathImages = new PImage[5];

   /**
    * Gremlin constructor.
    * <p>
    * Constructs a Gremlin that seeks the shortest path to the wizard's house.
    * @param initialHP The initial health points of the Gremlin.
    * @param speed The initial speed of the Gremlin.
    * @param armour The initial armour of the gremlin.
    * @param manaOnDeath The amount of mana the player gains on this monster's death.
    */
    public Gremlin(int initialHP, float speed, float armour, int manaOnDeath) {
        super(initialHP, speed, armour, manaOnDeath);

        this.setCurrentSprite(gremlinSprite);
        this.setXOffset(gremlinSprite.width / 2);
        this.setYOffset(gremlinSprite.height / 2);
        
        this.setDeathAnimImages(gremlinDeathImages);
        float height = 4;
        this.setHealthBar(new HealthBar(this.getCenterPos(), gremlinSprite.width, height, initialHP));
    }

    /**
     * Loads the Gremlin's sprite and death images.
     * @param app The app used to load the sprites.
     */
    public static void loadSprites(App app) {
        gremlinSprite = app.loadImage("src/main/resources/WizardTD/gremlin.png");

        gremlinDeathImages[0] = app.loadImage("src/main/resources/WizardTD/gremlin1.png");       
        gremlinDeathImages[1] = app.loadImage("src/main/resources/WizardTD/gremlin2.png");
        gremlinDeathImages[2] = app.loadImage("src/main/resources/WizardTD/gremlin3.png");
        gremlinDeathImages[3] = app.loadImage("src/main/resources/WizardTD/gremlin4.png");
        gremlinDeathImages[4] = app.loadImage("src/main/resources/WizardTD/gremlin5.png");
    }
}
