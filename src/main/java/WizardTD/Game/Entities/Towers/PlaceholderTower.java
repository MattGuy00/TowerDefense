package WizardTD.Game.Entities.Towers;

import processing.core.PGraphics;

/**
 * Represents the {@code PlaceHolderTower} class.
 * <p>
 * A placeholder tower is used for displaying a tower that
 * that the player intends to build.
 * <p>
 * Placeholder towers have minimal functionality compared
 * to its sibling classes.
 */
public final class PlaceholderTower extends Tower {
    /**
     * The amount of levels the tower gains
     * on upgrade.
     */
    private int changeInLevel = 1;
    /**
     * The current visibility of the tower.
     */
    private boolean isVisible = false;
    /**
     * The total cost to build the tower.
     */
    private float buildCost;

    /**
     * {@code PlaceHolderTower}'s constructor.
     * <p>
     * Constructs a tower with minimal functionality.
     * @param towerManager The manager that handles this tower.
     * @param x x-position of the tower's center.
     * @param y y-position of the tower's center.
     * @param range The towers base range.
     */
    public PlaceholderTower(TowerManager towerManager, float x, float y, float range) {
        this.setXOffset(this.getCurrentSprite().width / 2);
        this.setYOffset(this.getCurrentSprite().height / 2);
        this.setCenterPos(x, y);

        this.setTowerManager(towerManager);
        this.setRange(range);
        this.buildCost = towerManager.getBaseTowerCost();
    }

    /**
     * Gets the total cost to build this tower.
     * @return The cost to build this tower.
     */
    public float getBuildCost() { return this.buildCost; }

    @Override
    public void upgradeRange() {
        if (this.getRangeLevel() > 0) {
            return;
        }

        this.setRange(this.getRange() + this.getTowerManager().getRangeIncreasePerUpgrade());
        this.setRangeLevel(this.getRangeLevel() + this.changeInLevel);
        this.buildCost += this.getTowerManager().getInitialUpgradeCost();
    }

    /**
     * Downgrades the tower's range by the amount specified in
     * {@code changeInLevel}.
     */
    public void downgradeRange() {
        this.setRange(this.getRange() - this.getTowerManager().getRangeIncreasePerUpgrade());
        this.setRangeLevel(this.getRangeLevel() - this.changeInLevel);
        this.buildCost -= this.getTowerManager().getInitialUpgradeCost();
    }

    @Override
    public void upgradeFiringSpeed() {
        if (this.getFiringSpeedLevel() > 0) {
            return;
        }

        this.setFiringSpeedLevel(this.getFiringSpeedLevel() + this.changeInLevel);
        this.buildCost += this.getTowerManager().getInitialUpgradeCost();
    }

    /**
     * Downgrades the tower's firing speed by the amount specified in
     * {@code changeInLevel}.
     */
    public void downgradeFiringSpeed() {
        this.setFiringSpeedLevel(this.getFiringSpeedLevel() - this.changeInLevel);
        this.buildCost -= this.getTowerManager().getInitialUpgradeCost();
    }

    @Override
    public void upgradeDamage() {
        if (this.getDamageLevel() > 0) {
            return;
        }
        
        this.setDamageLevel(this.getDamageLevel() + changeInLevel);
        this.buildCost += this.getTowerManager().getInitialUpgradeCost();
    }

    /**
     * Downgrades the tower's damage by the amount specified in
     * {@code changeInLevel}.
     */
    public void downgradeDamage() {
        this.setDamageLevel(this.getDamageLevel() - changeInLevel);
        this.buildCost -= this.getTowerManager().getInitialUpgradeCost();
    }

    /**
     * Sets the visibility status of the tower to true.
     */
    public void show() { this.isVisible = true; }
    /**
     * Sets the visibility status of the tower to false.
     */
    public void hide() { this.isVisible = false; }

    @Override
    public void drawToLayer(PGraphics layer) {
        if (!this.isVisible) {
            return;
        }

        layer.image(this.getCurrentSprite(), this.getPos().x, this.getPos().y);
        this.drawUpgrades(layer);
        this.drawRangeIndicator(layer);
    }
    
}
