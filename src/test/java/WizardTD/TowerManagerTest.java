package WizardTD;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.LinkedList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import WizardTD.Game.Entities.Monsters.Monster;
import WizardTD.Game.Entities.Towers.PlaceholderTower;
import WizardTD.Game.Entities.Towers.Tower;
import WizardTD.Game.Entities.Towers.TowerManager;
import WizardTD.Game.Player.ManaPool;
import processing.data.JSONObject;


public class TowerManagerTest  {
    private static final String CONFIG_TEXT = "{ \"initial_tower_range\": 96,\r\n" + //
                                    "  \"initial_tower_firing_speed\": 1.5, \r\n" + //
                                    "  \"initial_tower_damage\": 100,\r\n" + //
                                    "  \"initial_mana\": 141,\r\n" + //
                                    "  \"initial_mana_cap\": 1000,\r\n" + //
                                    "  \"initial_mana_gained_per_second\": 2,\r\n" + //
                                    "  \"tower_cost\": 0,\r\n" + //
                                    "  \"mana_pool_spell_initial_cost\": 100,\r\n" + //
                                    "  \"mana_pool_spell_cost_increase_per_use\": 150,\r\n" + //
                                    "  \"mana_pool_spell_cap_multiplier\": 1.5,\r\n" + //
                                    "  \"mana_pool_spell_mana_gained_multiplier\": 1.1 }";

    private static final JSONObject CONFIG_FILE = JSONObject.parse(CONFIG_TEXT);
    private static ManaPool manaPool;
    private static LinkedList<Monster> testMonsters;

    private static TowerManager towerManager;
    private static Tower testTower;

    @BeforeEach
    void setup() {
        manaPool = new ManaPool(CONFIG_FILE);
        testMonsters = new LinkedList<>();
        towerManager = new TowerManager(CONFIG_FILE, manaPool, testMonsters);

        towerManager.initialiseUnbuiltTower();
        towerManager.buildTower();
        testTower = towerManager.getTowers().peek();
    }
    

    @Test
    // Tests that the TowerManager constructor does not except null objects.
    void testThrowsNullException() {
        String expectedMessage = "TowerManager must be constructed with non-null objects";

        Exception missingMonsters = assertThrows(NullPointerException.class,
                                           () -> {new TowerManager(CONFIG_FILE, manaPool, null); });
        assertTrue(missingMonsters.getMessage().contains(expectedMessage));

        Exception missingManaPool = assertThrows(NullPointerException.class,
                                           () -> {new TowerManager(CONFIG_FILE, null, testMonsters); });
        assertTrue(missingManaPool.getMessage().contains(expectedMessage));

        Exception missinConfig = assertThrows(NullPointerException.class,
                                           () -> {new TowerManager(null, manaPool, testMonsters); });
        assertTrue(missinConfig.getMessage().contains(expectedMessage));
    }
    
    @Test
    // Tests that the initial range set in TowerManager is correct.
    void testRangeIsSet() {
        float expectedRange = CONFIG_FILE.getFloat("initial_tower_range");
        assertEquals(expectedRange, testTower.getRange());
    }

    @Test
    // Tests that the initial firing speed set in TowerManager is correct.
    void testFiringSpeedIsSet() {
        float expectedFiringSpeed = CONFIG_FILE.getFloat("initial_tower_firing_speed");
        assertEquals(expectedFiringSpeed, testTower.getFiringSpeed());
    }

    @Test
    // Tests that the initial damage set in TowerManager is correct.
    void testDamageIsSet() {
        float expectedDamage = CONFIG_FILE.getFloat("initial_tower_damage");
        assertEquals(expectedDamage, testTower.getDamage());
    }

    @Test
    // Tests that the initial tower cost is correct.
    void testInitialCostIsSet() {
        float expectedTowerCost = CONFIG_FILE.getFloat("tower_cost");
        assertEquals(expectedTowerCost, towerManager.getBaseTowerCost());
    }

    @Test
    // Tests that getCostIncreasePerLevel() returns a value.
    void testCostIncreasePerLevelNotNull() {
        assertNotNull(towerManager.getCostIncreasePerLevel());
    }

    @Test
    // Tests that getInitialUpgradeCost() returns a value.
    void testCostInitialUpgradeCostNotNull() {
        assertNotNull(towerManager.getInitialUpgradeCost());
    }

    @Test
    // Tests that the range increase per upgrade is the size of a tile.
    void testRangeIncreasePerUpgrade() {
        float expectedIncrease = App.CELLSIZE;
        assertEquals(expectedIncrease, towerManager.getRangeIncreasePerUpgrade());
    }

