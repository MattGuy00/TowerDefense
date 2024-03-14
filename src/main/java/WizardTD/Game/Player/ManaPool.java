package WizardTD.Game.Player;

import WizardTD.App;
import WizardTD.Game.UI.ManaBar;
import processing.data.JSONObject;

/**
 * Represents the player's mana.
 * 
 */
public final class ManaPool {
    /**
     * By how much the cost of the mana spell
     * increases on use.
     */
    private final float spellCostIncrease;
    /**
     * Used to calculate the increase in mana capacity
     * on mana spell use.
     */
    private final float spellCapMultiplier;

    /**
     * The player's current mana.
     */
    private float currentMana;
    /**
     * The Total mana able to be held.
     */
    private float manaCap;
    /**
     * The amount of mana regenerated per second.
     */
    private float manaPerSec;
    /**
     * The speed at which {@code ManaPool} ticks.
     */
    private float speedMulti = 1;
    /**
     * Used to visually represent {@code ManaPool}
     */
    private ManaBar manaBar;

    /**
     * The current cost of the mana spell.
     */
    private float manaSpellCost;
    /**
     * By how much the mana per second is multiplied
     * on spell use.
     */
    private float spellManaPerSecMultiplier;
    
    /**
     * By how much the mana per second multiplier increases
     * on spell use.
     */
    private float manaPerSecMultiplierIncrease;

    /**
     * {@code ManaPool} constructor.
     * <p>
     * Takes in a json object that is used to initialise 
     * the mana pool's default values.
     * @param config The json object containing the default values.
     */
    public ManaPool(JSONObject config) {
        this.currentMana = config.getFloat("initial_mana");
        this.manaCap = config.getFloat("initial_mana_cap");
        this.manaPerSec = config.getFloat("initial_mana_gained_per_second");

        this.manaSpellCost = config.getFloat("mana_pool_spell_initial_cost");
        this.spellCostIncrease = config.getFloat("mana_pool_spell_cost_increase_per_use");
        this.spellCapMultiplier = config.getFloat("mana_pool_spell_cap_multiplier");
        this.spellManaPerSecMultiplier = config.getFloat("mana_pool_spell_mana_gained_multiplier");
        this.manaPerSecMultiplierIncrease = this.spellManaPerSecMultiplier - 1;
    }

    /**
     * Gets the player's current mana.
     * @return The player's current mana.
     */
    public float getCurrentMana() { return this.currentMana; }
    /**
     * Sets the player's current mana.
     * @param currentMana The player's new current mana.
     */
    public void setCurrentMana(float currentMana) { this.currentMana = currentMana; }

    /**
     * Gets the maximum capacity of the mana pool.
     * @return The mana pool's maximum capacity.
     */
    public float getManaCap() { return this.manaCap; }
    /**
     * Increases the mana pool's maximum capacity by 
     * multiplying the current mana cap with {@code spellCapMultiplier}.
     */
    public void increaseManaCap() {
        this.manaCap *= this.spellCapMultiplier;
        this.manaBar.setMaxMana(this.manaCap);
    }

    /**
     * Sets the mana pool's max capacity to the given parameter.
     * @param manaCap The new max capacity.
     */
    public void setManaCap(float manaCap) { this.manaCap = manaCap; }

    /**
     * Gets the amount of mana gained per second.
     * @return The amount of mana gained per second.
     */
    public float getManaPerSec() { return this.manaPerSec; }
    /**
     * Increases the mana gained per second by 
     * multiplying the current mana per second with
     * {@code spellManaPerSecMultiplier}
     */
    public void increaseManaPerSec() {
        // add rounding to stop float problems
        this.manaPerSec *= this.spellManaPerSecMultiplier;
        this.spellManaPerSecMultiplier += this.manaPerSecMultiplierIncrease;
    }

    /**
     * Sets the mana gained per second to the 
     * given parameter.
     * @param manaPerSec the new mana gained per second.
     */
    public void setManaPerSec(float manaPerSec) { this.manaPerSec = manaPerSec; }
    public void setSpeedMultiplier(float speedMulti) { this.speedMulti = speedMulti; }

    /**
     * Gets the cost of the mana spell.
     * @return The cost of the mana spell.
     */
    public float getManaSpellCost() { return this.manaSpellCost; }
    /**
     * Increases the cost of the mana spell by
     * adding {@code spellCostIncrease} to the
     * current cost.
     */
    public void increaseManaSpellCost() { this.manaSpellCost += this.spellCostIncrease; }

    /**
     * Upgrades the mana pool if the player has enough mana.
     */
    public void upgradeManaPool() {
        if (this.currentMana <= this.manaSpellCost) {
            return;
        }

        this.removeMana(this.manaSpellCost);
        this.increaseManaCap();
        this.increaseManaPerSec();
        this.increaseManaSpellCost();
        this.updateManaBar();
    }

    /**
     * Adds a {@code ManaBar} UI element to the mana pool.
     * <p>
     * {@code ManaPool} can only have one {@code ManaBar} at a time.
     * @param manaBar The new manaBar.
     * @see ManaBar
     */
    public void addManaBar(ManaBar manaBar) {
        this.manaBar = manaBar;
        this.manaBar.setCurrentMana(this.currentMana);
        this.manaBar.setMaxMana(this.manaCap);
    }

    /**
     * Adds mana to the mana pool.
     * @param amount The amount of mana to add.
     */
    public void addMana(float amount) {
        this.currentMana += amount;
    }

    /**
     * Removes mana from the mana pool.
     * <p>
     * If more mana is removed than the mana pool
     * currently has, {@code currentMana} is set to 0.
     * @param amount The amount of mana to remove.
     */
    public void removeMana(float amount) {
        if (amount > this.currentMana) {
            this.currentMana = 0;
            return;
        }
        this.currentMana -= amount;
    }

    /**
     * Executes {@code ManaPool}'s logic
     * <p>
     * This should be called every frame.
     */
    public void tick() {
        updateCurrentMana();
    }

    /**
     * Updates the mana pool's current mana and mana bar.
     * <p> this is where the mana gained per second is added into
     * the mana pool.
     */
    private void updateCurrentMana() {
        if (this.currentMana >= this.manaCap) {
            this.currentMana = this.manaCap;
        } else if (this.currentMana > 0) {
            this.currentMana += (manaPerSec * speedMulti) / App.FPS;
        }
        
        updateManaBar();
    }

    /**
     * Updates the mana pool's mana bar.
     */
    private void updateManaBar() {
        if (this.manaBar != null) {
            this.manaBar.setCurrentMana(this.currentMana);
        }
    }
}
