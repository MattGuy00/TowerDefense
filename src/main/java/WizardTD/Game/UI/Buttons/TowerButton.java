package WizardTD.Game.UI.Buttons;

import WizardTD.Game.Game;
import WizardTD.Game.Player.InputManager.Keybindings;

/**
 * Represents the {@code TowerButton} class.
 * <p>
 * This button instantiates an unbuilt tower for the player
 * to place on the board when activated, and the reverse
 * when deactivated.
 */
public class TowerButton extends Button {
    private final Game game;

    /**
     * {@code TowerButton}'s constructor.
     * <p>
     * On construction, this button is bound
     * to the {@code InputManager}, its text description is set, 
     * and a tooltip is added.
     * @param text The text displayed over the button.
     * @param x x-position of the button.
     * @param y y-position of the button.
     * @param game For changing the game speed.
     */
    public TowerButton(String text, float x, float y, Game game) {
        super(text, x, y);
        this.game = game;
        game.getInputManager().bindButtonToKey(Keybindings.BUILD_TOWER_KEY, this);
        this.setTextDescription("Build Tower");
        this.setTooltip("Cost: " + this.game.getTowerManager().getBaseTowerCost());
    }

    @Override 
    public void activateButton() {
        this.setButtonActivated(true);
        this.game.getTowerManager().initialiseUnbuiltTower();
    }

    @Override
    public void deactivateButton() {
        this.setButtonActivated(false);
        this.game.getTowerManager().deactivateUnbuiltTower();
    }
}
