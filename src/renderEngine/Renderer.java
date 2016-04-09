package renderEngine;

import models.RawModel;
import models.TexturedModel;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

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
	public void render(TexturedModel texturedModel)
	{
		RawModel model = texturedModel.getRawModel();

		// Select the VAO and choose the right location in the attribute array.
		GL30.glBindVertexArray(model.getVaoID());

		GL20.glEnableVertexAttribArray(0);
		GL20.glEnableVertexAttribArray(1);

		// Activate and fill a texture bank. Texture bank 0 is used by default by the
		// texture sampler in the fragment shader.
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texturedModel.getTexture().getID());

		// Draw the stored data as triangles.
		GL11.glDrawElements(GL11.GL_TRIANGLES, model.getVertexCount(), GL11.GL_UNSIGNED_INT, 0);

		// Finally, disable and unbind the attribute and vertex array.
		GL20.glDisableVertexAttribArray(0);
		GL20.glDisableVertexAttribArray(1);
		GL30.glBindVertexArray(0);
	}
}
