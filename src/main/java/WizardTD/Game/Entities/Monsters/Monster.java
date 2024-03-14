package WizardTD.Game.Entities.Monsters;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Random;

import WizardTD.App;
import WizardTD.Game.Renderable;
import WizardTD.Game.Board.Board;
import WizardTD.Game.Board.Path;
import WizardTD.Game.Board.Tile;
import WizardTD.Game.Entities.Entity;
import WizardTD.Game.UI.HealthBar;
import processing.core.PGraphics;
import processing.core.PImage;
import processing.core.PVector;

/**
 * Represents the monster abstract base class.
 * <p>
 * A {@code Monster}'s goal is to reach the wizard's house by following
 * the shortest walkable path. This path is chosen at random when
 * the monster is constructed.
 */
public class Monster extends Entity implements Renderable {
    /**
     * An ArrayList of ArrayLists that hold
     * a valid path for the monster to follow.
     */
    private static ArrayList<ArrayList<Tile>> paths = new ArrayList<>();

    /**
     * The path that the monster will follow.
     */
    private ArrayList<Tile> path;

    /**
     * The monster's current sprite.
     */
    private PImage currentSprite;
    /**
     * The monster's death animation images.
     */
    private PImage[] deathAnimImages = new PImage[5];
    
    /**
     * The monster's health bar.
     */
    private HealthBar healthBar;

    /**
     * The current health points of the monster.
     */
    private float currentHP;
    /**
     * The base speed of the monster.
     */
    private float speed;
   
    /**
     * The monster's armour.
     */
    private float armour;
    /**
     * the amount of mana gained by the player on this monster's death.
     */
    private float manaOnDeath;

    /**
     * An iterator used for setting the monster's current destination.
     */
    private int moveIter = 0;
    /**
     * For the monster's death animation.
     */
    private int currentFrame = 0;
    /**
     * The current image of the death animation.
     */
    private int deathImageIter = 0;
    /**
     * The tile that the monster starts at.
     */
    private Tile startingTile;
    /**
     * The tile that the monster is moving towards.
     */
    private Tile currentDest;
    /**
     * For tracking if the monster's death animation has finished.
     */
    private boolean isDead = false;

    /**
     * Monster's constructor.
     * <p>
     * This constructor should only be used by this class's child classes.
     * <p>
     * A sprite must be set otherwise the program will crash.
     * @param initialHP The monster's starting health points.
     * @param speed the monster's default speed.
     * @param armour The monster's default speed.
     * @param manaOnDeath The amount of dropped on death by the monster.
     * @throws IllegalArgumentException If initialHP param is <= 0, 
     * or if any of the other params are < 0.
     */
    public Monster(float initialHP, float speed, float armour, int manaOnDeath) throws IllegalArgumentException {
        if (initialHP <= 0) throw new IllegalArgumentException("initial HP must be > 0.");
        if (speed <= 0) throw new IllegalArgumentException("Initial speed must be > 0.");
        if (armour < 0) throw new IllegalArgumentException("Armour must be >= 0.");
        if (manaOnDeath < 0) throw new IllegalArgumentException("Mana on death must be >= 0.");

        Random rnd = new Random();
        // gets a random path for the monster to follow
        this.path = paths.get(rnd.nextInt(paths.size()));

      
        this.startingTile = this.getPath().get(0);

        this.setCenterPos(this.getStartingTile().getCenterPos().x,
                          this.getStartingTile().getCenterPos().y);

        this.currentHP = initialHP;
        this.speed = speed;
        this.armour = armour;
        this.manaOnDeath = manaOnDeath;
    }

    /**
     * Gets the monster's current sprite.
     * @return The monster's current sprite.
     */
    public PImage getCurrentSprite() { return this.currentSprite; }
    /**
     * Changes the monster's sprite.
     * @param sprite The monster's new sprite.
     */
    public void setCurrentSprite(PImage sprite) { this.currentSprite = sprite; }

    /**
     * Gets the iterator for the death animation.
     * @return The iterator for the death animation.
     */
    public int getDeathImageiter() { return this.deathImageIter; }

