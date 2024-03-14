package WizardTD.Game;

import java.util.LinkedList;

import processing.core.PGraphics;
import processing.data.JSONObject;

import WizardTD.App;
import WizardTD.Game.Board.Board;
import WizardTD.Game.Board.Grass;
import WizardTD.Game.Board.Path;
import WizardTD.Game.Board.Shrub;
import WizardTD.Game.Board.WizHouse;
import WizardTD.Game.Entities.Monsters.Beetle;
import WizardTD.Game.Entities.Monsters.Gremlin;
import WizardTD.Game.Entities.Monsters.Moag;
import WizardTD.Game.Entities.Monsters.Monster;
import WizardTD.Game.Entities.Monsters.Worm;
import WizardTD.Game.Entities.Towers.Fireball;
import WizardTD.Game.Entities.Towers.Tower;
import WizardTD.Game.Entities.Towers.TowerManager;
import WizardTD.Game.Player.InputManager;
import WizardTD.Game.Player.ManaPool;
import WizardTD.Game.UI.GameUI;

/**
 * This represents the game class.
 * This is the class from which anything game related should run.
 */
public class Game {
    /**
     * The game's board.
     */
    private final Board board;
    /**
     * The game's UI.
     */
    private final GameUI ui;
    /**
     * The game's wave manager.
     */
    private final WaveManager waveManager;
    /**
     * The games tower manager.
     */
    private final TowerManager towerManager;
    /**
     * The games input manager.
     */
    private final InputManager inputManager;
    /**
     * The game's mana pool.
     */
    private final ManaPool manaPool;

    /**
     * Tracks whether the game has been won.
     */
    private boolean gameWon = false;
    /**
     * Tracks whether the game has been lost.
     */
    private boolean gameLost = false;
    /**
     * Tracks whether the game has been paused.
     */
    private boolean gamePaused = false;

    /**
     * The monsters that are currently active on the board.
     */
    private final LinkedList<Monster> activeMonsters = new LinkedList<>();

    /**
     * The current speed of the game.
     */
    private float gameSpeed = 1;

    /**
     * The constructor for the {@code Game} class.
     * @param app The application used for the game.
     */
    public Game(JSONObject config) {
        this.board = new Board(config.getString("layout"));
        Monster.findMonsterPaths(board);
        this.manaPool = new ManaPool(config);
        this.waveManager = new WaveManager(config.getJSONArray("waves"), this.activeMonsters);
        this.towerManager = new TowerManager(config, this.manaPool, this.activeMonsters);
    
        this.inputManager = new InputManager(this);
        
        this.ui = new GameUI(this, this.inputManager);
        this.manaPool.addManaBar(this.ui.getManaBar());
        this.waveManager.addWaveTimer(this.ui.getWaveTimer());
    }

    public InputManager getInputManager() { return this.inputManager; }
    public GameUI getGameUI() { return this.ui; }

    /**
     * Gets the game's board.
     * @return The game's board.
     */
    public Board getBoard() { return this.board; }
    /**
     * Gets the game's tower manager.
     * @return The tower manager.
     */
    public TowerManager getTowerManager() { return this.towerManager; }
    /**
     * Gets the game's wave manager.
     * @return the wave manager.
     */
    public WaveManager getWaveManager() { return this.waveManager; }
    /**
     * Gets the game's mana pool.
     * @return The mana pool.
     */
    public ManaPool getManaPool() { return this.manaPool; }

    /**
     * Checks if the game has been won.
     * @return true if the game has been won, otherwise false.
     */
    public boolean isGameWon() { return this.gameWon; }
    /**
     * Checks if the game has been lost.
     * @return true if the game has been lost, otherwise false.
     */
    public boolean isGameLost() { return this.gameLost; }

    /**
     * Gets the game's pause status.
     * @return The game's pause status.
     */
    public boolean isGamePaused() { return this.gamePaused; }
    /**
     * Sets the game's pause state.
     * @param paused The pause state.
     */
    public void setGamePaused(boolean paused) { this.gamePaused = paused; }

    /**
     * Gets the active monsters ccurrently on the board.
     * @return A list of active monsters.
     */
    public LinkedList<Monster> getActiveMonsters() { return this.activeMonsters; }

    /**
     * Gets the current speed of the game.
     * @return The current speed of the game.
     */
    public float getGameSpeed() { return this.gameSpeed; }

    /**
     * Calls input manager's pressButton() to activate buttons on key press.
     * @param key The key that has been pressed.
     */
    public void pressKey(char key) {
        if (this.gameWon || this.gameLost) {
            return; 
        }

        inputManager.onButtonPress(key);
    }

