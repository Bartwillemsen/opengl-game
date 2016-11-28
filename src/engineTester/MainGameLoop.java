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

public class MainGameLoop
{
	public static void main(String[] args)
	{
		DisplayManager.createDisplay();

		Loader loader = new Loader();

		// Load the OBJ model.
		RawModel model = OBJLoader.loadObjModel("dragon", loader);

		TexturedModel texturedModel = new TexturedModel(model, new ModelTexture(loader.loadTexture("white")));
		ModelTexture texture = texturedModel.getTexture();

		texture.setShineDamper(25);
		texture.setReflectivity(1);

		Entity entity = new Entity(texturedModel, new Vector3f(0, -3, -25), 0, 0, 0, 1);
		Light light = new Light(new Vector3f(200, 200, 100), new Vector3f(1, 1, 1));

		Camera camera = new Camera();

		MasterRenderer renderer = new MasterRenderer();
		while (! Display.isCloseRequested()) {
			camera.move();

			// Start the shader program. Render the model, and finally stop
			// it again.
			renderer.processEntity(entity);

			renderer.render(light, camera);
			DisplayManager.updateDisplay();
		}

		// Clean everything up if we close the program.
		renderer.cleanUp();
		loader.cleanUp();
		DisplayManager.closeDisplay();
	}
}
