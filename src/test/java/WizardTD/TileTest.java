package WizardTD;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import WizardTD.Game.Board.Grass;
import WizardTD.Game.Board.Path;
import WizardTD.Game.Board.Shrub;
import WizardTD.Game.Board.Tile;
import WizardTD.Game.Board.WizHouse;
import processing.core.PVector;

public class TileTest {
    private static final PVector TEST_POS = new PVector(5, 5);
    
    @Test
    // Tests that the shrub's position is correctly set on instantiation.
    void testInstantiateShrubWithCorrectPos() {
        Shrub shrub = new Shrub(TEST_POS.x, TEST_POS.y);
        assertEquals(TEST_POS, shrub.getPos(), "Shrubs pos: " + shrub.getPos() + 
                     " does not equal: " + TEST_POS);
    }

    @Test
     // Tests that the WizHouse's position is correctly set on instantiation.
    void testInstantiateWizHouseWithCorrectPos() {
        WizHouse house = new WizHouse(TEST_POS.x, TEST_POS.y);
        assertEquals(TEST_POS, house.getPos(), "WizHouse pos: " + house.getPos() +
                   " does not equal: " + TEST_POS);
    }

    @Test
    // Tests that the grass's position is correctly set on instantiation.
    void testInstantiateGrassWithCorrectPos() {
        Grass grass = new Grass(TEST_POS.x, TEST_POS.y);
        assertEquals(TEST_POS, grass.getPos(), "Grass pos: " + grass.getPos() +
                   " does not equal: " + TEST_POS);
    }

    @Test
    // Tests that the path's position is correctly set on instantiation.
    void testInstantiatePathWithCorrectPos() {
        Path path = new Path(TEST_POS.x, TEST_POS.y);
        assertEquals(TEST_POS, path.getPos(), "Path pos: " + path.getPos() +
                   " does not equal: " + TEST_POS);
    }

    @Test 
    // Tests if the shrub's walkability and placeability are correctly set on instantiation.
    void testInstantiateShrubWithCorrectStatus() {
        Shrub shrub = new Shrub(0, 0);
        boolean expectedWalkable = false;
        boolean expectedPlaceable = false;

        assertTrue(shrub.isPlaceable() == expectedPlaceable, 
                   "The placeability of Shrub should be " + expectedPlaceable);

        assertTrue(shrub.isWalkable() == expectedWalkable, 
                   "The placeability of Shrub should be " + expectedWalkable);
    }

    @Test
    // Tests if the tile super class correctly sets the tile's new pos.
    void testSetPosOfTile() {
        Tile t = new Path(0, 0);
        PVector TEST_POS = new PVector(10, 24);
        t.setPos(TEST_POS.x, TEST_POS.y);

        assertEquals(TEST_POS, t.getPos(), "Tile's pos: " + t.getPos() + 
                     " was not correctly set to: " + TEST_POS);
    }

    @Test
    // Tests that the occupation, walkablitity, and placeability status of a tile is correctly set.
    void testAbleToSetStatusOfTile() {
        Tile t = new Grass(0, 0);

        boolean expectedWalkableStatus = true;
        boolean expectedPlaceableStatus = true;
        boolean expectedOccupiedStatus = true;

        t.setWalkable(expectedWalkableStatus);
        t.setPlaceable(expectedPlaceableStatus);
        t.setOccupied(expectedOccupiedStatus);

        boolean actualWalkableStatus = t.isWalkable();
        boolean actualPlaceableStatus = t.isPlaceable();
        boolean actualOccupiedStatus = t.isOccupied();

        assertEquals(expectedWalkableStatus, actualWalkableStatus);
        assertEquals(expectedPlaceableStatus, actualPlaceableStatus);
        assertEquals(expectedOccupiedStatus, actualOccupiedStatus);
    }
}
