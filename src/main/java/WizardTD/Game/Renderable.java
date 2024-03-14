package WizardTD.Game;

import processing.core.PGraphics;

public interface Renderable {
    /**
     * Draws images and shapes to the specified layer.
     * @param layer The layer to be drawn to.
     */
    public void drawToLayer(PGraphics layer);
}