    /**
     * Calls input manager's mousePressed() if the mouse has been pressed.
     * @param e The events related to the mouse that was moved.
     */
    public void mousePressed(int button, float x, float y) {
        if (this.gameWon || this.gameLost) {
            return;  
        }

        if (button == App.LEFT) {
            inputManager.onLeftClick(x, y);
        } else if (button == App.RIGHT) {
            this.inputManager.onRightClick(x, y);
        }
    }

    /**
     * Calls input manager's mouseMoved() if the mouse has been moved.
     */
    public void mouseMoved(float x, float y) {
        if (this.gameWon || this.gameLost) {
            return;
        } 

        inputManager.onMouseMoved(x, y);
    }

    /**
     * Executes all game logic if the game has not been paused or finished.
     */
    public void tick() {
        if (this.gameWon || this.gameLost) {
            return;
        } 
        
        this.isGameOver();

        inputManager.tickActiveButtons();
        if (gamePaused) {
            return;
        }

        waveManager.tick();
        tickMonsters();
        tickTowers();
        
        manaPool.tick();
    }

    /**
     * Ticks the monsters that are currently active on the board.
     * <p>
     * This method also handles the removal of dead monsters,
     * and monster respawning.
     */
    public void tickMonsters() {
        LinkedList<Monster> forMoag = new LinkedList<>();

        for (Monster monster : this.activeMonsters) {
            if (monster.isAlive()) {
                monster.tick();
            }

            if (monster.getCurrentHP() > 0 &&
                monster.getCenterPos().dist(board.getWizHouse().getCenterPos()) <= 5) {
                manaPool.removeMana(monster.getCurrentHP());
                monster.respawn();
            } else if (monster.isDead()) {
                manaPool.addMana(monster.getManaOnDeath());

                if (monster instanceof Moag) {
                    Moag moag = (Moag)monster;
                    forMoag.addAll(moag.getMonstersInMoag());
                }
            }
        }
        this.activeMonsters.addAll(forMoag);
        this.activeMonsters.removeIf(m -> (m.isDead()));
    
    }

     /**
     * Updates all towers on the board.
     */
    public void tickTowers() {
        for (Tower t : this.towerManager.getTowers()) {
            t.tick();
        }
    }

    /**
     * Changes the speed of everything in the game.
     * @param speed The desired speed.
     */
    public void changeGameSpeed(float speed) {
        this.gameSpeed = speed;
        this.waveManager.setSpeedMultiplier(speed);
        this.towerManager.setSpeedOfTowers(speed);
        this.manaPool.setSpeedMultiplier(speed);

        if (this.activeMonsters.isEmpty()) {
            return;
        }

        for (Monster monster : this.activeMonsters) {
            monster.setSpeedMultiplier(speed);
        }
    }

    /**
     * Checks if the game has been won or lost.
     * @return returns true if the game has been one or lost, otherwise false.
     */
    public boolean isGameOver() {
        this.gameWon = this.waveManager.areWavesFinished() && 
                       this.activeMonsters.isEmpty();
        
        this.gameLost = this.manaPool.getCurrentMana() <= 0;
        return this.gameWon || this.gameLost;
    }

    /**
     * Draws the game's board to the layer.
     * @param layer the layer to which the board is drawn.
     */
    public void drawMap(App app, PGraphics layer) {
        board.connectPaths(app);
        board.drawToLayer(layer);
    }

    /**
     * Draws the game's active entities to the layer.
     * @param layer The layer to which the entities are drawn.
     */
    public void drawEntities(PGraphics layer) {
        for (Monster monster : activeMonsters) {
            if (monster.isAlive()) {
                monster.drawToLayer(layer);
            } else {
                monster.playDeathAnim(layer);
            }
        }
        
        for (Tower t : this.towerManager.getTowers()) {
            t.drawToLayer(layer);
        }

        if (this.towerManager.getUnbuiltTower() != null) {
            this.towerManager.getUnbuiltTower().drawToLayer(layer);
        }
    }

    /**
     * Draws the wizard's house to the layer.
     * @param layer The layer to which the wizard's house is drawn.
     */
    public void drawHouse(PGraphics layer) {
        board.getWizHouse().drawToLayer(layer);
    }

    /**
     * Draws the game's UI to the layer.
     * @param layer The layer to which the UI is drawn.
     */
    public void drawUI(PGraphics layer) {
        ui.drawToLayer(layer);  
    } 

    /**
     * Loads All sprites on game creation.
     * @param app The app used for loading the sprites.
     */
    public static void loadGameSprites(App app) {
        Path.loadSprite(app);
        Grass.loadSprite(app);
        Shrub.loadSprite(app);
        WizHouse.loadSprite(app);

        Moag.loadSprites(app);
        Gremlin.loadSprites(app);
        Beetle.loadSprites(app);
        Worm.loadSprites(app);

        Fireball.loadSprite(app);
        Tower.loadSprites(app);
    }
}
