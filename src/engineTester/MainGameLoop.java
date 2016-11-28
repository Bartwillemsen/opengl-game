package engineTester;

import entities.Camera;
import entities.Entity;
import entities.Light;
import models.TexturedModel;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;
import renderEngine.*;
import models.RawModel;
import textures.ModelTexture;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainGameLoop
{
	public static void main(String[] args)
	{
		DisplayManager.createDisplay();

		Loader loader = new Loader();

		// Load the OBJ model.
		RawModel model = OBJLoader.loadObjModel("cube", loader);

		TexturedModel cubeModel = new TexturedModel(model, new ModelTexture(loader.loadTexture("brick-texture")));

		Light light = new Light(new Vector3f(200, 200, 100), new Vector3f(1, 1, 1));
		Camera camera = new Camera();

		List<Entity> cubes = new ArrayList<>();
		Random random = new Random();

		for (int i = 0; i < 200; i++) {
			float x = random.nextFloat() * 100 - 50;
			float y = random.nextFloat() * 100 - 50;
			float z = random.nextFloat() * -300;

			cubes.add(new Entity(cubeModel, new Vector3f(x, y, z), random.nextFloat() * 180f, random.nextFloat() * 180f, 0, 2f));
		}

		MasterRenderer renderer = new MasterRenderer();
		while (! Display.isCloseRequested()) {
			camera.move();

			// Start the shader program. Render the model, and finally stop
			// it again.
			for (Entity c : cubes) {
				renderer.processEntity(c);
			}

			renderer.render(light, camera);
			DisplayManager.updateDisplay();
		}

		// Clean everything up if we close the program.
		renderer.cleanUp();
		loader.cleanUp();
		DisplayManager.closeDisplay();
	}
}
