package WizardTD.Game.Entities.Monsters;

import WizardTD.App;
import WizardTD.Game.UI.HealthBar;
import processing.core.PImage;

/**
 * This class represents the worm monster.
 * worm is a child class of {@code Monster}.
 * @see Monster
 */
public class Worm extends Monster {
    /**
     * A static PImage of the worm's sprite.
     */
    private static PImage wormSprite;
    /**
     * A static PImage array of the worm's death images.
     */
    private static PImage[] deathAnimImages = new PImage[5];
   
    /**
    * {@code Worm}'s constructor.
    <p>
    * Constructs a worm that seeks the shortest path to the wizard's house.
    * @param initialHP The initial health points of the worm.
    * @param speed The initial speed of the worm.
    * @param armour The initial armour of the worm.
    * @param manaOnDeath The amount of mana the player gains on the worm's death.
    */
    public Worm(int initialHP, float speed, float armour, int manaOnDeath)
    {
        super(initialHP, speed, armour, manaOnDeath);
        this.setCurrentSprite(wormSprite);
        this.setXOffset(wormSprite.width / 2);
        this.setYOffset(wormSprite.height / 2);

        this.setDeathAnimImages(deathAnimImages);

        float height = 4;
        this.setHealthBar(new HealthBar(this.getCenterPos(),
                                        wormSprite.width, height,
                                        initialHP));
    }

    /**
     * A static method that loads the worm's sprites.
     * @param app The app used to load the sprites.
     */
    public static void loadSprites(App app) {
        wormSprite = app.loadImage("src/main/resources/WizardTD/worm.png");

        deathAnimImages[0] = app.loadImage("src/main/resources/WizardTD/gremlin3.png");
        deathAnimImages[1] = app.loadImage("src/main/resources/WizardTD/gremlin4.png");
        deathAnimImages[2] = app.loadImage("src/main/resources/WizardTD/gremlin4.png");
        deathAnimImages[3] = app.loadImage("src/main/resources/WizardTD/gremlin5.png");
        deathAnimImages[4] = app.loadImage("src/main/resources/WizardTD/gremlin5.png");
    }

}
