package renderEngine;

import entities.Entity;
import models.RawModel;
import models.TexturedModel;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;
import shaders.StaticShader;
import toolbox.Maths;

public class Renderer
{
	public void prepare()
	{

		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
		GL11.glClearColor(1f, 0f, 0f, 1f);
	}

	/**
	 * Render a model.
	 *
	 * @param  texturedModel  The model being rendered
	 */
	public void render(Entity entity, StaticShader shader)
	{
		TexturedModel model = entity.getModel();
		RawModel rawModel = model.getRawModel();

		// Select the VAO and choose the right location in the attribute array.
		GL30.glBindVertexArray(rawModel.getVaoID());

		GL20.glEnableVertexAttribArray(0);
		GL20.glEnableVertexAttribArray(1);

		// Create a transformation matrix from the entity information.
		Matrix4f transformationMatrix = Maths.createTransformationMatrix(
			entity.getPosition(),
			entity.getRotX(),
			entity.getRotY(),
			entity.getRotZ(),
			entity.getScale()
		);

		shader.loadTransformationMatrix(transformationMatrix);

		// Activate and fill a texture bank. Texture bank 0 is used by default by the
		// texture sampler in the fragment shader.
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, model.getTexture().getID());

		// Draw the stored data as triangles.
		GL11.glDrawElements(GL11.GL_TRIANGLES, rawModel.getVertexCount(), GL11.GL_UNSIGNED_INT, 0);

		// Finally, disable and unbind the attribute and vertex array.
		GL20.glDisableVertexAttribArray(0);
		GL20.glDisableVertexAttribArray(1);
		GL30.glBindVertexArray(0);
	}
}
