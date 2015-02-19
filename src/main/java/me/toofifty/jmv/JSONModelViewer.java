package me.toofifty.jmv;

import java.awt.Rectangle;
import java.io.File;

import javax.swing.JFrame;

import me.toofifty.jmv.editor.EditorAction;
import me.toofifty.jmv.gui.ApplicationControl;
import me.toofifty.jmv.model.Model;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;
import org.newdawn.slick.opengl.Texture;

/**
 * Requires:
 * 		jackson-core
 * 		jackson-annotations
 * 		jackson-databind
 * 		jackson-coreutils
 * 		lwjgl
 * 		slick-util
 * 
 * @author Toofifty
 *
 */
public class JSONModelViewer {

	/** Main instance */
	public static JSONModelViewer instance;
	
	/* FPS Info */
	private long lastFrame;
	private long lastFPS;
	protected int fps;
	
	/* Zoom level (aka. z axis) */
	private float zoom = -10;
	
	/* Other classes */
	private CubeRenderer renderer;
	private MouseControl mouse;
	private ApplicationControl frame;
	private EditorAction editorAction;
	
	private Texture floor;
	
	/** Main model */
	private Model model;
	
	/** New model json string */
	private String newModelString;
	private Model newModel;
	/** Model update flag */
	private boolean modelNeedsUpdate = false;
	private boolean displayNeedsResize = true;
	private boolean showFloor = true;
	private boolean isOrtho = false;
	private File saveFile;

	/**
	 * Main function, init and loop
	 */
	public void start() {
		editorAction = new EditorAction();
		frame = new ApplicationControl(editorAction);
		getDelta();
		lastFPS = getTime();
		
		initGL();
		
		renderer = new CubeRenderer();
		
		mouse = new MouseControl();
		mouse.update(Mouse.getX(), Mouse.getY());
		
		floor = FileLoader.loadTexture("floor");

		while (!Display.isCloseRequested()) {
			int delta = getDelta();
			
			if (modelNeedsUpdate) {
				if (newModelString != null && newModelString != "") {
					if (FileLoader.isValidJSON(newModelString)) {
						model = new Model(FileLoader.loadJson(newModelString));
					}
					newModelString = null;
				} else if (newModel != null) {
					model = newModel;
					newModel = null;
				} else {
					model = new Model();
				}
				modelNeedsUpdate = false;
			}
			
			if (displayNeedsResize) {
				resize();
			}

			pollInput(delta);
			updateFPS();
			renderGL();
			
			Display.update();
			Display.sync(120);
		}

		Display.destroy();
		frame.dispose();
	}
	
	/**
	 * Init OpenGL context
	 */
	private void initGL() {
		try {
			Display.setParent(frame.getCanvas());
			Display.setResizable(true);
			Display.create();
		} catch (LWJGLException e) {
			e.printStackTrace();
			System.exit(0);
		}

		GL11.glEnable(GL11.GL_TEXTURE_2D);

		//GL11.glViewport(0, 0, frame.getWidth(), frame.getHeight());
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		
		GLU.gluPerspective(70, (float) frame.getWidth() / (float) frame.getHeight(), 0.3F, 100);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		
		GL11.glEnable(GL11.GL_DEPTH_TEST);
    	GL11.glEnable(GL11.GL_BLEND);
    	GL11.glEnable(GL11.GL_CULL_FACE);
    	//GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		
		GL11.glClearColor(0.375F, 0.5625F, 0.75F, 1F);
	}
	
	/**
	 * Resize OpenGL
	 */
	protected void resize() {		
		int width = Display.getWidth();
		/*if (frame.getTextEditorPanel().isVisible()) {
			width -= frame.getTextEditorPanel().getWidth();
		}*/
		
		GL11.glViewport(0, 0, width, Display.getHeight());
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		if (isOrtho) {
			GLU.gluPerspective(2, (float) (width) / (float) Display.getHeight(), 0.3F, 10000);
		} else {
			GLU.gluPerspective(70, (float) (width) / (float) Display.getHeight(), 0.3F, 100);
		}
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		displayNeedsResize = false;
	}
	
	/**
	 * Thread-safe resizer.
	 */
	public void scheduleResize() {
		displayNeedsResize = true;
	}

	/**
	 * Main render (looped) 
	 */
	private void renderGL() {
		// Clear screen & depth buffer
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		GL11.glLoadIdentity();
		GL11.glTranslatef(0, 0, zoom);
		
		renderer.doRotate();
		if (renderer.getRX() >= 0 && showFloor) {
			renderer.renderFloor(floor);		
		}
		
		if (this.model != null) {
			renderer.renderModel(model);
		} else {
			frame.disableSaves();
		}
	}
	
