package WizardTD.Game.UI.Buttons;

import WizardTD.Game.Game;
import WizardTD.Game.Player.InputManager.Keybindings;

/**
 * Represents the {@code PauseButton} class.
 * <p>
 * This button pauses the game on button activation, 
 * and reverses this change on deactivation.
 */
public class PauseButton extends Button {
    private final Game game;

    /**
     * {@code PauseButton}'s constructor.
     * <p>
     * On construction, this button is bound
     * to the {@code InputManager} and its text description is set.
     * @param text The text displayed over the button.
     * @param x x-position of the button.
     * @param y y-position of the button.
     * @param game For changing the game speed.
     */
    public PauseButton(String text, float x, float y, Game game) {
        super(text, x, y);
        this.game = game;
        this.game.getInputManager().bindButtonToKey(Keybindings.PAUSE_KEY, this);
        this.setTextDescription("Pause Button");
    }

    @Override 
    public void activateButton() {
        this.setButtonActivated(true);
        this.game.setGamePaused(true);
    }

    @Override
    public void deactivateButton() {
        this.setButtonActivated(false);
        this.game.setGamePaused(false);
    }
}
