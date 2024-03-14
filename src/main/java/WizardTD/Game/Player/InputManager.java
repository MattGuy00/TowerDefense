package WizardTD.Game.Player;

import java.util.HashMap;
import java.util.Map;

import WizardTD.App;
import WizardTD.Game.Game;
import WizardTD.Game.Board.Tile;
import WizardTD.Game.Entities.Towers.PlaceholderTower;
import WizardTD.Game.Entities.Towers.Tower;
import WizardTD.Game.UI.Buttons.Button;

/**
 * Represents the InputManager class.
 * <p>
 * This is where all keybindings and button functionality can be found.
 */
public class InputManager {
    /**
     * A public enum for the currently bound keys.
     */
    public enum Keybindings {
        FF_KEY ('f'),
        PAUSE_KEY ('p'),
        RESTART_KEY ('r'),
        BUILD_TOWER_KEY ('t'),
        UPGRADE_RANGE_KEY ('1'),
        UPGRADE_SPEED_KEY ('2'),
        UPGRADE_DAMAGE_KEY ('3'),
        INCREASE_MANA_KEY ('m');

        private char key;
        Keybindings(char key) {
            this.key = key;
        }
        
        /**
         * Gets the key associated with the enum.
         * @return The lowercase key  associated with the enum.
         */
        public char key() { return this.key; }
        /**
         * A static method that gets the enum that the key is associated with.
         * @param key the key that is associated with an enum.
         * @return The name of the enum if it exists, otherwise null.
         */
        public static Keybindings getNameOfBoundKey(char key) {
           for (Keybindings name : Keybindings.values()) {
                if (name.key == key) {
                    return name;
                }
           }
           return null;
        }

        public void rebindKey(char key) {
            for (Keybindings name : Keybindings.values()) {
                if (name.key == key) {
                    return;
                }
           }
            this.key = key;
        }
    }

    private final Game game;
    /**
     * A hashmap that associates Keybinding's enums with buttons.
     */
    private final HashMap<Keybindings, Button> buttons = new HashMap<>();

    /**
     * The closest tower in range of the cursor.
     */
    private Tower towerUnderCursor = null;
    /**
     * The closest tile in range of the cursor.
     */
    private Tile tileUnderCursor = null;
    /**
     * The closest button in range of the cursor.
     */
    private Button buttonUnderCursor = null;

    /**
     * The status of the player being in the key rebind menu
     */
    private boolean inRebindMenu = false;
    /**
     * The key that is being rebound.
     */
    private Keybindings keyToBeRebound;

    /**
     * InputManager's constructor.
     * @param game The game that the input manager is used in.
     */
    public InputManager(Game game) {
        this.game = game;  
    }

    /**
     * Gets the current value of towerUnderCursor.
     * @return The current value of towerUnderCursor.
     */
    public Tower getTowerUnderCursor() { return this.towerUnderCursor; }
    /**
     * Gets bound buttons.
     * @return Buttons that have been bound.
     */
    public HashMap<Keybindings, Button> getBoundButtons() { return this.buttons; }
    /**
     * Binds a button to a Keybindings enum.
     * @param key A keybindings enum that is not already bound.
     * @param button A button that is not already bound.
     */
    public void bindButtonToKey(Keybindings key, Button button) { this.buttons.put(key, button); }

    /**
     * Gets the status of the player being in the rebind menu.
     * @return The status of the player being in the rebind menu.
     */
    public boolean isPlayerInRebindMenu() { return this.inRebindMenu; }

    /**
     * Triggers the button associated with the given key.
     * <p>
     * If there is no button associated with the given key, the method returns early.
     * @param key a char that may be associated with a Keybindings enum.
     */
    public void onButtonPress(char key) {
        if (this.inRebindMenu) {
            keyToBeRebound.rebindKey(key);
            this.game.getGameUI().getRebindKeyMenu().showRebindMenu(key);
            return;
        }

        Button button = buttons.get(Keybindings.getNameOfBoundKey(key));
        if (button == null) {
            return;
        }

        if (button.isButtonActivated()) {
            button.deactivateButton();
        } else {
            button.activateButton();
        }
    }

    public void onLeftClick(float x, float y) {
        if (this.inRebindMenu) { 
            return;
        }

        if (!this.isCursorOnBoard(x, y)) {
            activateButtonUnderCursor(x, y);
            return;
        } 

        if (buttons.get(Keybindings.BUILD_TOWER_KEY).isButtonActivated()) {
            onMouseMoved(x, y); // Hacky way to update the unbuilt tower if the cursor hasnt moved.
            tryToPlaceTower();
        } else {
            tryUpgradeTowerClosestToCursor();
        }
    }

    public void onRightClick(float x, float y) {
        if (this.inRebindMenu) {
            this.inRebindMenu = false;
            this.game.getGameUI().getRebindKeyMenu().hideRebindMenu();
            this.game.setGamePaused(false);
        } else {
            Button boundButton = getButtonUnderCursor(x, y);

            if (boundButton == null) {
                return;
            }

            for (Map.Entry<Keybindings, Button> entry : buttons.entrySet()) {
                if (entry.getValue() == boundButton) {
                    this.game.getGameUI().getRebindKeyMenu().showRebindMenu(entry.getKey().key());
                    this.inRebindMenu = true;
                    keyToBeRebound = entry.getKey();
                    this.game.setGamePaused(true);
                    break;
                }
            }
        }
    }

