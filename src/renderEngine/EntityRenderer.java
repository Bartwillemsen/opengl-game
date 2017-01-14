package renderEngine;

import entities.Entity;
import models.RawModel;
import models.TexturedModel;
import org.lwjgl.opengl.*;
import org.lwjgl.util.vector.Matrix4f;
import shaders.StaticShader;
import textures.ModelTexture;
import toolbox.Maths;

import java.util.List;
import java.util.Map;

public class EntityRenderer
{
	private StaticShader shader;

	/**
	 * Create a new renderer instance.
	 *
	 * @param  shader            The shader being executed during rendering
	 * @param  projectionMatrix  The projection matrix to laod into the shader
	 */
	public EntityRenderer(StaticShader shader, Matrix4f projectionMatrix)
	{
		this.shader = shader;

		shader.start();
		shader.loadProjectionMatrix(projectionMatrix);
		shader.stop();
	}

	/**
	 * <p>Render all the entities on the screen.</p>
	 *
	 * <p>This methods takes in a hashmap which contains textured model and their corresponding
	 * entities. This allows a more efficient rendering method that only binds a certain model
	 * once for entities based on the same model.</p>
	 *
	 * <p>For example, we might have a cube model that we render multiple times on the screen. The
	 * previous render method would bind and unbind the VAO and VBOs for the model for every single
	 * entity of that model. With this method, we will bind the model once, render all entities in
	 * one batch, and only then unbind the base model.</p>
	 *
	 * @param  entities  The hashmap keyed by a Textured model, which contains a list of all
	 *                   the entities that are based on that textured model.
	 */
	public void render(Map<TexturedModel, List<Entity>> entities)
	{
		for (TexturedModel model : entities.keySet()) {
			prepareTexturedModel(model);
			List<Entity> batch = entities.get(model);

			for (Entity entity : batch) {
				prepareInstance(entity);
				GL11.glDrawElements(GL11.GL_TRIANGLES, model.getRawModel().getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
			}

			unbindTexturedModel();
		}
	}

	/**
	 * Prepare the model for rendering. This includes enabling the needed VAO and the
	 * needed attribute lists for storing VBOs. After that, the texture is also bound
	 * to the model.
	 *
	 * @param  model  The textured model to prepare
	 */
	private void prepareTexturedModel(TexturedModel model)
	{
		RawModel rawModel = model.getRawModel();

		// Select the VAO and choose the right location in the attribute array.
		GL30.glBindVertexArray(rawModel.getVaoID());

		GL20.glEnableVertexAttribArray(0);
		GL20.glEnableVertexAttribArray(1);
		GL20.glEnableVertexAttribArray(2);

		ModelTexture texture = model.getTexture();
		shader.loadShineVariables(texture.getShineDamper(), texture.getReflectivity());

		// Activate and fill a texture bank. Texture bank 0 is used by default by the
		// texture sampler in the fragment shader.
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, model.getTexture().getID());
	}

	/**
	 * Unbind the VAO and all of its attribute arrays.
	 */
	private void unbindTexturedModel()
	{
		GL20.glDisableVertexAttribArray(0);
		GL20.glDisableVertexAttribArray(1);
		GL20.glDisableVertexAttribArray(2);
		GL30.glBindVertexArray(0);
	}

	/**
	 * Prepare a single entity for rendering. The transformation matrix for this
	 * entity is loaded into the shader uniform location.
	 *
	 * @param  entity  The entity to prepare for rendering.
	 */
	private void prepareInstance(Entity entity)
	{
		// Create a transformation matrix from the entity information.
		Matrix4f transformationMatrix = Maths.createTransformationMatrix(
				entity.getPosition(),
				entity.getRotX(),
				entity.getRotY(),
				entity.getRotZ(),
				entity.getScale()
		);

		shader.loadTransformationMatrix(transformationMatrix);
	}
}
