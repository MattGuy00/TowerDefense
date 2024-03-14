package WizardTD.Game.Entities.Towers;

import WizardTD.App;
import WizardTD.Game.Renderable;
import WizardTD.Game.Entities.Entity;
import WizardTD.Game.Player.ManaPool;
import processing.core.PGraphics;
import processing.core.PImage;

/**
 * This is an abstract base class for all tower types.
 * <p>
 * A tower is an object that is placed by the player,
 * which then attacks monsters on the board.
 */
public abstract class Tower extends Entity implements Renderable {
    /**
     * The tower's base, or level 1, sprite
     */
    private static PImage baseSprite;
    /**
     * The tower's level 2 sprite.
     */
    private static PImage level2Sprite;
    /**
     * The tower's level 3 sprite.
     */
    private static PImage level3Sprite;

    /**
     * The tower manager that manages all towers.
     */
    private TowerManager towerManager;
    
    /**
     * The mana pool used when upgrading and building towers.
     */
    private ManaPool manaPool;

    /**
     * The tower's current sprite.
     * <p>
     * {@code baseSprite} by default.
     */
    private PImage currentSprite = baseSprite;
    /**
     * The current cost of upgrading the tower's range.
     */
    private float rangeUpgradeCost;
    /**
     * The current cost of upgrading the tower's firing speed.
     */
    private float firingSpeedUpgradeCost;
    /**
     * The current cost of upgrading the tower's damage.
     */
    private float damageUpgradeCost;

    /**
     * The tower's range.
     */
    private float range;
    /**
     * The number of projectiles fired per second.
     */
    private float firingSpeed; // Projectiles fired per second. 60 frames per second. So, 60/(proj/s) gives us frames between each shot.
    /**
     * The number of frames before the tower shoots again.
     * <p>
     *  FPS/(proj/s) gives us the frames between each shot.
     */
    private float framesBetweenEachShot;
    /**
     * The tower's damage.
     */
    private float damage;

    /**
     * The level of the tower.
     */
    private int towerLevel = 1;
    /**
     * The level of the tower's range.
     */
    private int rangeLevel = 0;
    /**
     * The level of the tower's firing speed.
     */
    private int firingSpeedLevel = 0;
    /**
     * The level of the tower's damage.
     */
    private int damageLevel = 0;

    /**
     * The status of the tower being hovered over by the cursor.
     */
    private boolean hoveredOver = false;

    /**
     * Gets the current cost to upgrade the tower's range
     * @return The cost to upgrade the tower's range.
     */
    public float getRangeUpgradeCost() { return this.rangeUpgradeCost; }
    /**
     * Gets the current cost to upgrade the tower's firing speed
     * @return The cost to upgrade the tower's firing speed.
     */
    public float getFiringSpeedUpgradeCost() { return this.firingSpeedUpgradeCost; }
    /**
     * Gets the current cost to upgrade the tower's damage.
     * @return The cost to upgrade the tower's damage.
     */
    public float getDamageUpgradeCost() { return this.damageUpgradeCost; }

    /**
     * Gets the tower's current level.
     * @return The tower's level.
     */
    public int getTowerLevel() { return this.towerLevel; }
    /**
     * Gets current level of the tower's range.
     * @return The level of the tower's range.
     */
    public int getRangeLevel() { return this.rangeLevel; }
    /**
     * Gets current level of the tower's firing speed.
     * @return The level of the tower's firing speed.
     */
    public int getFiringSpeedLevel() { return this.firingSpeedLevel; }
    /**
     * Gets current level of the tower's damage.
     * @return The level of the tower's damage.
     */
    public int getDamageLevel() { return this.damageLevel; }

    /**
     * Gets the tower's current sprite.
     * @return The tower's current sprite.
     */
    public PImage getCurrentSprite() { return this.currentSprite; }

    /**
     * Gets the tower manager managing the tower.
     * @return The tower manager managing the tower.
     */
    public TowerManager getTowerManager() { return this.towerManager; }
    /**
     * Sets the tower manager managing this tower,
     * and initialises the tower's attributes.
     * @param manager The new manager.
     */
    public void setTowerManager(TowerManager manager) { 
        this.towerManager = manager;
        this.rangeUpgradeCost = manager.getInitialUpgradeCost();
        this.firingSpeedUpgradeCost = manager.getInitialUpgradeCost();
        this.damageUpgradeCost = manager.getInitialUpgradeCost();
    }