    /**
     * Gets the monster's death animation images.
     * @return The monster's death animation images.
     */
    public PImage[] getDeathAnimImages() { return this.deathAnimImages; }
    /**
     * Sets the PImage array of death images,
     * for the monster's death animation.
     * @param images the images to be used in the death animation.
     */
    public void setDeathAnimImages(PImage[] images) { this.deathAnimImages = images; }

    /**
     * Gets the path that the monster is following.
     * @return The path that the monster is following.
     */
    public ArrayList<Tile> getPath() { return this.path; }
    /**
     * Sets the path that the monster will follow.
     * @param path The new path for monster to follow.
     */
    public void setPath(ArrayList<Tile> path) { this.path = path; }

    /**
     * Gets the monster's move iterator.
     * @return The monster's move iterator.
     */
    public int getMoveIter() { return this.moveIter; }
    /**
     * Sets the monster's move iterator.
     * @param iter The new iterator.
     */
    public void setMoveIter(int iter) { this.moveIter = iter; }
       
    /**
     * Gets the monster's health bar.
     * @return The monster's health bar.
     */
    public HealthBar getHealthBar() { return this.healthBar; }
    /**
     * Sets the monster's health bar.
     * <p>
     * The monster can only have one health bar.
     * @param healthBar the new health bar.
     * @see HealthBar
     */
    public void setHealthBar(HealthBar healthBar) { this.healthBar = healthBar; }
    
    /**
     * Gets the monster's current health points.
     * @return The monster's current health points.
     */
    public float getCurrentHP() { return this.currentHP; }
    /**
     * Sets the monster's current health points.
     * @param hp the new monster health points.
     */
    public void setCurrentHP(float hp) { this.currentHP = hp; }
    
    /**
     * Gets the base speed of the monster.
     * @return The base speed of the monster.
     */
    public float getSpeed() { return this.speed; }
    /**
     * Sets the base speed of the monster.
     * @param speed the monster's new base speed.
     */
    public void setSpeed(float speed) { this.speed = speed; }
     
    /**
     * Gets the monster's armour.
     * @return The monster's armour.
     */
    public float getArmour() { return this.armour; }
    /**
     * Sets the monster's armour.
     * @param armour The monster's new armour.
     */
    public void setArmour(float armour) { this.armour = armour; }
    
    /**
     * Gets the amount of mana dropped on death by the monster.
     * @return The amount of mana dropped on death by the monster.
     */
    public float getManaOnDeath() { return this.manaOnDeath; }
    /**
     * Sets the amount of mana dropped on death by the monster.
     * @param manaOnDeath the new amount of mana dropped on death.
     */
    public void setManaOnDeath(float manaOnDeath) { this.manaOnDeath = manaOnDeath; }

    /**
     * Gets the tile that the monster started on.
     * @return The tile that the monster started on.
     */
    public Tile getStartingTile() { return this.startingTile; }
    /**
     * Sets the monster's starting tile.
     * @param t The tile that the monster starts on.
     */
    public void setStartingTile(Tile t) { this.startingTile = t; }
    
    /**
     * Gets the tile that the monster is currently moving towards.
     * @return The tile that the monster is currently moving towards.
     */
    public Tile getCurrentDest() { return this.currentDest; }
    /**
     * Sets the tile that the monster is currently moving towards.
     * @param dest the new tile destination.
     */
    public void setCurrentDest(Tile dest) { this.currentDest = dest; }
    
    /**
     * Checks if the monster has played its death animation.
     * @return {@code true} if the monster has finished its death animation,
     * otherwise {@code false}
     */
    public boolean isDead() { return this.isDead; }
    /**
     * Sets the death status of the monster.
     * @param dead if the monster is dead or not.
     */
    public void setDead(boolean dead) { this.isDead = dead; }

    /**
     * Checks if the monster has any health points left.
     * @return true if the monster's health is > 0,
     * otherwise returns false.
     */
    public boolean isAlive() { return this.currentHP > 0; }
    /**
     * Kills the monster by setting its health points to 0.
     */
    public void kill() { this.currentHP = 0; }
    /**
     * Adds health points to the monster's current health.
     * @param hp thr amount of health points to add.
     */
    public void addHP(float hp) { this.currentHP += hp; }
    /**
     * Removes health points from the monster.
     * <p>
     * The amount of health points removed depends on the monster's
     * armour.
     * @param damageTaken The amount of damage taken.
     */
    public void removeHP(float damageTaken) { this.currentHP -= damageTaken * (1 - this.armour); }
    
