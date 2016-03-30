package renderEngine;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.*;

public class DisplayManager
{
	private static final int WIDTH = 1280;
	private static final int HEIGHT = 720;

	private static final int FPS_CAP = 120;

	/**
	 * Create a new game window.
	 */
	public static void createDisplay()
	{
		// Make sure we use at least OpenGL 3.3
		ContextAttribs attribs = new ContextAttribs(3, 3)
			.withForwardCompatible(true)
			.withProfileCore(true);

		try {
			Display.setDisplayMode(new DisplayMode(WIDTH, HEIGHT));
			Display.create(new PixelFormat(), attribs);
			Display.setTitle("Our first display!");
		} catch (LWJGLException e) {
			e.printStackTrace();
		}

		GL11.glViewport(0, 0, WIDTH, HEIGHT);
	}

	/**
	 * Update the window.
	 */
	public static void updateDisplay()
	{
		Display.sync(FPS_CAP);
		Display.update();
	}

	/**
	 * Destroy the window.
	 */
	public static void closeDisplay()
	{
		Display.destroy();
	}
}