    @Test
    // Tests that the firing speed increase per upgrade is correct.
    void testFiringSpeedPerUpgrade() {
        float expectedIncrease = 0.5f;
        assertEquals(expectedIncrease, towerManager.getFiringSpeedIncreasePerUpgrade());
    }

    @Test
    // tests that the damage increase per upgrade is half the initial damagefrom the config.
    void testDamageIncreasePerUpgrade() {
        float initialDamage = CONFIG_FILE.getFloat("initial_tower_damage");
        float expectedDamageIncreasePerUpgrade = initialDamage / 2;
        assertEquals(expectedDamageIncreasePerUpgrade, towerManager.getDamageIncreasePerUpgrade());
    }

    @Test
    // Tests that the tower build status changes correctly.
    void testTowerBeingBuiltStatus() {
        boolean defaultValue = towerManager.isTowerBeingBuilt();
        towerManager.initialiseUnbuiltTower();

        assertTrue(defaultValue != towerManager.isTowerBeingBuilt());
    }

    @Test
    // Tests that the speed multiplier for a tower is correctly set.
    void testSpeedMultiplier() {
        float expectedSpeed = 2.3f;
        towerManager.setSpeedOfTowers(expectedSpeed);

        for (Tower t : towerManager.getTowers()) {
            assertEquals(expectedSpeed, t.getSpeedMultiplier());
        }
    }

    @Test
    // Tests that the cost to build a tower is correct.
    void testCostToBuildTower() {
        float expectedInitialCost = CONFIG_FILE.getFloat("tower_cost");
        assertEquals(expectedInitialCost, towerManager.getCostToBuildTower());

        // Increase the cost of the unbuilt tower by upgrading it
        towerManager.initialiseUnbuiltTower();
        towerManager.getUnbuiltTower().upgradeRange();
        assertNotEquals(expectedInitialCost, towerManager.getCostToBuildTower());
    }

    @Test
    // Tests that getUnbuiltTower() returns the correct values.
    void testGetUnbuiltTower() {
        assertNull(towerManager.getUnbuiltTower());

        towerManager.initialiseUnbuiltTower();
        assertNotNull(towerManager.getUnbuiltTower());
    }

    @Test
    // Tests that the unbuilt tower is of PlaceHolder type.
    void testUnbuiltTowerIsPlaceHolder() {
        towerManager.initialiseUnbuiltTower();
        assertTrue(towerManager.getUnbuiltTower() instanceof PlaceholderTower);
    }

    @Test
    // Tests that unbuilt tower is set back to null when deactivated.
    void testAbleToDeactivateUnbuiltTower() {
        towerManager.initialiseUnbuiltTower();
        Tower initialisedUnbuiltTower = towerManager.getUnbuiltTower();

        towerManager.deactivateUnbuiltTower();
        assertNotEquals(initialisedUnbuiltTower, towerManager.getUnbuiltTower());
    }

    @Test
    // Tests that tower's are able to be built
    void testTAbleToBuildTower() {
        int initialAmountOfTowers = towerManager.getTowers().size();

        towerManager.initialiseUnbuiltTower();
        towerManager.buildTower();

        int actualAmountOfTowers = towerManager.getTowers().size();

        assertNotEquals(initialAmountOfTowers, actualAmountOfTowers);
        assertNull(towerManager.getUnbuiltTower());

      
    }

    @Test
    // Tests that the the upgrades on the unbuilt tower transfers to
    // the new tower when built.
    void testTowerUpgradesTransferOnBuild() {
        towerManager.initialiseUnbuiltTower();
        Tower unbuiltTower = towerManager.getUnbuiltTower();

        towerManager.buildTower();
        Tower builtTower = towerManager.getTowers().peek();

        assertEquals(unbuiltTower.getRangeLevel(), builtTower.getRangeLevel());
        assertEquals(unbuiltTower.getFiringSpeedLevel(), builtTower.getFiringSpeedLevel());
        assertEquals(unbuiltTower.getDamageLevel(), builtTower.getDamageLevel());
    }

    @Test
    // Tests that it correctly gets the closest tower.
    void testGetClosestTower() {
        Tower defaultClosest = towerManager.getTowerAtPos(0, 0);
        assertNull(defaultClosest);

        towerManager.initialiseUnbuiltTower();
        towerManager.buildTower();
        Tower expectedTower = towerManager.getTowers().peek();

        Tower actualTower = towerManager.getTowerAtPos(expectedTower.getCenterPos().x,
                                                         expectedTower.getCenterPos().y);

        assertEquals(expectedTower, actualTower);

        Tower noTower = towerManager.getTowerAtPos(3, 2);
        assertNotEquals(expectedTower, noTower);
    }

    
}
