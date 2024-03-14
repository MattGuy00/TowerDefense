package WizardTD.Game.Board;

import java.util.HashMap;
import java.util.Map;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import WizardTD.App;
import WizardTD.Game.Renderable;
import processing.core.PGraphics;
import processing.core.PVector;

/**
 * Represents the {@code Board} class.
 * <p>
 * A board consists of a 20x20 grid of tiles,
 * with exactly one {@code WizHouse} and a minimum of 
 * one viable path for monsters to follow.
 */
public final class Board implements Renderable {
    /**
     * The maximum number of tiles on this board.
     * <p>
     * Default is 20x20 tiles.
     */
    private final int maxTilesOnBoard = 400;
    /**
     * A hashmap that pairs all tiles on the board with their position.
     */
    private final HashMap<PVector, Tile> levelTiles;

    /**
     * The position of the wizard's house.
     */
    private PVector housePos;

    /**
     * {@code Board}'s constructor.
     * On construction the board is loaded.
     * @param filename The map file's name.
     */
    public Board(String filename) {
        this.levelTiles = new HashMap<PVector, Tile>(this.maxTilesOnBoard);    
        this.loadBoard(filename);
    }

    /**
     * Gets all tiles on the board.
     * @return All tiles on the board.
     */
    public HashMap<PVector, Tile> getLevelTiles() { return levelTiles; }
    /**
     * Gets the wizard's house.
     * @return The wizard's house.
     * @see WizHouse
     */
    public WizHouse getWizHouse() { return (WizHouse)levelTiles.get(housePos); }

    /**
     * Loads the given map file onto the board.
     * @param app Used for rotating images.
     * @param filename The name of the map that is to be loaded onto the board.
     */
    private void loadBoard(String filename) {
        try {
            File mapFile = new File(filename);
            Scanner scn = new Scanner(mapFile);

            String[] layout = new String[20];
            for (int y = App.TOPBAR; y < App.LEVEL_HEIGHT + App.TOPBAR; y += App.CELLSIZE) {
                if (scn.hasNextLine()) {
                    String line = scn.nextLine();
                    layout = line.split("");
                }
                
                for (int x = 0; x < App.LEVEL_WIDTH; x += App.CELLSIZE) {
                    // It's possible that layout may not be the length of the board
                    if (x / App.CELLSIZE < layout.length) {
                        this.addTileToBoard(x, y, layout[x / App.CELLSIZE]);
                    } else {
                        // Turns empty spaces into grass tiles
                        this.addTileToBoard(x, y, "");
                    }
                }      
            }

            scn.close();
        } catch (FileNotFoundException e) {
           e.printStackTrace();
           System.exit(0);
        }
    }

    /**
     * Adds a tile to the board based on the tile type given.
     * @param x The x-position of the tile.
     * @param y The y-position of the tile.
     * @param tileType The type of tile added to the board. Grass by default.
     */
    private void addTileToBoard(int x, int y, String tileType) {
        switch (tileType) {
            case "S": // Shrub
                Shrub s = new Shrub(x, y);
                levelTiles.put(s.getPos(), s);
                break;
            case "X": // Path
                Path p = new Path(x, y);
                levelTiles.put(p.getPos(), p);
                break;
            case "W": // Wizard's house
                WizHouse w = new WizHouse(x, y);
                this.housePos = w.getPos();
                levelTiles.put(w.getPos(), w);
                break;
            default: // Grass
                Grass g = new Grass(x, y);
                levelTiles.put(g.getPos(), g);
        }
    }