    /**
     * Moves the monster towards the current destination. 
     * Once that destination has been reached, the monster gets the next
     * destination and so on, until it reaches the wizard's house.
     */
    public void move() {
        if (this.moveIter >= path.size()) {
            return;
        }

        this.currentDest = path.get(this.moveIter);
        if (this.getCenterPos().dist(this.currentDest.getCenterPos()) <= speed * this.getSpeedMultiplier()) {
            this.setCenterPos(this.currentDest.getCenterPos().x, this.currentDest.getCenterPos().y);
            ++this.moveIter;
            return;
        }
        this.moveTowardsDest();
    }

    /**
     * Moves the monster a certain distance each frame,
     * depending on its base speed and speed multiplier.
     */
    public void moveTowardsDest() {
        // Finds the direction in which the monster should move, then moves it
        float yDiff = this.getCenterPos().y - this.currentDest.getCenterPos().y;
        float xDiff = this.getCenterPos().x - this.currentDest.getCenterPos().x;

        boolean up = yDiff > 0;
        boolean down = yDiff < 0;
        boolean left = xDiff > 0;
        boolean right = xDiff < 0;

        float moveSpeed = speed * this.getSpeedMultiplier();
        if (up) {
            this.updatePos(0, -moveSpeed);
        } else if (down) {
            this.updatePos(0, moveSpeed);
        }

        if (left) {
            this.updatePos(-moveSpeed, 0);
        } else if (right) {
            this.updatePos(moveSpeed, 0);
        }
    }

    /**
     * Respawns the monster at its starting tile.
     */
    public void respawn() {
        this.moveIter = 0;
        this.getCenterPos().set(this.startingTile.getCenterPos());
    }

    public void tick() {
        if (this.getSpeedMultiplier() == 0) {
            return;
        }

        this.move();
        
        if (this.healthBar != null) {
            healthBar.setCurrentHP(currentHP);
        }
    }

    public void drawToLayer(PGraphics layer) {
        if (isDead) {
            return;
        }
        layer.image(this.currentSprite, this.getPos().x, this.getPos().y);
        healthBar.drawToLayer(layer);
    }
    
    /**
     * Draws a new death image to the layer every four frames.
     * @param layer The layer to which the image draws to.
     */
    public void playDeathAnim(PGraphics layer) {
        if (this.deathImageIter >= deathAnimImages.length) {
            this.setDead(true);
            return;
        }
        
        layer.image(deathAnimImages[this.deathImageIter], this.getPos().x, this.getPos().y);

        if (this.currentFrame % (deathAnimImages.length - 1) == 0) {
            ++this.deathImageIter;
        }
        this.currentFrame += this.getSpeedMultiplier();
    }

    /**
     * Gets an ArrayList of ArrayLists of all the paths that the monster can follow.
     * @return The paths that the monster can follow.
     */
    public static ArrayList<ArrayList<Tile>> getValidPaths() { return paths; }
    
    /**
     * Finds all possible paths on the board for the monster to follow.
     * @param board
     */
    public static void findMonsterPaths(Board board) { 
        paths = PathFinding.getShortestPaths(board); 
    }

    /**
     * Represents the {@code Monster} class's pathfinding.
     * <p>
     * This private static class is used for calculating the shortest
     * path for the monster to follow.
     * <p>
     * Breadth-First Search is the algorithm that is used for the shortest
     * path calculations.
     */
    private static final class PathFinding {
        /**
         * Iterates over each starting tile on the board 
         * and calculates the shortest path to the wizard's house
         * from that tile.
         * @param board the board used.
         * @return The shortest path to the wizard's house 
         * for each starting tile on the board.
         */
        private static ArrayList<ArrayList<Tile>> getShortestPaths(Board board) {
            ArrayList<ArrayList<Tile>> shortestPaths = new ArrayList<>();
        
            for (Path startingTile : getStartingTiles(board)) {
                shortestPaths.add(findShortestPath(board, startingTile));
            }

            return shortestPaths;
        }

