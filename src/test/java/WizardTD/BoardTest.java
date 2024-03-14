package WizardTD;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.Map;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import WizardTD.Game.Board.Board;
import WizardTD.Game.Board.Grass;
import WizardTD.Game.Board.Tile;
import processing.core.PApplet;
import processing.core.PVector;

public class BoardTest {
    private static App app;
    private static Board board;

    @BeforeAll
    static void setup() {
        app = new App();
        app.loop();
        PApplet.runSketch(new String[] { "App" }, app);
        app.setup();
        app.delay(1500);
    }

    @BeforeEach
    void setupBoard() {
        board = new Board("level1.txt");
    }

    @Test
    void testAbleToConnectPathsWithoutCrashing() {
        board = new Board("level1.txt");
        board.connectPaths(app);
    }

    @Test
    void testBoard() {
        assertNotNull(board.getLevelTiles());
    }

    @Test
    void testGetWizHouse() {
        assertNotNull(board.getWizHouse());
    }

    @Test
    void testGetClosestTile() {
        for (Map.Entry<PVector, Tile> entry : board.getLevelTiles().entrySet()) {
            Tile t = entry.getValue();
            Tile expectedTile = board.getTileAtPos(t.getPos().x+1, t.getPos().y+1);
            assertEquals(expectedTile.getPos(), t.getPos());
        }
    }

    @Test
    void testGetTileRightOf() {
        Tile leftMostTile = new Grass(0, 40);
        Tile rightMostTile = new Grass(608, 40);

        Tile tileOnMap = board.getTileRightOf(leftMostTile);
        Tile tileOffMap = board.getTileRightOf(rightMostTile);

        assertNull(tileOffMap);
        assertNotNull(tileOnMap);
    }

    @Test
    void testGetTileLeftOf() {
        Tile leftMostTile = new Grass(0, 40);
        Tile rightMostTile = new Grass(608, 40);

        Tile tileOnMap = board.getTileLeftOf(rightMostTile);
        Tile tileOffMap = board.getTileLeftOf(leftMostTile);

        assertNull(tileOffMap);
        assertNotNull(tileOnMap);
    }

    @Test
    void testGetTileUpFrom() {
        Tile topMostTile = new Grass(0, 40);
        Tile bottomMostTile = new Grass(608, 648);

        Tile tileOnMap = board.getTileUpOf(bottomMostTile);
        Tile tileOffMap = board.getTileUpOf(topMostTile);

        assertNull(tileOffMap);
        assertNotNull(tileOnMap);
    }

     @Test
    void testGetTileDownFrom() {
        Tile topMostTile = new Grass(0, 40);
        Tile bottomMostTile = new Grass(608, 648);

        Tile tileOnMap = board.getTileDownOf(topMostTile);
        Tile tileOffMap = board.getTileDownOf(bottomMostTile);

        assertNull(tileOffMap);
        assertNotNull(tileOnMap);
    }

    @Test
    void testCanDrawBoard() {
        board.drawToLayer(app.g);
    }
}
