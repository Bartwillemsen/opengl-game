package engineTester;

import entities.Entity;
import models.TexturedModel;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;
import renderEngine.DisplayManager;
import renderEngine.Loader;
import models.RawModel;
import renderEngine.Renderer;
import shaders.StaticShader;
import textures.ModelTexture;

public class MainGameLoop
{
	public static void main(String[] args)
	{
		DisplayManager.createDisplay();

		Loader loader = new Loader();
		Renderer renderer = new Renderer();
		StaticShader shader = new StaticShader();

		float[] vertices = {
			-0.5f,  0.5f, 0f, // V0
			-0.5f, -0.5f, 0f, // V1
			 0.5f, -0.5f, 0f, // V2
			 0.5f,  0.5f, 0f, // V3
		};

		int[] indices = {
			0, 1, 3,    // Top left triangle (V0, V1, V3)
			3, 1, 2     // Bottom right triangle (V3, V1, V2)
		};

		float[] textureCoords = {
			0, 0,   // V0
			0, 1,   // V1
			1, 1,   // V2
			1, 0    // V3
		};

		RawModel model = loader.loadToVAO(vertices, textureCoords, indices);
		TexturedModel texturedModel = new TexturedModel(model, new ModelTexture(loader.loadTexture("brick-texture")));

		Entity entity = new Entity(texturedModel, new Vector3f(-1, 0, 0), 0, 0, 0, 1);

		while (! Display.isCloseRequested()) {
			renderer.prepare();

			// Start the shader program. Render the model, and finally stop
			// it again.
			shader.start();
			renderer.render(entity, shader);
			shader.stop();

			DisplayManager.updateDisplay();
		}

		// Clean everything up if we close the program.
		shader.cleanUp();
		loader.cleanUp();
		DisplayManager.closeDisplay();
	}
}
