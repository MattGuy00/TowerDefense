package WizardTD.Game.UI.Buttons;

import WizardTD.Game.Game;
import WizardTD.Game.Player.InputManager.Keybindings;

/**
 * Represents the {@code UDamageButton} class.
 * <p>
 * This button allows the player to upgrade the damage of
 * towers when activated.
 */
public class UDamageButton extends Button {
    private final Game game;

    /**
     * {@code UDamageButton}'s constructor.
     * <p>
     * On construction, this button is bound
     * to the {@code InputManager} and its text description is set.
     * @param text The text displayed over the button.
     * @param x x-position of the button.
     * @param y y-position of the button.
     * @param game For changing the game speed.
     */
    public UDamageButton(String text ,float x, float y, Game game) {
        super(text, x, y);
        this.game = game;
        game.getInputManager().bindButtonToKey(Keybindings.UPGRADE_DAMAGE_KEY, this);
        this.setTextDescription("Upgrade damage");
    }

    @Override 
    public void activateButton() {
        this.setButtonActivated(true);
        
        if (this.game.getTowerManager().getUnbuiltTower() != null) {
            this.game.getTowerManager().getUnbuiltTower().upgradeDamage();
        }
    }

    @Override
    public void deactivateButton() {
        this.setButtonActivated(false);
        
        if (this.game.getTowerManager().getUnbuiltTower() != null) {
            this.game.getTowerManager().getUnbuiltTower().downgradeDamage();
        }
    }
    
}