	/**
	 * Schedule model update for the next frame,
	 * using a JSON string
	 * 
	 * @param jsonModel
	 */
	public void scheduleModelUpdate(String newModelString) {
		if (newModelString != null && newModelString != "") {
			this.newModelString = newModelString;
			this.modelNeedsUpdate = true;
			frame.enableSaves();
		}
	}
	
	/**
	 * Schedule model update for the next frame,
	 * using a Model object
	 * 
	 * @param model
	 */
	public void scheduleModelUpdate(Model model) {
		if (model != null) {
			this.newModel = model;
			this.modelNeedsUpdate = true;
			frame.enableSaves();
		}
	}
	
	/**
	 * Main model getter
	 * 
	 * @return model
	 */
	public Model getModel() {
		return model;
	}
	
	/**
	 * Thread-safe model creation
	 */
	public void scheduleNewModel() {
		this.modelNeedsUpdate = true;
		frame.enableSaves();
	}

	/**
	 * Toggle floor boolean 
	 * (used by ControlFrame)
	 */
	public void toggleFloor() {
		showFloor = !showFloor;
	}

	/**
	 * Set angles to isometric
	 * (used by ControlFrame)
	 */
	public void setIso() {
		renderer.setRY(45F); 
		renderer.setRX(35.264F);
		displayNeedsResize = true;
	}
	
	/**
	 * Toggle 'ortho' (extremely small fov)
	 */
	public void toggleOrtho() {
		isOrtho = !isOrtho;
		if (isOrtho) {
			zoom *= 40;
		} else {
			zoom /= 40;
		}
		displayNeedsResize = true;
	}

	/**
	 * Get the time in milliseconds
	 * 
	 * @return time (ms)
	 */
	public long getTime() {
		return System.nanoTime() / 1000000;
	}

	/**
	 * Get delta time since last frame
	 * 
	 * @return delta time (ms)
	 */
	public int getDelta() {
		long time = getTime();
		int delta = (int) (time - lastFrame);
		lastFrame = time;
		return delta;
	}
	
	public ApplicationControl getFrame() {
		return frame;
	}
	
	public File getSaveFile() {
		return saveFile;
	}
	
	public void setSaveFile(File file) {
		this.saveFile = file;
	}
	
	public Rectangle getCentreScreen(int width, int height) {
		int x = frame.getWidth() / 2 + frame.getX() - width / 2;
		int y = frame.getHeight() / 2 + frame.getY() - height / 2;
		return new Rectangle(x, y, width, height);		
	}

	/**
	 * Update the FPS on the window title
	 */
	private void updateFPS() {
		if (getTime() - lastFPS > 1000) {
			Display.setTitle("FPS: " + fps);
			fps = 0;
			lastFPS += 1000;
		}
		this.fps++;
	}
	
	/**
	 * Check for and handle input
	 * 
	 * @param delta
	 */
	private void pollInput(float delta) {	
		mouse.update(Mouse.getX(), Mouse.getY());
		
		if (Keyboard.isKeyDown(Keyboard.KEY_W))
			renderer.rotateUp(delta / 20F);
		if (Keyboard.isKeyDown(Keyboard.KEY_S))
			renderer.rotateUp(-delta / 20F);
		if (Keyboard.isKeyDown(Keyboard.KEY_A))
			renderer.rotateLeft(delta / 20F);
		if (Keyboard.isKeyDown(Keyboard.KEY_D))
			renderer.rotateLeft(-delta / 20F);
		
		if (Mouse.isButtonDown(0)) {
			renderer.rotateUp(-mouse.dy() / 2F);
			renderer.rotateLeft(mouse.dx() / 2F);
		}
		
		float scrollAmount = Mouse.getDWheel();
		if (scrollAmount != 0) {
			scroll(-scrollAmount / 100F);
		}
	}

	/**
	 * Scroll (zoom) in and out by delta
	 * 
	 * @param delta
	 */
	private void scroll(float delta) {
		if (isOrtho) {
			delta *= 40;
		}
		this.zoom -= delta;
		System.out.println(zoom);
	}

	/**
	 * Java main
	 * 
	 * @param argv
	 */
	public static void main(String[] argv) {
		instance = new JSONModelViewer();
		instance.start();
	}

}
