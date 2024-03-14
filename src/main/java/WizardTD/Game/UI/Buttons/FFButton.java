package WizardTD.Game.UI.Buttons;

import WizardTD.Game.Game;
import WizardTD.Game.Player.InputManager.Keybindings;

/**
 * Represents the {@code FFButton} class.
 * <p>
 * This button fast forwards the game when activated,
 * and reverses this change when deactivated.
 */
public class FFButton extends Button {
    /**
     * The game that the button is used in.
     */
    private final Game game;

    /**
     * {@code FFButton}'s constructor.
     * <p>
     * On construction, this button is bound
     * to the {@code InputManager} and a
     * text description is added.
     * @param text The text displayed over the button.
     * @param x x-position of the button.
     * @param y y-position of the button.
     * @param game For changing the game speed.
     */
    public FFButton(String text, float x, float y, Game game) {
        super(text, x, y);
        this.game = game;
        this.game.getInputManager().bindButtonToKey(Keybindings.FF_KEY, this);
        this.setTextDescription("2x Speed");
    }

    @Override
    public void activateButton() {
        this.setButtonActivated(true);
        this.game.changeGameSpeed(2);
    }

    @Override
    public void deactivateButton() {
        this.setButtonActivated(false);
        this.game.changeGameSpeed(1);
    }
}
