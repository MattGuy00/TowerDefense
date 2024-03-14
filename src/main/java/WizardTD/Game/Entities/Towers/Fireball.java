package WizardTD.Game.Entities.Towers;

import WizardTD.App;
import WizardTD.Game.Renderable;
import WizardTD.Game.Entities.Entity;
import WizardTD.Game.Entities.Monsters.Monster;
import processing.core.PGraphics;
import processing.core.PImage;

/**
 * Represents the fireball class.
 * <p>
 * Fireballs are projectiles that are fired from towers.
 */
public final class Fireball extends Entity implements Renderable {
    /**
     * A static field for storing the class's default sprite.
     */
    private static PImage fireballSprite;
    
    /**
     * The fireball's target. This is the entity that the fireball will move towards.
     */
    private final Monster target;
    /**
     * How much damage the fireball deals once it has reached its target.
     */
    private final float damage;
    
    /**
     * How many pixels the fireball moves per frame. Default is 5.0.
     */
    private float moveSpeed = 5.0f;

    /**
     * The fireball's current sprite.
     */
    private PImage currentSprite;
    /**
     * Tracks whether the fireball has reached its target. false by default.
     */
    private boolean targetReached = false;

    /**
     * The constructor for the fireball class.
     * @param x The fireball's pre-offset x position.
     * @param y The fireball's pre-offset y position.
     * @param damage How much damage the fireball does.
     * @param target The target of the fireball.
     */
    public Fireball(float x, float y, float damage, Monster target) {
        this.currentSprite = fireballSprite;

        this.setXOffset(fireballSprite.width / 2);
        this.setYOffset(fireballSprite.height / 2);
        this.setCenterPos(x, y);

        this.damage = damage;
        this.target = target;
    }

    /**
     * Gets the base movement speed of the fireball.
     * @return The base movement speed of the fireball.
     */
    public float getMoveSpeed() { return this.moveSpeed; }
    /**
     * Gets whether the target has been reached.
     * @return If the target has been reached or not.
     */
    public boolean targetReached() { return this.targetReached; }
    /**
     * Gets the target of the fireball.
     * @return The target of the fireball.
     */
    public Monster getTarget() { return this.target; }

    public void tick() {
        moveToTarget();
    }

    /**
     * This method moves the fireball towards the target by its current movement speed.
     */
    private void moveToTarget() {
        if (this.target == null) {
            this.targetReached = true;
            return;
        }
        
        if (this.getCenterPos().dist(this.target.getCenterPos()) < this.currentSprite.width) {
            targetReached = true;
            target.removeHP(damage);
            return;
        }

        float yDiff = this.getCenterPos().y - this.target.getCenterPos().y;
        float xDiff = this.getCenterPos().x - this.target.getCenterPos().x;

        boolean up = yDiff > 0;
        boolean down = yDiff < 0;
        boolean left = xDiff > 0;
        boolean right = xDiff < 0;

        float moveSpeed = this.moveSpeed * this.getSpeedMultiplier();
        if (up) {
            this.updatePos(0, -moveSpeed);
        } else if (down) {
            this.updatePos(0, moveSpeed);
        }

        if (left) {
            this.updatePos(-moveSpeed, 0);
        } else if (right) {
            this.updatePos(moveSpeed, 0);
        }
    }

    public void drawToLayer(PGraphics layer) {
        layer.image(this.currentSprite, this.getPos().x, this.getPos().y);
    }

    /**
     * A static method that loads the fireball's sprite.
     * @param app The app used to the load the image.
     */
    public static void loadSprite(App app) { 
        fireballSprite = app.loadImage("src/main/resources/WizardTD/fireball.png"); 
    }
    
}
