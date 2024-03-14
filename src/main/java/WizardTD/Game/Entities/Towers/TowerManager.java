package WizardTD.Game.Entities.Towers;

import java.util.LinkedList;

import WizardTD.App;
import WizardTD.Game.Entities.Monsters.Monster;
import WizardTD.Game.Player.ManaPool;
import processing.core.PVector;
import processing.data.JSONObject;

/**
 * Represents the {@code TowerManager} class.
 * <p>
 * {@code TowerManager} is used to manage all towers, built and unbuilt, during the game.
 */
public final class TowerManager {
    /**
     * Targets for a tower.
     */
    private final LinkedList<Monster> activeMonsters;
    /**
     * @see ManaPool
     */
    private final ManaPool manaPool;

    /**
     * How much the cost of an upgrade increases per level.
     */
    private final float costIncreasePerLevel = 10.0f;
    /**
     * Initial cost to upgrade a tower.
     */
    private final float initialUpgradeCost = 20f;
    /**
     * How much a tower's range increases per level.
     */
    private final float rangeIncreasePerUpgrade = App.CELLSIZE;
    /** 
     * How much a tower's firing speed increases per level.
     */
    private final float firingSpeedIncreasePerUpgrade = 0.5f;
    /**
     * How much a tower's damage increases per level.
     */
    private final float damageIncreasePerUpgrade;

    /**
     * A tower's base range.
     */
    private final float initialRange;
    /**
     * A tower's base firing speed.
     */
    private final float initialFiringSpeed;
    /**
     * A tower's base damage.
     */
    private final float initialDamage;
    /**
     * The base cost to build a tower
     */
    private final float initialCost;

    /**
     * The towers that are active on the board.
     */
    private LinkedList<Tower> towers = new LinkedList<>();
    /**
     * The tower that the player may build.
     */
    private PlaceholderTower unbuiltTower = null;

    /**
     * {@code TowerManager}'s constructor.
     * <p>
     * Constructs a tower manager.
     * @param gameConfig Contains the initial values used for all towers.
     * @param manaPool Needed for building towers.
     * @param activeMonsters The monsters that are active on the board.
     * @throws NullPointerException If any of the parameters are null.
     */
    public TowerManager(JSONObject gameConfig, ManaPool manaPool,
                        LinkedList<Monster> activeMonsters) throws NullPointerException {
        if (gameConfig == null || manaPool == null || activeMonsters == null) {
            throw new NullPointerException("TowerManager must be constructed with non-null objects");
        }

        this.activeMonsters = activeMonsters;
        this.manaPool = manaPool;

        this.initialRange = gameConfig.getFloat("initial_tower_range");
        this.initialFiringSpeed = gameConfig.getFloat("initial_tower_firing_speed");
        this.initialDamage = gameConfig.getFloat("initial_tower_damage");
        this.damageIncreasePerUpgrade = this.initialDamage / 2;
        this.initialCost = gameConfig.getFloat("tower_cost");       
    }

    /**
     * Gets the active towers on the board.
     * @return A list of active towers.
     */
    public LinkedList<Tower> getTowers() { return this.towers; }
    /**
     * Gets the initial cost of building a tower.
     * @return The initial cost of building a tower.
     */
    public float getBaseTowerCost() { return this.initialCost; }
    /**
     * Gets increase in upgrade cost per level.
     * @return How much the cost of an upgrade increases per level.
     */
    public float getCostIncreasePerLevel() { return this.costIncreasePerLevel; }
    /**
     * Gets the initial cost of upgrading a tower.
     * @return The initial cost of upgrading a tower.
     */
    public float getInitialUpgradeCost() { return this.initialUpgradeCost; }