    /**
     * Converts all paths on the board into their correct sprite and orientation.
     * @param app Used to rotate the paths.
     */
    public void connectPaths(App app) {
        for (Map.Entry<PVector, Tile> entry : levelTiles.entrySet()) {
            if (!(entry.getValue() instanceof Path)) {
                continue;
            }

            Path p = (Path)entry.getValue();
            
            boolean validLeftPath = getTileLeftOf(p) instanceof Path || getTileLeftOf(p) == null;
            boolean validRightPath = getTileRightOf(p) instanceof Path || getTileRightOf(p) == null;
            boolean validTopPath = getTileUpOf(p) instanceof Path || getTileUpOf(p) == null;
            boolean validBotPath = getTileDownOf(p) instanceof Path || getTileDownOf(p) == null;

            if (validTopPath && validBotPath && validLeftPath && validRightPath) { // Connect all
                p.convertToCross();
                continue;
            }

            // Straight paths
            if (!validTopPath && !validBotPath) { // Left to Right
                continue;
            } else if (!validLeftPath && !validRightPath) { // Top to bot
                p.rotateImage(app, 90);
                continue;
            }
            
            // Bent paths
            if (!validBotPath && !validLeftPath) { // Top to right
                p.convertToBend();
                p.rotateImage(app, 180);
                continue;
            } else if (!validTopPath && !validLeftPath) { // Bot to right
                p.convertToBend();
                p.rotateImage(app, 270);
                continue;
            } else if (!validTopPath && !validRightPath) { // Bot to left
                p.convertToBend();
                continue;
            } else if (!validBotPath && !validRightPath) { // Top to left
                p.convertToBend();
                p.rotateImage(app, 90);
                continue;
            } 

            if (!validBotPath) { // All but left
                p.convertToT();
                p.rotateImage(app, 180);
            } else if (!validTopPath) { // All but top
                p.convertToT();
            } else if (!validRightPath) { // All but right
                p.convertToT();
                p.rotateImage(app, 90);
            } else if (!validLeftPath) { // All but left
                p.convertToT();
                p.rotateImage(app, 270);
            } 
        }
    }

    /**
     * Gets the tile that is under the specified position.
     * @param x x-position.
     * @param y y-position.
     * @return The tile under the specified position.
     */
    public Tile getTileAtPos(float x, float y) {
        for (Map.Entry<PVector, Tile> entry : levelTiles.entrySet()) {
            if (x >= entry.getKey().x && x <= entry.getKey().x + App.CELLSIZE &&
                y >= entry.getKey().y && y <= entry.getKey().y + App.CELLSIZE) {
                return entry.getValue();
            }
        }
        return null;
    }

    /**
     * Gets the tile to the right of given tile.
     * @param t The tile that is checked.
     * @return The tile right of the given tile, {@code null} if there isn't one.
     */
    public Tile getTileRightOf(Tile t) {
        PVector rightPos = new PVector(t.getPos().x + App.CELLSIZE, t.getPos().y);
        return levelTiles.get(rightPos);
    }

    /**
     * Gets the tile to the left of given tile.
     * @param t The tile that is checked.
     * @return The tile left of the given tile, {@code null} if there isn't one.
     */
    public Tile getTileLeftOf(Tile t) {
        PVector leftPos = new PVector(t.getPos().x - App.CELLSIZE, t.getPos().y);
        return levelTiles.get(leftPos);
    }

     /**
     * Gets the tile up from the given tile.
     * @param t The tile that is checked.
     * @return The tile up from the given tile, {@code null} if there isn't one.
     */
    public Tile getTileUpOf(Tile t) {
        PVector upPos = new PVector(t.getPos().x, t.getPos().y - App.CELLSIZE);
        return levelTiles.get(upPos);
    }

     /**
     * Gets the tile below the given tile.
     * @param t The tile that is checked.
     * @return The tile below the given tile, {@code null} if there isn't one.
     */
    public Tile getTileDownOf(Tile t) {
        PVector downPos = new PVector(t.getPos().x, t.getPos().y + App.CELLSIZE);
        return levelTiles.get(downPos);
    }

    public void drawToLayer(PGraphics layer) {
        for (Map.Entry<PVector, Tile> entry : levelTiles.entrySet()) {
            Tile tile = entry.getValue();
            tile.drawToLayer(layer);
        }   

        Grass gr = new Grass(this.getWizHouse().getPos().x, this.getWizHouse().getPos().y);
        gr.drawToLayer(layer);
    }
    
}
