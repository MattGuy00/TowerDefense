package WizardTD;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.LinkedList;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import WizardTD.Game.Entities.Monsters.Gremlin;
import WizardTD.Game.Entities.Monsters.Monster;
import WizardTD.Game.Entities.Towers.FireballTower;
import WizardTD.Game.Entities.Towers.Tower;
import WizardTD.Game.Entities.Towers.TowerManager;
import WizardTD.Game.Player.ManaPool;
import processing.core.PApplet;
import processing.core.PVector;
import processing.data.JSONObject;

public class TowerTest {
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

    private static FireballTower fireballTower;
    private static PVector fireballTowerPos = new PVector(100, 100);

    private static App app;

    @BeforeAll
    static void setup() {
        app = new App();
        app.loop();
        PApplet.runSketch(new String[] { "App" }, app);
        app.setup();
        app.delay(1500);
        
        testMonsters = new LinkedList<>();
        Gremlin.loadSprites(app);
        testMonsters.add(new Gremlin(100, 1, 0, 20));
    }

    @BeforeEach
    void setupTower() {
        manaPool = new ManaPool(CONFIG_FILE);
        
        towerManager = new TowerManager(CONFIG_FILE, manaPool, testMonsters);
        float initialRange = CONFIG_FILE.getFloat("initial_tower_range");
        float initialFiringSpeed = CONFIG_FILE.getFloat("initial_tower_firing_speed");
        float initialDamage = CONFIG_FILE.getFloat("initial_tower_damage");
        fireballTower = new FireballTower(towerManager, manaPool, testMonsters,
                                          fireballTowerPos.x, fireballTowerPos.y,
                                          initialRange, initialFiringSpeed, initialDamage);
    }

    @Test
    // Tests that the tower correctly acquires a target.
    void testTowerAcquiresTarget() {
        testMonsters.get(0).setCenterPos(fireballTowerPos.x + 10, 
                                         fireballTowerPos.y + 10);

        fireballTower.tick();
        fireballTower.tick();
        assertTrue(fireballTower.getFireballs().size() > 0);

        Monster fireballTarget = fireballTower.getFireballs().get(0).getTarget();
        assertNotNull(fireballTarget);
    }

    @Test
    // Tests that the tower does not shoot when there are no targets.
    void testTowerHasNoTarget() {
        fireballTower.tick();
        assertTrue(fireballTower.getFireballs().size() == 0);
    }

    @Test
    // Tests that the tower's speed multiplier changes.
    void testTowerCanChangeSpeed() {
        testMonsters.get(0).setCenterPos(fireballTowerPos.x + 10, 
                                         fireballTowerPos.y + 10);
        fireballTower.tick();

        float expectedSpeed = 3;
        fireballTower.setSpeedMultiplier(expectedSpeed);
        assertEquals(expectedSpeed, fireballTower.getSpeedMultiplier());
    }

    @Test
    // Basic test to check that the tower draws without crashing.
    void testTowerDrawsWithoutCrashing() {
        fireballTower.drawToLayer(app.g);
    }

    @Test
    // Basic test to check that the tower draws its range indicator without crashing.
    void testTowerCanDrawrRangeIndicatorWithoutCrashing() {
        fireballTower.drawRangeIndicator(app.g);

        fireballTower.setHoveredOver(true);
        fireballTower.drawRangeIndicator(app.g);
    }

    @Test
    // Basic test to check that the tower draws its upgrades without crashing.
    void testTowerCanDrawUpgradesWithoutCrashing() {
        for (int i = 0; i < 21; i++) {
            fireballTower.upgradeDamage();
            fireballTower.upgradeFiringSpeed();
            fireballTower.upgradeRange();
        }

        fireballTower.drawUpgrades(app.g);
    }

    @Test
    // Tests that a tower removes mana from the mana pool on instantiation.
    void testFireballTowerRemovesManaOnInstantiation() {
        float manaBeforeTowerBuilt = manaPool.getCurrentMana();

        towerManager.initialiseUnbuiltTower();
        towerManager.buildTower();
        float manaAfterTowerBuilt = manaPool.getCurrentMana();

        assertNotEquals(manaBeforeTowerBuilt, manaAfterTowerBuilt);
    }
    