    /**
     * Gets the mana pool used by the tower.
     * @return The mana pool used by the tower.
     */
    public ManaPool getManaPool() { return this.manaPool; }
    /**
     * Sets the mana pool used by the tower.
     * @param pool The new mana pool.
     */
    public void setManaPool(ManaPool pool) { this.manaPool = pool; }

    /**
     * Gets the current range of the tower.
     * @return the current range of the tower.
     */
    public float getRange() { return this.range; }
    /**
     * Sets the range of the tower.
     * @param range The new range of the tower.
     */
    public void setRange(float range) { this.range = range; }
    /**
     * Sets the tower's range level.
     * @param level the new range level.
     */
    public void setRangeLevel(int level) { this.rangeLevel = level; }

    /**
     * Gets the current firing speed of the tower.
     * @return the current firing speed of the tower.
     */
    public float getFiringSpeed() { return this.firingSpeed; }
    /**
     * Sets the firing speed of the tower.
     * @param speed The new firing speed of the tower.
     */
    public void setFiringSpeed(float speed) { 
        this.firingSpeed = speed;
        this.framesBetweenEachShot = App.FPS / (speed * this.getSpeedMultiplier());
    }

    /**
     * Sets the tower's firing speed level.
     * @param level the new firing speed level.
     */
    public void setFiringSpeedLevel(int level) { this.firingSpeedLevel = level;}

    /**
     * Gets the current damage of the tower.
     * @return the current damage of the tower.
     */
    public float getDamage() { return this.damage; }
    /**
     * Sets the damage of the tower.
     * @param damage The new damage of the tower.
     */
    public void setDamage(float damage) { this.damage = damage; }
    /**
     * Sets the tower's damage level.
     * @param level the new damage level.
     */
    public void setDamageLevel(int level) { this.damageLevel = level; }

    /**
     * Gets the number of frames the tower waits before firing.
     * @return The number of frames the tower waits before firing.
     */
    public float getFramesBetweenEachShot() { return this.framesBetweenEachShot; }
   
    /**
     * Gets the status of the {@code hoveredOver} boolean.
     * @return {@code true} if the tower is being hovered, {@code false} otherwise.
     */
    public boolean isBeingHoveredOver() { return this.hoveredOver; }
    /**
     * Sets the status of the {@code hoveredOver} boolean.
     * @param hoveredOver the new status.
     */
    public void setHoveredOver(boolean hoveredOver) { this.hoveredOver = hoveredOver; }

    /**
     * Upgrades the range of the tower.
     * <p>
     * If the player does not have enough mana, this method does nothing. 
     * Otherwise, the tower will upgrade and remove mana from the mana pool.
     * 
     */
    public void upgradeRange() {
        if (this.rangeUpgradeCost >= this.manaPool.getCurrentMana()) {
            return;
        }

        this.manaPool.removeMana(this.rangeUpgradeCost);
        this.rangeUpgradeCost += this.towerManager.getCostIncreasePerLevel();
        this.range += this.towerManager.getRangeIncreasePerUpgrade();
        ++this.rangeLevel;
    }

    /**
     * Upgrades the firing speed of the tower.
     * <p>
     * If the player does not have enough mana, this method does nothing. 
     * Otherwise, the tower will upgrade and remove mana from the mana pool.
     * 
     */
    public void upgradeFiringSpeed() {
        if (this.firingSpeedUpgradeCost >= this.manaPool.getCurrentMana()) {
            return;
        }

        this.manaPool.removeMana(this.firingSpeedUpgradeCost);
        this.firingSpeedUpgradeCost += this.towerManager.getCostIncreasePerLevel();
        this.firingSpeed += this.towerManager.getFiringSpeedIncreasePerUpgrade();
        this.framesBetweenEachShot = App.FPS / this.firingSpeed;
        ++this.firingSpeedLevel;
    }

    /**
     * Upgrades the damage of the tower.
     * <p>
     * If the player does not have enough mana, this method does nothing. 
     * Otherwise, the tower will upgrade and remove mana from the mana pool.
     * 
     */
    public void upgradeDamage() {
        if (this.damageUpgradeCost >= this.manaPool.getCurrentMana()) {
            return;
        }

        this.manaPool.removeMana(this.damageUpgradeCost);
        this.damageUpgradeCost += this.towerManager.getCostIncreasePerLevel();
        this.damage += this.towerManager.getDamageIncreasePerUpgrade();
        ++this.damageLevel;
    }