    /**
     * Tries to apply upgrades to the closest tower in range of the cursor.
     * The upgrades that it applies depend what buttons are active.
     */
    private void tryUpgradeTowerClosestToCursor() {      
        if (this.towerUnderCursor == null) {
            return;
        }

        if (buttons.get(Keybindings.UPGRADE_RANGE_KEY).isButtonActivated()) {
            this.towerUnderCursor.upgradeRange();
        }

        if (buttons.get(Keybindings.UPGRADE_SPEED_KEY).isButtonActivated()) {
            this.towerUnderCursor.upgradeFiringSpeed();
        }

        if (buttons.get(Keybindings.UPGRADE_DAMAGE_KEY).isButtonActivated()) {
            this.towerUnderCursor.upgradeDamage();
        }       
    }

    /**
     * called every time the mouse moves and a mouse button is not pressed.
     * @param x x-position of the cursor,
     * @param y y-position of the cursor.
     */
    public void onMouseMoved(float x, float y) {
        if (this.isCursorOnBoard(x, y)) { 
            this.towerUnderCursor = game.getTowerManager().getUnbuiltTower() != null ?
                                        game.getTowerManager().getUnbuiltTower() :
                                        game.getTowerManager().getTowerAtPos(x, y);

            this.tileUnderCursor = game.getBoard().getTileAtPos(x, y);
            showUnbuiltTowerAtValidTile();
        }

        onButtonHover(x, y);
    }
    
    /**
     * Changes the colour of the button that is currently underneath the cursor.
     * @param x x-position of the cursor.
     * @param y y-position of the cursor.
     */
    private void onButtonHover(float x, float y) {
        Button button = getButtonUnderCursor(x, y);

        if (buttonUnderCursor != null && buttonUnderCursor.equals(button)) {
            buttonUnderCursor.setColour(120, 120, 120);
            buttonUnderCursor.showTooltip();
        } else if (buttonUnderCursor != null && !buttonUnderCursor.equals(button)) {
            buttonUnderCursor.resetColour();
            buttonUnderCursor.hideTooltip();
        }

        if (buttonUnderCursor == null || !buttonUnderCursor.equals(button)) {
            buttonUnderCursor = button;
        }
    }

    /**
     * Shows the unbuilt tower at the current position of the cursor.
     * <p>
     * The tower is only shown if the tile underneath the mouse is considered valid.
     */
    public void showUnbuiltTowerAtValidTile() {
        if (!(this.towerUnderCursor instanceof PlaceholderTower)) {
            return;
        }

        if (!this.tileUnderCursor.isPlaceable() || this.tileUnderCursor.isOccupied()) {
            this.game.getTowerManager().getUnbuiltTower().hide();
            return;
        }

        game.getTowerManager()
            .showUnbuiltTowerAtPos(this.tileUnderCursor.getCenterPos().x, 
                                   this.tileUnderCursor.getCenterPos().y);
    }

    /**
     * Tries to place the tower on the tile underneath the cursor.
     * <p>
     * This method returns early if the tile underneath the cursor is not valid.
     */
    private void tryToPlaceTower() {
        if (this.tileUnderCursor == null || !this.tileUnderCursor.isPlaceable() || this.tileUnderCursor.isOccupied()) {
            return;
        }
        
        game.getTowerManager().buildTower();
        // If unbuiltTower isn't null, then it must mean that we haven't successfully placed the tower
        if (this.game.getTowerManager().getUnbuiltTower() == null) {
            this.tileUnderCursor.setOccupied(true);
            buttons.get(Keybindings.BUILD_TOWER_KEY).deactivateButton();
        }
    }

    /**
     * Checks if the cursor is currently on the board.
     * @param x x-position of the cursor.
     * @param y y-position of the cursor.
     * @return true if the cursor is on the board, otherwise false.
     */
    public boolean isCursorOnBoard(float x, float y) { 
        return x <= App.LEVEL_WIDTH &&
               x <= App.LEVEL_HEIGHT && y >= App.TOPBAR; 
    }

    /**
     * Gets the closest button that the given coordinates are in range of.
     * @param x The x-coordinate.
     * @param y The y-coordinate.
     * @return The closest button if possible, otherwise null.
     */
    public Button getButtonUnderCursor(float x, float y) {
        for (Map.Entry<Keybindings, Button> entry : buttons.entrySet()) {
            Button button = entry.getValue();
            if (x >= button.getPos().x && x <= button.getPos().x + button.getWidth() &&
                y >= button.getPos().y && y <= button.getPos().y + button.getHeight()) {
                return button;
            }
        }
        return null;
    }

    /**
     * Triggers the button that is currently underneath the cursor.
     * @param x The x-coordinate of the cursor.
     * @param y The y-coordinate of the cursor.
     */
    public void activateButtonUnderCursor(float x, float y) {
        if (this.isCursorOnBoard(x, y)) {
            return;
        }

        Button button = getButtonUnderCursor(x, y);
        if (button == null) {
            return;
        }

        if (button.isButtonActivated()) {
            button.deactivateButton();
        } else {
            button.activateButton();
        }
    }

    /**
     * Reactivates certain buttons on every frame.
     */
    public void tickActiveButtons() {
        if (this.buttons.get(Keybindings.FF_KEY).isButtonActivated()) {
            this.buttons.get(Keybindings.FF_KEY).activateButton();
        }  

        if (this.buttons.get(Keybindings.UPGRADE_RANGE_KEY).isButtonActivated()) {
            this.buttons.get(Keybindings.UPGRADE_RANGE_KEY).activateButton();
        }

        if (this.buttons.get(Keybindings.UPGRADE_SPEED_KEY).isButtonActivated()) {
            this.buttons.get(Keybindings.UPGRADE_SPEED_KEY).activateButton();
        }

        if (this.buttons.get(Keybindings.UPGRADE_DAMAGE_KEY).isButtonActivated()) {
            this.buttons.get(Keybindings.UPGRADE_DAMAGE_KEY).activateButton();
        }
    } 
}