    @Test
    // Tests that the tower's range is correctly ugraded.
    void testTowerUpgradesRange() {
        towerManager.initialiseUnbuiltTower();
        towerManager.buildTower();
        float manaBeforeUpgrade = manaPool.getCurrentMana();

        Tower tower = towerManager.getTowers().peek();
        float rangeBeforeUpgrade = tower.getRange();
        tower.upgradeRange();
        float manaAfterUpgrade = manaPool.getCurrentMana();
        float rangeAfterUpgrade = tower.getRange();

        assertTrue(manaAfterUpgrade < manaBeforeUpgrade);
        assertTrue(rangeAfterUpgrade > rangeBeforeUpgrade);

    }

    @Test
     // Tests that the tower's range level is correctly ugraded.
    void testTowerUpgradesRangeLevel() {
        float initialRangeLevel = fireballTower.getRangeLevel();
        fireballTower.upgradeRange();
        assertTrue(fireballTower.getRangeLevel() > initialRangeLevel);
    }

    @Test
    // Tests that the tower's firing speed is correctly ugraded.
    void testTowerUpgradesFiringSpeed() {
        float initialFiringSpeed = fireballTower.getFiringSpeed();
        float manaBeforeUpgrade = manaPool.getCurrentMana();

        fireballTower.upgradeFiringSpeed();
        float firingSpeedAfterUpgrade = fireballTower.getFiringSpeed();
        float manaAfterUpgrade = manaPool.getCurrentMana();
        
        assertTrue(manaAfterUpgrade < manaBeforeUpgrade);
        assertTrue(firingSpeedAfterUpgrade > initialFiringSpeed);
    }
    
    @Test
    // Tests that the tower's firing speed level is correctly ugraded.
    void testTowerUpgradesFiringSpeedLevel() {
        float initialFiringSpeedLevel = fireballTower.getFiringSpeedLevel();
        fireballTower.upgradeFiringSpeed();
        float firingSpeedLevelAfterUpgrade = fireballTower.getFiringSpeedLevel();
        assertTrue(firingSpeedLevelAfterUpgrade > initialFiringSpeedLevel);
    }

    @Test
    // Tests that the tower's frames betwween each shot is correct.
    void testFramesBetweenEachShotIsCorrect() {
        float initialFiringSpeed = CONFIG_FILE.getFloat("initial_tower_firing_speed");
        float expectedFramesBetweenShots = App.FPS / initialFiringSpeed;
        float actualFramesBetweenShots = fireballTower.getFramesBetweenEachShot();
        assertEquals(expectedFramesBetweenShots, actualFramesBetweenShots);
    }

    @Test
    // Tests that the tower's damage is correctly ugraded.
    void testTowerUpgradesDamage() {
        float expectedDamageIncrease = fireballTower.getDamage() + 
                                       CONFIG_FILE.getFloat("initial_tower_damage") / 2;
        float expectedMana = manaPool.getCurrentMana() - 
                             towerManager.getInitialUpgradeCost();

        fireballTower.upgradeDamage();
        float actualDamageIncrease = fireballTower.getDamage();
        float actualMana = manaPool.getCurrentMana();

        assertEquals(expectedDamageIncrease, actualDamageIncrease);
        assertEquals(expectedMana, actualMana);
    }

    @Test
    // Tests that the tower's damage level is correctly ugraded.
    void testTowerUpgradesDamageLevel() {
        float expectedLevelIncrease = fireballTower.getDamageLevel() + 1;
        fireballTower.upgradeDamage();
        float actualLevelIncrease = fireballTower.getDamageLevel();

        assertEquals(expectedLevelIncrease, actualLevelIncrease);
    }

    @Test
    // Tests that the tower's level increases to 2 when possible.
    void testTowerCanLevelUpToTwo() {
        int expectedTowerLevel = fireballTower.getTowerLevel() + 1;
        fireballTower.upgradeRange();
        fireballTower.upgradeFiringSpeed();
        fireballTower.upgradeDamage();
        fireballTower.upgradeTowerIfPossible();

        int actualTowerLevel = fireballTower.getTowerLevel();
        assertEquals(expectedTowerLevel, actualTowerLevel);
    }

