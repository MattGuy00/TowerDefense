package WizardTD;

import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PImage;
import processing.data.JSONObject;
import processing.event.MouseEvent;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

import WizardTD.Game.Game;
import WizardTD.Game.Player.InputManager.Keybindings;

/**
 * App is the main class from which all other code should execute.
 * It handles all input and the displaying of the application window.
 */
public class App extends PApplet {

    /**
     * This defines the width and height of every tile.
     */
    public static final int CELLSIZE = 32;
    /**
     * The width of the bar to the right of the board.
     * This is where all buttons are placed.
     */
    public static final int SIDEBAR = 120;
    /**
     * The height of the bar above the board.
     * This defines the area where the wave timer and mana bar is located.
     */
    public static final int TOPBAR = 40;
    /**
     * Defines the width/height of the board in number of tiles
     */
    public static final int BOARD_WIDTH = 20;
    /**
     * The width of the application window.
     */
    public static final int WIDTH = CELLSIZE*BOARD_WIDTH+SIDEBAR;
    /**
     * The height of the application window.
     */
    public static final int HEIGHT = BOARD_WIDTH*CELLSIZE+TOPBAR;

    /**
     * The width of the board in pixels.
     */
    public static int LEVEL_WIDTH = CELLSIZE * BOARD_WIDTH;
    /**
     * The height of the board in pixels.
     */
    public static int LEVEL_HEIGHT = CELLSIZE * BOARD_WIDTH;

    /**
     * This is the maximum number of frames per second that the application displays.
     */
    public static final int FPS = 60;
    /**
     * The default thickness of a shape's outline.
     */
    public static final float DEFAULT_STROKE_WEIGHT = 1;
    /**
     * The default size of all text displayed in the application.
     */
    public static final float DEFAULT_TEXT_SIZE = 12;
    /**
     * The default colour that all shapes and text are rendered with.
     */
    public static final float DEFAULT_FILL_COLOUR = 0;
    /**
     * The default colour that all outlines use.
     */
    public static final float DEFAULT_STROKE_COLOUR = 0;
    /**
     * Holds the path to the json config file.
     */
    public String configPath;

    /**
     * Class constructor.
     */
    public App() {
        this.configPath = "config.json";
    }

    /**
     * Initialise the setting of the window size.
     */
	@Override
    public void settings() {
        size(WIDTH, HEIGHT);
    }

    /**
     * Holds the current game.
     * This can be reassigned.
     */
    private Game game;
    /**
     * The PGraphics layer that the background is drawn to
     */
    private PGraphics backgroundLayer; 
    /**
     * The PGraphics layer that the board is drawn to
     */
    private PGraphics mapLayer;
    /**
     * The PGraphics layer that all entities are drawn to
     */
    private PGraphics entityLayer; 
    /**
     * The PGraphics layer that the wizard's house is drawn to
     */
    private PGraphics houseLayer;
    /**
     * The PGraphics layer that all ui elements are drawn to
     */
    private PGraphics uiLayer;

    /**
     * This function is run once, when the program starts.
     * It's used to define initial environment properties such as screen size and to load media such as images and fonts as the program starts.
     * There can only be one setup() function for each program, and it shouldn't be called again after its initial execution.
     * More information can be found at https://processing.org/reference/setup_.html
     */
	@Override
    public void setup() {
        frameRate(FPS); 
        backgroundLayer = createGraphics(WIDTH, HEIGHT);
        mapLayer = createGraphics(LEVEL_WIDTH, LEVEL_HEIGHT + TOPBAR);
        entityLayer = createGraphics(LEVEL_WIDTH, LEVEL_HEIGHT + TOPBAR);
        houseLayer = createGraphics(LEVEL_WIDTH, LEVEL_HEIGHT + TOPBAR);
        uiLayer = createGraphics(WIDTH, HEIGHT);

        JSONObject config = this.loadJSONObject(this.configPath);
        Game.loadGameSprites(this);
        game = new Game(config);

        // Only have to draw the map tiles once
        mapLayer.beginDraw();
        game.drawMap(this, mapLayer);
        mapLayer.endDraw();
        
    }

