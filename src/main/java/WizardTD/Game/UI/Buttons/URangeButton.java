package WizardTD.Game.UI.Buttons;

import WizardTD.Game.Game;
import WizardTD.Game.Player.InputManager.Keybindings;

/**
 * Represents the {@code URangeButton} class.
 * <p>
 * This button allows the player to upgrade the range of
 * towers when activated.
 */
public class URangeButton extends Button {
    private final Game game;

    /**
     * {@code URangeButton}'s constructor.
     * <p>
     * On construction, this button is bound
     * to the {@code InputManager} and its text description is set.
     * @param text The text displayed over the button.
     * @param x x-position of the button.
     * @param y y-position of the button.
     * @param game For changing the game speed.
     */
    public URangeButton(String text, float x, float y, Game game) {
        super(text, x, y);
        this.game = game;
        this.game.getInputManager().bindButtonToKey(Keybindings.UPGRADE_RANGE_KEY, this);
        this.setTextDescription("Upgrade Range");
    }

    @Override 
    public void activateButton() {
        this.setButtonActivated(true);
        
        if (this.game.getTowerManager().getUnbuiltTower() != null) {
            this.game.getTowerManager().getUnbuiltTower().upgradeRange();
        }
    }

    @Override
    public void deactivateButton() {
        this.setButtonActivated(false);
        
        if (this.game.getTowerManager().getUnbuiltTower() != null) {
            this.game.getTowerManager().getUnbuiltTower().downgradeRange();
        }
    }
}