    @Test
    // Tests that the tower's level increases to 3 when possible.
    void testTowerCanLevelUpToThree() {
        int expectedTowerLevel = fireballTower.getTowerLevel() + 2;
        fireballTower.upgradeRange();
        fireballTower.upgradeFiringSpeed();
        fireballTower.upgradeDamage();
        fireballTower.upgradeTowerIfPossible();

        fireballTower.upgradeRange();
        fireballTower.upgradeFiringSpeed();
        fireballTower.upgradeDamage();
        fireballTower.upgradeTowerIfPossible();

        int actualTowerLevel = fireballTower.getTowerLevel();
        assertEquals(expectedTowerLevel, actualTowerLevel);
    }

    @Test
    // Tests that the tower's level doesn't increase past level 3.
    void testTowerLevelCapsAtThree() {
        int expectedTowerLevel = fireballTower.getTowerLevel() + 2;
        for (int i = 0; i < 10; i++) {
            fireballTower.upgradeRange();
            fireballTower.upgradeFiringSpeed();
            fireballTower.upgradeDamage();
            fireballTower.upgradeTowerIfPossible();
        }

        int actualTowerLevel = fireballTower.getTowerLevel();
        assertEquals(expectedTowerLevel, actualTowerLevel);
    }

    @Test
    // Tests that the tower's upgrade costs correctly increase.
    void testTowerUpgradeCostIncreases() {
        float expectedRangeUpgradeCost = fireballTower.getRangeUpgradeCost() +
                                         towerManager.getCostIncreasePerLevel();
        float expectedFiringSpeedUpgradeCost = fireballTower.getFiringSpeedUpgradeCost() +
                                               towerManager.getCostIncreasePerLevel();
        float expectedDamageUpgradeCost = fireballTower.getDamageUpgradeCost() + 
                                          towerManager.getCostIncreasePerLevel();
        
        fireballTower.upgradeRange();
        fireballTower.upgradeFiringSpeed();
        fireballTower.upgradeDamage();

        float actualRangeUpgradeCost = fireballTower.getRangeUpgradeCost();
        float actualFiringSpeedUpgradeCost = fireballTower.getFiringSpeedUpgradeCost();
        float actualDamageUpgradeCost = fireballTower.getDamageUpgradeCost();

        assertEquals(expectedRangeUpgradeCost, actualRangeUpgradeCost);
        assertEquals(expectedFiringSpeedUpgradeCost, actualFiringSpeedUpgradeCost);
        assertEquals(expectedDamageUpgradeCost, actualDamageUpgradeCost);
    }

    @Test
    // Tests that the tower's firing speed level is correctly set.
    void testAbleToSetFiringSpeedlevel() {
        int expectedLevel = 15;
        fireballTower.setFiringSpeedLevel(expectedLevel);
        int actualLevel = fireballTower.getFiringSpeedLevel();

        assertEquals(expectedLevel, actualLevel);
    }

    @Test
    // Tests that the tower's damage level is correctly set.
    void testAbleToSetDamagelevel() {
        int expectedLevel = 15;
        fireballTower.setDamageLevel(expectedLevel);
        int actualLevel = fireballTower.getDamageLevel();

        assertEquals(expectedLevel, actualLevel);
    }

    @Test 
    // Tests that the tower won't upgrade its range when mana is low.
    void testUnableToUpgradeTowerRange() {
        manaPool.setCurrentMana(0);
        float expectedRangeLevel = fireballTower.getRangeLevel();

        fireballTower.upgradeRange();
        float actualRangeLevel = fireballTower.getRangeLevel();
        assertEquals(expectedRangeLevel, actualRangeLevel);
    }

    @Test 
    // Tests that the tower won't upgrade its firing speed when mana is low.
    void testUnableToUpgradeTowerFiringSpeed() {
        manaPool.setCurrentMana(0);
        float expectedFiringSpeedLevel = fireballTower.getFiringSpeedLevel();

        fireballTower.upgradeFiringSpeed();
        float actualFiringSpeedLevel = fireballTower.getFiringSpeedLevel();
        assertEquals(expectedFiringSpeedLevel, actualFiringSpeedLevel);
    }

    @Test 
    // Tests that the tower won't upgrade its damage when mana is low.
    void testUnableToUpgradeTowerDamage() {
        manaPool.setCurrentMana(0);
        float expectedDamageLevel = fireballTower.getDamageLevel();

        fireballTower.upgradeDamage();
        float actualDamageLevel = fireballTower.getDamageLevel();
        assertEquals(expectedDamageLevel, actualDamageLevel);
    }
}

