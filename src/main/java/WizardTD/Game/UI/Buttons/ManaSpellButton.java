package WizardTD.Game.UI.Buttons;

import WizardTD.Game.Game;
import WizardTD.Game.Player.InputManager.Keybindings;

/**
 * Represents the {@code ManaSpellButton} class.
 * <p>
 * This button upgrades the game's mana pool
 * on activation.
 */
public class ManaSpellButton extends Button {
    private final Game game;
    
    /**
     * {@code ManaSpellButton}'s constructor.
     * <p>
     * On construction, this button is bound
     * to the {@code InputManager}, its text description
     * is set, and a tooltip is added.
     * @param text The text displayed over the button.
     * @param x x-position of the button.
     * @param y y-position of the button.
     * @param game For changing the game's mana pool.
     */
    public ManaSpellButton(String text, float x, float y, Game game) {
        super(text, x, y);
        this.game = game;
        game.getInputManager().bindButtonToKey(Keybindings.INCREASE_MANA_KEY, this);
        this.setTooltip("cost: " + game.getManaPool().getManaSpellCost());
        this.setTextDescription("spell cost: " + game.getManaPool().getManaSpellCost());
    }

    @Override 
    public void activateButton() {
        this.setButtonActivated(true);
        
        this.game.getManaPool().upgradeManaPool();
        this.setTooltip("cost: " + this.game.getManaPool().getManaSpellCost());
        this.setTextDescription("Mana pool cost: " + this.game.getManaPool().getManaSpellCost());

        this.setButtonActivated(false);
    }
}
