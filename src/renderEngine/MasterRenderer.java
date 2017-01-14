package renderEngine;

import entities.Camera;
import entities.Entity;
import entities.Light;
import models.TexturedModel;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Matrix4f;
import shaders.StaticShader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MasterRenderer
{
	private static final float FOV = 70;
	private static final float NEAR_PLANE = 0.1f;
	private static final float FAR_PLANE = 1000f;

	private Matrix4f projectionMatrix;

	/**
	 * The StaticShader instance.
	 */
	private StaticShader shader = new StaticShader();

	/**
	 * The renderer instance.
	 */
	private EntityRenderer renderer;

	/**
	 * A hashmap containing all of the Textured Models and all of their entities.
	 */
	private Map<TexturedModel, List<Entity>> entities = new HashMap<>();

	public MasterRenderer()
	{
		// Make sure faces that point away of the camera are not rendered.
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glCullFace(GL11.GL_BACK);

		// Initiate the projection matrix. This is used for all the renderers.
		createProjectionMatrix();

		renderer = new EntityRenderer(shader, projectionMatrix);
	}

	/**
	 * Render all of the entities on the screen.
	 *
	 * @param sun     The lightsource to use in the scene
	 * @param camera  The camera from which the player views the scene
	 */
	public void render(Light sun, Camera camera)
	{
		prepare();

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
	 * Prepare the screen for rendering.
	 */
	public void prepare()
	{
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		GL11.glClearColor(0.3f, 0f, 0.0f, 1f);
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

	/**
	 * Create the projection matrix.
	 */
	private void createProjectionMatrix()
	{
		float aspectRatio = (float) Display.getWidth() / (float) Display.getHeight();
		float y_scale = (float) ((1f / Math.tan(Math.toRadians(FOV / 2f))) * aspectRatio);
		float x_scale = y_scale / aspectRatio;
		float frustum_length = FAR_PLANE - NEAR_PLANE;

		/*
		 * [ (1/tan(fov/2))/a  0               0       0                   ]
		 * [ 0                 1/tan(fov/2)    0       0                   ]
		 * [ 0                 0               -zp/zm  -(2*Zfar*Znear)/zm  ]
		 * [ 0                 0               -1      0                   ]
		 *
		 * a   = aspect ratio
		 * fov = Field of View
		 * zm  = Zfar - Znear
		 * zp  = Zfar + Znear
		 */
		projectionMatrix = new Matrix4f();
		projectionMatrix.m00 = x_scale;
		projectionMatrix.m11 = y_scale;
		projectionMatrix.m22 = -((FAR_PLANE + NEAR_PLANE) / frustum_length);
		projectionMatrix.m23 = -1;
		projectionMatrix.m32 = -((2 * NEAR_PLANE * FAR_PLANE) / frustum_length);
		projectionMatrix.m33 = 0;
	}
}