    /**
     * Gets the increase in range per upgrade.
     * @return The increase in range per upgrade.
     */
    public float getRangeIncreasePerUpgrade() { return this.rangeIncreasePerUpgrade; }
    /**
     * Gets the increase in firing speed per upgrade.
     * @return The increase in firing speed per upgrade.
     */
    public float getFiringSpeedIncreasePerUpgrade() { return this.firingSpeedIncreasePerUpgrade; }
    /**
     * Gets the increase in damage per upgrade.
     * @return The increase in damage per upgrade.
     */
    public float getDamageIncreasePerUpgrade() { return this.damageIncreasePerUpgrade; }
    
    /**
     * Gets the status of the tower being built.
     * @return {@code true} if there is a tower being built, otherwise {@code false}.
     */
    public boolean isTowerBeingBuilt() { return this.unbuiltTower != null; }

    /**
     * Sets the speed of all towers on the board.
     * @param speed The new speed.
     */
    public void setSpeedOfTowers(float speed) {
        for (Tower tower : this.towers) {
            tower.setSpeedMultiplier(speed);
        }
    }

    /**
     * Gets the cost of building the current {@code unbuiltTower}.
     * @return The cost of building the current {@code unbuiltTower}.
     */
    public float getCostToBuildTower() {
        if (unbuiltTower == null) {
            return this.initialCost;
        }

        return this.unbuiltTower.getBuildCost();
    }
    /**
     * Gets the tower that is being built.
     * @return {@code null} if there is no tower being built,
     * otherwise the unbuilt tower.
     */
    public PlaceholderTower getUnbuiltTower() { return this.unbuiltTower; }

    /**
     * Initialises a Placeholder tower for building.
     */
    public void initialiseUnbuiltTower() {
        unbuiltTower = new PlaceholderTower(this, -50, -50, initialRange);
        this.unbuiltTower.setHoveredOver(true);
    }

    /**
     * Sets {@code unbuiltTower} to {@code null}.
     */
    public void deactivateUnbuiltTower() { unbuiltTower = null; }

    /**
     * Builds the current unbuilt tower into a {@code FireBall} tower.
     */
    public void buildTower() {
        if (this.unbuiltTower == null) {
            return;
        }

        if (this.initialCost >= this.manaPool.getCurrentMana()) {
            return;
        }

        Tower builtTower = new FireballTower(this, manaPool, this.activeMonsters,
                                                     this.unbuiltTower.getCenterPos().x, 
                                                     this.unbuiltTower.getCenterPos().y,
                                                     initialRange,
                                                     initialFiringSpeed, initialDamage);

        for (int i = 0; i < unbuiltTower.getRangeLevel(); i++) {
            builtTower.upgradeRange();
        }

        for (int i = 0; i < unbuiltTower.getFiringSpeedLevel(); i++) {
            builtTower.upgradeFiringSpeed();
        }

        for (int i = 0; i < unbuiltTower.getDamageLevel(); i++) {
            builtTower.upgradeDamage();
        }
        
        this.towers.add(builtTower);
        this.unbuiltTower = null;
    }

    /**
     * Gets the tower that is on the tile of the given position.
     * @param x 
     * @param y
     * @return The {@code Tower} in range of the given position,
     * otherwise {@code null}.
     */
    public Tower getTowerAtPos(float x, float y) {
        if (towers.size() == 0) {
            return null;
        }

        PVector pos = new PVector(x, y);
        for (Tower t : towers) {
            if (t.getCenterPos().dist(pos) <= App.CELLSIZE / 2) {
                t.setHoveredOver(true);
                return t;
            } else {
                t.setHoveredOver(false);
            }
        }

        return null;
    }

     /**
     * Shows the unbuilt tower at the current position of the cursor.
     * <p>
     * The tower is only shown if the tile underneath the mouse is considered valid.
     * @param x x-position.
     * @param y y-position.
     */
    public void showUnbuiltTowerAtPos(float x, float y) {
        if (this.unbuiltTower == null) {
            return;
        }
        
        this.unbuiltTower.setCenterPos(x, y);
        this.unbuiltTower.show();
    }
}
