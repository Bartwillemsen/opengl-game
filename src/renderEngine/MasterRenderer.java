package renderEngine;

import entities.Camera;
import entities.Entity;
import entities.Light;
import models.TexturedModel;
import shaders.StaticShader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MasterRenderer
{
	/**
	 * The StaticShader instance.
	 */
	private StaticShader shader = new StaticShader();

	/**
	 * The renderer instance.
	 */
	private Renderer renderer = new Renderer(shader);

	/**
	 * A hashmap containing all of the Textured Models and all of their entities.
	 */
	private Map<TexturedModel, List<Entity>> entities = new HashMap<>();

	/**
	 * Render all of the entities on the screen.
	 *
	 * @param sun     The lightsource to use in the scene
	 * @param camera  The camera from which the player views the scene
	 */
	public void render(Light sun, Camera camera)
	{
		renderer.prepare();

		// Start the shader and load the light source and camera.
		shader.start();
		shader.loadLight(sun);
		shader.loadViewMatrix(camera);

		// Render all of the entities as one batch.
		renderer.render(entities);

		shader.stop();
		entities.clear();
	}

	/**
	 * This method places an entity in the right location in the Hashmap.
	 *
	 * @param  entity  The entity to sort
	 */
	public void processEntity(Entity entity)
	{
		TexturedModel entityModel = entity.getModel();

		// We first check if there is already an existing list of entities for
		// this model. If not, a new list if entities is created and stored with
		// the base model used by the entity.
		List<Entity> batch = entities.get(entityModel);
		if (batch != null) {
			batch.add(entity);
		} else {
			List<Entity> newBatch = new ArrayList<>();
			newBatch.add(entity);

			entities.put(entityModel, newBatch);
		}

	}

	/**
	 * Clean up the mess.
	 */
	public void cleanUp()
	{
		shader.cleanUp();
	}
}
