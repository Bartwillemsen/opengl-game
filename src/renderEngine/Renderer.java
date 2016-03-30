package renderEngine;

import org.lwjgl.opengl.GL11;
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
	 * @param  model  The model being rendered
	 */
	public void render(RawModel model)
	{
		// Select the VAO and choose the right location in the attribute array.
		GL30.glBindVertexArray(model.getVaoID());
		GL20.glEnableVertexAttribArray(0);

		// Draw the stored data as triangles.
		GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, model.getVertexCount());

		// Finally, disable and unbind the attribute and vertex array.
		GL20.glDisableVertexAttribArray(0);
		GL30.glBindVertexArray(0);
	}
}
