package WizardTD.Game.UI.Buttons;

import WizardTD.Game.Game;
import WizardTD.Game.Player.InputManager.Keybindings;

/**
 * Represents the {@code USpeedButton} class.
 * <p>
 * This button allows the player to upgrade the firing speed of
 * towers when activated.
 */
public class USpeedButton extends Button {
    private final Game game;
    
    /**
     * {@code USpeedButton}'s constructor.
     * <p>
     * On construction, this button is bound
     * to the {@code InputManager} and its text description is set.
     * @param text The text displayed over the button.
     * @param x x-position of the button.
     * @param y y-position of the button.
     * @param game For changing the game speed.
     */
    public USpeedButton(String text, float x, float y, Game game) {
        super(text, x, y);
        this.game = game;
        game.getInputManager().bindButtonToKey(Keybindings.UPGRADE_SPEED_KEY, this);
        this.setTextDescription("Upgrade Speed");
    }

    @Override 
    public void activateButton() {
        this.setButtonActivated(true);
        
        if (this.game.getTowerManager().getUnbuiltTower() != null) {
            this.game.getTowerManager().getUnbuiltTower().upgradeFiringSpeed();
        }
    }

    @Override
    public void deactivateButton() {
        this.setButtonActivated(false);
        
        if (this.game.getTowerManager().getUnbuiltTower() != null) {
            this.game.getTowerManager().getUnbuiltTower().downgradeFiringSpeed();
        }
    }
}
