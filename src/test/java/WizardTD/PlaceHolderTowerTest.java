package WizardTD;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.LinkedList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import WizardTD.Game.Entities.Monsters.Monster;
import WizardTD.Game.Entities.Towers.PlaceholderTower;
import WizardTD.Game.Entities.Towers.TowerManager;
import WizardTD.Game.Player.ManaPool;
import processing.core.PVector;
import processing.data.JSONObject;

public class PlaceHolderTowerTest {
    private static final String CONFIG_TEXT = "{ \"initial_tower_range\": 96,\r\n" + //
                                    "  \"initial_tower_firing_speed\": 1.5, \r\n" + //
                                    "  \"initial_tower_damage\": 100,\r\n" + //
                                    "  \"initial_mana\": 100000,\r\n" + //
                                    "  \"initial_mana_cap\": 100000,\r\n" + //
                                    "  \"initial_mana_gained_per_second\": 2,\r\n" + //
                                    "  \"tower_cost\": 10,\r\n" + //
                                    "  \"mana_pool_spell_initial_cost\": 100,\r\n" + //
                                    "  \"mana_pool_spell_cost_increase_per_use\": 150,\r\n" + //
                                    "  \"mana_pool_spell_cap_multiplier\": 1.5,\r\n" + //
                                    "  \"mana_pool_spell_mana_gained_multiplier\": 1.1 }";

    private static final JSONObject CONFIG_FILE = JSONObject.parse(CONFIG_TEXT);
    private static ManaPool manaPool;
    private static LinkedList<Monster> testMonsters;
    private static TowerManager towerManager;

    private static PlaceholderTower placeholderTower;
    private static PVector towerPos = new PVector(100, 100);

    @BeforeEach
    void setup() {
        manaPool = new ManaPool(CONFIG_FILE);
        testMonsters = new LinkedList<>();
        towerManager = new TowerManager(CONFIG_FILE, manaPool, testMonsters);
        float initialRange = CONFIG_FILE.getFloat("initial_tower_range");
        placeholderTower = new PlaceholderTower(towerManager, towerPos.x, towerPos.y, initialRange);
    }

    @Test
    void testPlaceHolderTowerDoesntRemoveMana() {
        float expectedMana = CONFIG_FILE.getFloat("initial_mana");
        float actualMana = manaPool.getCurrentMana();

        assertEquals(expectedMana, actualMana);
    }

    @Test
    void testUpgradePlaceHolderTowerRange() {
        float expectedRange = placeholderTower.getRange() +
                              towerManager.getRangeIncreasePerUpgrade();
        float expectedRangeLevel = placeholderTower.getRangeLevel() + 1;
        float expectedBuildCost = towerManager.getBaseTowerCost() + 
                                  placeholderTower.getRangeUpgradeCost();
        float expectedMana = manaPool.getCurrentMana();

        placeholderTower.upgradeRange();

        float actualRange = placeholderTower.getRange();
        float actualRangeLevel = placeholderTower.getRangeLevel();
        float actualBuildCost = placeholderTower.getBuildCost();
        float actualMana = manaPool.getCurrentMana();

        assertEquals(expectedRange, actualRange);
        assertEquals(expectedRangeLevel, actualRangeLevel);
        assertEquals(expectedBuildCost, actualBuildCost);
        assertEquals(expectedMana, actualMana);
    }

    @Test
    void testDowngradePlaceHolderTowerRange() {
        placeholderTower.upgradeRange();

        float expectedRange = placeholderTower.getRange() -
                              towerManager.getRangeIncreasePerUpgrade();
        float expectedRangeLevel = placeholderTower.getRangeLevel() - 1;
        float expectedBuildCost = placeholderTower.getBuildCost() - 
                                  towerManager.getInitialUpgradeCost();

        placeholderTower.downgradeRange();

        float actualRange = placeholderTower.getRange();
        float actualRangeLevel = placeholderTower.getRangeLevel();
        float actualBuildCost = placeholderTower.getBuildCost();

        assertEquals(expectedRange, actualRange);
        assertEquals(expectedRangeLevel, actualRangeLevel);
        assertEquals(expectedBuildCost, actualBuildCost);
    }
}