        /**
         * Finds the shortest path from a given starting tile to
         * the wizard's house.
         * @param board The board that contains a path to the wizard's house.
         * @param startingTile The tile that this algorithm starts at
         * @return The shortest path to the wizard's house.
         * @see https://stackoverflow.com/questions/69471667/two-implementation-methods-of-bfs-for-finding-the-shortest-path-which-one-is-th
         * <p>
         * This was referenced for the creation of this algorithm.
         */
        private static ArrayList<Tile> findShortestPath(Board board, Path startingTile) {
            Tile target = board.getWizHouse();
            HashMap<Tile, Tile> parent = new HashMap<>();
            LinkedList<Tile> queue = new LinkedList<>();
            queue.add(startingTile);
            parent.put(startingTile, null);

            while (!queue.isEmpty()) {
                Tile current = queue.poll();

                if (current.getPos().equals(target.getPos())) {
                    return backTrace(parent, startingTile, target);
                }

                for (Tile t : getAdjacentTiles(board, current)) {
                    if (!parent.containsKey(t)) {
                        parent.put(t, current);
                        queue.add(t);
                    }   
                }
            }
            return new ArrayList<>(); // If something goes wrong, return an empty arraylist
        }

        /**
         * Starting from the wizard's house, this method traces the shortest path back
         * to the starting tile.
         * <p>
         * @param parent All path tiles that were found by BFS.
         * @param start  The starting tile.
         * @param end The wizard's house
         * @return The shortest path from the start tile to the end tile.
         */
        private static ArrayList<Tile> backTrace(HashMap<Tile, Tile> parent, Path start, Tile end) {
            ArrayList<Tile> path = new ArrayList<>();

            Tile current = end;
            path.add(current);
            while (!path.contains(start)) {
                Tile next = parent.get(current);
                path.add(next);
                current = next;
            }
            Collections.reverse(path);
            return path;
        }

        /**
         * Gets an {@code ArrayList} of all tiles adjacent to the parent tile.
         * @param board For getting adjacent tiles.
         * @param parent The tile that is checked.
         * @return An {@code ArrayList} of all tiles adjacent to the parent tile.
         */
        private static ArrayList<Tile> getAdjacentTiles(Board board, Tile parent) {
            ArrayList<Tile> adjacentTiles = new ArrayList<>();
            if (board.getTileUpOf(parent) != null && board.getTileUpOf(parent).isWalkable()) {
                adjacentTiles.add(board.getTileUpOf(parent));
            }

            if (board.getTileDownOf(parent) != null && board.getTileDownOf(parent).isWalkable()) {
                 adjacentTiles.add(board.getTileDownOf(parent));
            }

            if (board.getTileLeftOf(parent) != null && board.getTileLeftOf(parent).isWalkable()) {
                adjacentTiles.add(board.getTileLeftOf(parent));
            }

            if (board.getTileRightOf(parent) != null && board.getTileRightOf(parent).isWalkable()) {
                adjacentTiles.add(board.getTileRightOf(parent));
            }

            return adjacentTiles;
        }

        /**
         * Gets all starting path tiles on the board.
         * @param board The board that is checked.
         * @return All starting path tiles on the board.
         */
        private static ArrayList<Path> getStartingTiles(Board board) {
            ArrayList<Path> startingTiles = new ArrayList<>();
            for (Map.Entry<PVector, Tile> entry : board.getLevelTiles().entrySet()) {
                if (!(entry.getValue() instanceof Path)) {
                    continue;
                }

                // Monsters walk in from outside of map
                Path p = (Path)entry.getValue();
                if (board.getTileUpOf(p) == null) {
                    startingTiles.add(new Path(p.getPos().x, p.getPos().y - App.CELLSIZE));
                } else if (board.getTileDownOf(p) == null) {
                    startingTiles.add(new Path(p.getPos().x, p.getPos().y + App.CELLSIZE));
                } else if (board.getTileLeftOf(p) == null) {
                    startingTiles.add(new Path(p.getPos().x - App.CELLSIZE, p.getPos().y));
                } else if (board.getTileRightOf(p) == null) {
                    startingTiles.add(new Path(p.getPos().x + App.CELLSIZE, p.getPos().y));
                }
            }
            return startingTiles;
        }
    }
}