    /**
     * Called once every time a key is pressed.
     * The key that was pressed is stored in the key variable. 
     * More information can be found at https://processing.org/reference/keyPressed_.html
     */
	@Override
    public void keyPressed() {
        game.pressKey(this.key);
        if (this.game.isGameOver() && Keybindings.RESTART_KEY.key() == this.key) {
            JSONObject config = this.loadJSONObject(this.configPath);
            this.game = new Game(config);
        }
    }

    /**
     * Called every time a mouse button is pressed. 
     * The mouseButton variable can be used to determine which button has been pressed. 
     * More information can be found at https://processing.org/reference/mousePressed_.html
     */
    @Override
    public void mousePressed(MouseEvent e) {
        game.mousePressed(e.getButton(), e.getX(), e.getY());
    }

    /**
     * called every time the mouse moves and a mouse button is not pressed.
     * More information can be found at https://processing.org/reference/mouseMoved_.html
     */
    @Override
    public void mouseMoved() {
        game.mouseMoved(this.mouseX, this.mouseY);
    }

    /**
     * Executes all logic in game.
     * This should be called in the draw() method, ensuring that it is run on every frame.
     */
    public void tick() {   
        game.tick();
    }

    /**
     * Draw all elements in the game by current frame.
     * The tick() method should be called in this method.
     */
	@Override
    public void draw() {
        tick();

        drawBackgroundLayer();
        drawEntityLayer();
        drawHouseLayer();
        drawUILayer();
        
        image(backgroundLayer, 0, 0);
        image(mapLayer, 0, 0);
        image(entityLayer, 0, 0);
        image(houseLayer, 0, 0);
        image(uiLayer, 0, 0);
    }

    /**
     * All graphics that are to be drawn to the background layer should be called within this method.
     */
    private void drawBackgroundLayer() {
        backgroundLayer.beginDraw();
        backgroundLayer.clear();
        backgroundLayer.fill(200, 100, 50);
        backgroundLayer.rect(0, 0, backgroundLayer.width, backgroundLayer.height);
        backgroundLayer.endDraw();
    }

    /**
     * All graphics that are to be drawn to the entity layer should be called within this method.
     */
    private void drawEntityLayer() {
        entityLayer.beginDraw();
        entityLayer.clear();
        game.drawEntities(entityLayer);
        entityLayer.endDraw();
    }

    /**
     * Wizard's house's draw() method should be called within this method.
     */
    private void drawHouseLayer() {
        houseLayer.beginDraw();
        houseLayer.clear();
        game.drawHouse(houseLayer);
        houseLayer.endDraw();
    }

    /**
     * All graphics that are to be drawn to the background layer should be called within this method.
     */
    private void drawUILayer() {
        uiLayer.beginDraw();
        uiLayer.clear();
        game.drawUI(uiLayer);
        uiLayer.endDraw();
    }

    public static void main(String[] args) {
        PApplet.main("WizardTD.App");
    }

    /**
     * Source: https://stackoverflow.com/questions/37758061/rotate-a-buffered-image-in-java
     * @param pimg The image to be rotated
     * @param angle between 0 and 360 degrees
     * @return the new rotated image
     */
    public PImage rotateImageByDegrees(PImage pimg, double angle) {
        BufferedImage img = (BufferedImage) pimg.getNative();
        double rads = Math.toRadians(angle);
        double sin = Math.abs(Math.sin(rads)), cos = Math.abs(Math.cos(rads));
        int w = img.getWidth();
        int h = img.getHeight();
        int newWidth = (int) Math.floor(w * cos + h * sin);
        int newHeight = (int) Math.floor(h * cos + w * sin);

        PImage result = this.createImage(newWidth, newHeight, ARGB);
        //BufferedImage rotated = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_ARGB);
        BufferedImage rotated = (BufferedImage) result.getNative();
        Graphics2D g2d = rotated.createGraphics();
        AffineTransform at = new AffineTransform();
        at.translate((newWidth - w) / 2, (newHeight - h) / 2);

        int x = w / 2;
        int y = h / 2;

        at.rotate(rads, x, y);
        g2d.setTransform(at);
        g2d.drawImage(img, 0, 0, null);
        g2d.dispose();
        for (int i = 0; i < newWidth; i++) {
            for (int j = 0; j < newHeight; j++) {
                result.set(i, j, rotated.getRGB(i, j));
            }
        }

        return result;
    }
}
