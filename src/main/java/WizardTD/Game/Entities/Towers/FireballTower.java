package WizardTD.Game.Entities.Towers;

import java.util.LinkedList;

import WizardTD.Game.Entities.Monsters.Monster;
import WizardTD.Game.Player.ManaPool;
import processing.core.PGraphics;

/**
 * Represents the {@code FireballTower} class.
 * <p>
 * This tower has the ability to shoot fireballs.
 */
public final class FireballTower extends Tower {
    /**
     * The monster's that are currently on the board.
     */
    private final LinkedList<Monster> monstersToTarget;
    /**
     * A LinkedList of active fireballs shot from the tower.
     */
    private final LinkedList<Fireball> fireballs = new LinkedList<>();
    /**
     * The tower's current target.
     */
    private Monster target = null;
    /**
     * A frame counter used for firing speed.
     */
    private int frameCounter = 0;

    /**
     * The constructor for the FireballTower class.
     * @param towerManager Manages this tower.
     * @param manaPool For removing mana when created and upgraded.
     * @param activeMonsters The monster's that are currently on the board.
     * @param x The x position of the tower's center.
     * @param y The y position of the tower's center.
     * @param range The tower's default range.
     * @param firingSpeed How fast the tower shoots by default.
     * @param damage The tower's default damage.
     */
    public FireballTower(TowerManager towerManager, ManaPool manaPool, 
                         LinkedList<Monster> activeMonsters,
                         float x, float y, float range, 
                         float firingSpeed, float damage) {
        this.setXOffset(this.getCurrentSprite().width / 2);
        this.setYOffset(this.getCurrentSprite().height / 2); 
        this.setCenterPos(x, y);    

        this.setTowerManager(towerManager);
        this.monstersToTarget = activeMonsters;
        this.setManaPool(manaPool);
        this.setRange(range);
        this.setFiringSpeed(firingSpeed);
        this.setDamage(damage);
        manaPool.removeMana(towerManager.getBaseTowerCost());
    }

    /**
     * Gets the fireball's that are currently moving towards a target.
     * @return The fireball's that are currently moving towards a target.
     */
    public LinkedList<Fireball> getFireballs() { return this.fireballs; }

    @Override
    public void setSpeedMultiplier(float speedMulti) {
        // make sure to use super otherwise its infinitely recursive
        super.setSpeedMultiplier(speedMulti); 
        for (Fireball projectile : fireballs) {
            projectile.setSpeedMultiplier(speedMulti);
        }
    }

    public void tick() {
        this.upgradeTowerIfPossible();
        getClosestTarget();
        attackTarget();
    }

    /**
     * Shoots fireball's at the current target when possible.
     * Also ticks each fireball it has shot.
     */
    private void attackTarget() {
        for (Fireball proj : this.fireballs) {
            proj.tick();
        }
        this.fireballs.removeIf(b -> (b.targetReached()));

        if (this.target == null) {
            this.frameCounter = 0;
            return;
        }

        // This is the tower's firing speed
        if (Math.round(this.frameCounter % this.getFramesBetweenEachShot()) == 0) {
            this.fireballs.add(new Fireball(this.getCenterPos().x, this.getCenterPos().y,
                                            this.getDamage(), this.target));
        }

        this.frameCounter += this.getSpeedMultiplier();
    }

    /**
     * Gets the closest valid target in range of the tower.
     * Only gets a new target if current target is out of range or dead
     */
    private void getClosestTarget() {
        for (Monster monster : this.monstersToTarget) {
            // Gets the first monster that's in range and alive
            if (this.getCenterPos().dist(monster.getCenterPos()) <= this.getRange() &&
                monster.isAlive()) {
                this.target = monster;
                break;
            } else {
                this.target = null;
            }
        }
    }

    @Override
    public void drawToLayer(PGraphics layer) {
        layer.image(this.getCurrentSprite(), this.getPos().x, this.getPos().y);
        this.drawUpgrades(layer);
        this.drawRangeIndicator(layer);
        drawProjectiles(layer); 
    }

    /**
     * Draws the tower's active projectiles to the layer.
     * @param layer The layer to which projectiles are drawn.
     */
    private void drawProjectiles(PGraphics layer) {
        for (Fireball proj : this.fireballs) {
            proj.drawToLayer(layer);
        }
    } 
}
