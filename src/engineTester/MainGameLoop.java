package engineTester;

import entities.Camera;
import entities.Entity;
import entities.Light;
import models.TexturedModel;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;
import renderEngine.DisplayManager;
import renderEngine.Loader;
import models.RawModel;
import renderEngine.OBJLoader;
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

		// Load the OBJ model.
		RawModel model = OBJLoader.loadObjModel("dragon", loader);

		TexturedModel texturedModel = new TexturedModel(model, new ModelTexture(loader.loadTexture("white")));

		Entity entity = new Entity(texturedModel, new Vector3f(0, -3, -25), 0, 0, 0, 1);
		Light light = new Light(new Vector3f(0, -3, -20), new Vector3f(1, 1, 1));

		Camera camera = new Camera();

		while (! Display.isCloseRequested()) {
			entity.increaseRotation(0, 1, 0);
			camera.move();
			renderer.prepare();

			// Start the shader program. Render the model, and finally stop
			// it again.
			shader.start();
			shader.loadLight(light);
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
