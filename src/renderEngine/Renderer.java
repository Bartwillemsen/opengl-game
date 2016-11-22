package renderEngine;

import entities.Entity;
import models.RawModel;
import models.TexturedModel;
import org.lwjgl.opengl.*;
import org.lwjgl.util.vector.Matrix4f;
import shaders.StaticShader;
import textures.ModelTexture;
import toolbox.Maths;

public class Renderer
{
	private static final float FOV = 70;
	private static final float NEAR_PLANE = 0.1f;
	private static final float FAR_PLANE = 1000f;

	private Matrix4f projectionMatrix;

	/**
	 * Create a new renderer instance.
	 *
	 * @param  shader  The shader being executed during rendering
	 */
	public Renderer(StaticShader shader)
	{
		createProjectionMatrix();
		shader.start();
		shader.loadProjectionMatrix(projectionMatrix);
		shader.stop();
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
	 * Render a model.
	 *
	 * @param  entity  The entity being rendered
	 * @param  shader  The shader being executed
	 */
	public void render(Entity entity, StaticShader shader)
	{
		TexturedModel model = entity.getModel();
		RawModel rawModel = model.getRawModel();

		// Select the VAO and choose the right location in the attribute array.
		GL30.glBindVertexArray(rawModel.getVaoID());

		GL20.glEnableVertexAttribArray(0);
		GL20.glEnableVertexAttribArray(1);
		GL20.glEnableVertexAttribArray(2);

		// Create a transformation matrix from the entity information.
		Matrix4f transformationMatrix = Maths.createTransformationMatrix(
			entity.getPosition(),
			entity.getRotX(),
			entity.getRotY(),
			entity.getRotZ(),
			entity.getScale()
		);

		shader.loadTransformationMatrix(transformationMatrix);

		// Load the reflectivity value of the model into the shader.
		ModelTexture texture = model.getTexture();
		shader.loadShineVariables(texture.getShineDamper(), texture.getReflectivity());

		// Activate and fill a texture bank. Texture bank 0 is used by default by the
		// texture sampler in the fragment shader.
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, model.getTexture().getID());

		// Draw the stored data as triangles.
		GL11.glDrawElements(GL11.GL_TRIANGLES, rawModel.getVertexCount(), GL11.GL_UNSIGNED_INT, 0);

		// Finally, disable and unbind the attribute and vertex array.
		GL20.glDisableVertexAttribArray(0);
		GL20.glDisableVertexAttribArray(1);
		GL20.glDisableVertexAttribArray(2);
		GL30.glBindVertexArray(0);
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
