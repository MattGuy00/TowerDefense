package WizardTD.Game.UI;

import WizardTD.App;
import processing.core.PGraphics;

/**
 * Represents the {@code WaveTimer} class.
 * <p>
 * A wave time gives a visual representation of the game's waves
 * to the player.
 */
public final class WaveTimer extends UIElement {
    /**
     * The current wave's number.
     */
    private int currentWave = 1;
    /**
     * The current time of the wave.
     */
    private float currentTime;
    /**
     * Status of the the game being on the final wave.
     */
    private boolean isFinalWave = false;

    /**
     * {@code WaveTimer}'s constructor.
     * <p>
     * Constructs a timer used by {@code WaveManager}.
     * @param x x-position of the timer.
     * @param y y-position of the timer.
     * @param width width of the timer.
     * @param height height of the timer.
     */
    public WaveTimer(float x, float y, float width, float height) {
        this.setPos(x, y);
        this.setWidth(width);
        this.setHeight(height);
    }


    public float getTime() { return this.currentTime; }
    /**
     * Sets the current time of the wave.
     * @param currentTime The wave's current duration.
     */
    public void setTime(float currentTime) { this.currentTime = currentTime; }
    /**
     * The current wave's position in the queue.
     * @param wave The wave's position in the queue.
     */
    public int getWavePos() { return this.currentWave; }
    public void setCurrentWave(int wave) { this.currentWave = wave; }

    public boolean isFinalWave() { return this.isFinalWave; }
    /**
     * Sets the status of the final wave.
     * @param isFinalWave The new status of the final wave.
     */
    public void finalWave(boolean isFinalWave) {this.isFinalWave = isFinalWave; }

    public void drawToLayer(PGraphics layer) {
        layer.fill(0, 0, 0);
        layer.textSize(this.getTextSize());

        if (isFinalWave) {
            String finalWaveText = this.currentTime <= 0 ? "Final wave!" :
                                   "Final wave starts in " +  Math.round(this.currentTime);
            layer.text(finalWaveText, this.getPos().x, this.getPos().y,
                       this.getWidth(), this.getHeight());
        } else {
            layer.text("Wave " + this.currentWave + " starts in " + Math.round(this.currentTime),
                       this.getPos().x, this.getPos().y, this.getWidth(), this.getHeight());
        }
        layer.textSize(App.DEFAULT_TEXT_SIZE);
    }  
}