    public void tick() {
        upgradeTowerIfPossible();
    }

    /**
     * If the right conditions are met, this method upgrades the tower.
     */
    public void upgradeTowerIfPossible() {
        if (towerLevel == 1 && rangeLevel >= 1 && firingSpeedLevel >= 1 && damageLevel >= 1) {
            this.currentSprite = level2Sprite;
            ++towerLevel;
        } else if (towerLevel == 2 && rangeLevel >= 2 && firingSpeedLevel >= 2 && damageLevel >= 2) {
            this.currentSprite = level3Sprite;
            ++towerLevel;
        }
    }

    public void drawToLayer(PGraphics layer) {
        layer.image(this.currentSprite, this.getPos().x, this.getPos().y);
        this.drawUpgrades(layer);
        this.drawRangeIndicator(layer);
    }

    /**
     * Draws the tower's range indicator to the given layer.
     * <p>
     * The range indicator is a semi-transparent circle.
     * @param layer The layer that the range indicator is drawn to.
     */
    public void drawRangeIndicator(PGraphics layer) {
        if (!this.hoveredOver) {
            return;
        }

        layer.fill(20, 50);
        layer.ellipse(this.getCenterPos().x, this.getCenterPos().y, range * 2, range * 2);
        layer.noFill();
        layer.ellipse(this.getCenterPos().x, this.getCenterPos().y, range * 2, range * 2);
        layer.fill(App.DEFAULT_FILL_COLOUR);
    }

    /**
     * Draws the visual indicators of the tower's upgrades to the layer.
     * @param layer The layer that the visual indicators are drawn to.
     */
    public void drawUpgrades(PGraphics layer) {
        drawFiringSpeedUpgrades(layer);
        drawRangeUpgrades(layer);
        drawDamageUpgrades(layer);
    }   

    /**
     * Draws to the layer the tower's range upgrades based on its level.
     * @param layer The layre that the upgrades are drawn to.
     */
    private void drawRangeUpgrades(PGraphics layer) {
        int width = 6;
        int height = 6;
        layer.fill(0, 220, 0);
        // Range upgrade indicators
        for (int i = 0; i <= this.rangeLevel - this.towerLevel; i++) {
            layer.rect(this.getCenterPos().x - this.currentSprite.width / 2 + width * i,
                       this.getCenterPos().y - this.currentSprite.height / 2, width, height);
        }
        layer.fill(App.DEFAULT_FILL_COLOUR);
    }

     /**
     * Draws to the layer the tower's firing speed upgrades based on its level.
     * @param layer The layre that the upgrades are drawn to.
     */
    private void drawFiringSpeedUpgrades(PGraphics layer) {
        if (this.firingSpeedLevel - this.towerLevel >= 0) {
            layer.stroke(0, 150, 255);
            layer.noFill();
            layer.strokeWeight(this.firingSpeed - this.towerLevel + 2);
            layer.rect(this.getCenterPos().x - this.currentSprite.width / 2 + 5,
                       this.getCenterPos().y - this.currentSprite.height / 2 + 5, 20, 20);
            layer.strokeWeight(App.DEFAULT_STROKE_WEIGHT);
            layer.stroke(App.DEFAULT_STROKE_COLOUR);
        }
    }

     /**
     * Draws to the layer the tower's range damage based on its level.
     * @param layer The layre that the upgrades are drawn to.
     */
    private void drawDamageUpgrades(PGraphics layer) {
        int width = 6;
        int height = 6;
        layer.fill(240, 0, 0);
        // Damage upgrade indicators
        for (int i = 0; i <= this.damageLevel - this.towerLevel; i++) {
            layer.rect(this.getCenterPos().x - this.currentSprite.width / 2 + width * i,
                       this.getCenterPos().y + this.currentSprite.height / 2 - 6, width, height);
        }
        layer.fill(App.DEFAULT_FILL_COLOUR);
    }

    /**
     * A static method that loads the tower's sprites.
     * @param app The app used to load the sprites.
     */
    public static void loadSprites(App app) {
        baseSprite = app.loadImage("src/main/resources/WizardTD/tower0.png");
        level2Sprite = app.loadImage("src/main/resources/WizardTD/tower1.png");
        level3Sprite = app.loadImage("src/main/resources/WizardTD/tower2.png");
    }
}
