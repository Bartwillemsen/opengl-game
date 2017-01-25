package renderEngine;

import entities.Entity;
import models.RawModel;
import models.TexturedModel;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import shaders.TerrainShader;
import terrains.Terrain;
import textures.ModelTexture;
import toolbox.Maths;

import java.util.List;

public class TerrainRenderer
{
	private TerrainShader shader;

	public TerrainRenderer(TerrainShader shader, Matrix4f projectionMatrix)
	{
		this.shader = shader;

		shader.start();
		shader.loadProjectionMatrix(projectionMatrix);
		shader.stop();
	}

	public void render(List<Terrain> terrains)
	{
		for (Terrain terrain : terrains) {
			prepareTerrain(terrain);
			loadModelMatrix(terrain);

			GL11.glDrawElements(GL11.GL_TRIANGLES, terrain.getModel().getVertexCount(), GL11.GL_UNSIGNED_INT, 0);

			unbindTexturedModel();
		}
	}

	/**
	 * Prepare the model for rendering. This includes enabling the needed VAO and the
	 * needed attribute lists for storing VBOs. After that, the texture is also bound
	 * to the model.
	 *
	 * @param  terrain  The textured model to prepare
	 */
	private void prepareTerrain(Terrain terrain)
	{
		RawModel rawModel = terrain.getModel();

		// Select the VAO and choose the right location in the attribute array.
		GL30.glBindVertexArray(rawModel.getVaoID());

		GL20.glEnableVertexAttribArray(0);
		GL20.glEnableVertexAttribArray(1);
		GL20.glEnableVertexAttribArray(2);

		ModelTexture texture = terrain.getTexture();
		shader.loadShineVariables(texture.getShineDamper(), texture.getReflectivity());

		// Activate and fill a texture bank. Texture bank 0 is used by default by the
		// texture sampler in the fragment shader.
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture.getID());
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
	 * @param  terrain  The entity to prepare for rendering.
	 */
	private void loadModelMatrix(Terrain terrain)
	{
		// Create a transformation matrix from the entity information.
		Matrix4f transformationMatrix = Maths.createTransformationMatrix(
				new Vector3f(terrain.getX(), 0, terrain.getZ()),
				0,
				0,
				0,
				1
		);

		shader.loadTransformationMatrix(transformationMatrix);
	}
}
