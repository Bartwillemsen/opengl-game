package engineTester;

import entities.Camera;
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
		StaticShader shader = new StaticShader();
		Renderer renderer = new Renderer(shader);

		float[] vertices = {
				-0.5f,0.5f,-0.5f,
				-0.5f,-0.5f,-0.5f,
				0.5f,-0.5f,-0.5f,
				0.5f,0.5f,-0.5f,

				-0.5f,0.5f,0.5f,
				-0.5f,-0.5f,0.5f,
				0.5f,-0.5f,0.5f,
				0.5f,0.5f,0.5f,

				0.5f,0.5f,-0.5f,
				0.5f,-0.5f,-0.5f,
				0.5f,-0.5f,0.5f,
				0.5f,0.5f,0.5f,

				-0.5f,0.5f,-0.5f,
				-0.5f,-0.5f,-0.5f,
				-0.5f,-0.5f,0.5f,
				-0.5f,0.5f,0.5f,

				-0.5f,0.5f,0.5f,
				-0.5f,0.5f,-0.5f,
				0.5f,0.5f,-0.5f,
				0.5f,0.5f,0.5f,

				-0.5f,-0.5f,0.5f,
				-0.5f,-0.5f,-0.5f,
				0.5f,-0.5f,-0.5f,
				0.5f,-0.5f,0.5f

		};

		int[] indices = {
				0,1,3,
				3,1,2,
				4,5,7,
				7,5,6,
				8,9,11,
				11,9,10,
				12,13,15,
				15,13,14,
				16,17,19,
				19,17,18,
				20,21,23,
				23,21,22
		};

		float[] textureCoords = {
				0,0,
				0,1,
				1,1,
				1,0,
				0,0,
				0,1,
				1,1,
				1,0,
				0,0,
				0,1,
				1,1,
				1,0,
				0,0,
				0,1,
				1,1,
				1,0,
				0,0,
				0,1,
				1,1,
				1,0,
				0,0,
				0,1,
				1,1,
				1,0
		};

		RawModel model = loader.loadToVAO(vertices, textureCoords, indices);
		TexturedModel texturedModel = new TexturedModel(model, new ModelTexture(loader.loadTexture("brick-texture")));

		Entity entity = new Entity(texturedModel, new Vector3f(0, 0, -5), 0, 0, 0, 1);

		Camera camera = new Camera();

		while (! Display.isCloseRequested()) {
			entity.increaseRotation(1, 1, 0);
			camera.move();
			renderer.prepare();

			// Start the shader program. Render the model, and finally stop
			// it again.
			shader.start();
			shader.loadViewMatrix(camera);
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
