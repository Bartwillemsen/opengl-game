package renderEngine;

import models.RawModel;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

public class Loader
{
	// Keeps track of all the VAOs and VBOs we create.
	private List<Integer> vaos = new ArrayList<>();
	private List<Integer> vbos = new ArrayList<>();
	private List<Integer> textures = new ArrayList<>();

	/**
	 * Create a new VAO and create a new RawModel in it.
	 *
	 * @param  positions  The vertex positions
	 * @return A new RawModel object
	 */
	public RawModel loadToVAO(float[] positions, float[] textureCoords, int[] indices)
	{
		int vaoID = createVAO();
		bindIndicesBuffer(indices);

		storeDataInAttributeList(0, 3, positions);
		storeDataInAttributeList(1, 2, textureCoords);

		unbindVAO();

		// Create a new model. We divide by three because a position consists
		// of three elements.
		return new RawModel(vaoID, indices.length);
	}

	/**
	 * Load a texture image.
	 *
	 * @param  fileName  The name of the texture (relative to the res folder).
	 * @return The ID of the loaded texture
	 */
	public int loadTexture(String fileName)
	{
		Texture texture = null;
		try {
			texture = TextureLoader.getTexture("PNG", new FileInputStream("res/" + fileName + ".png"));
		} catch (IOException e) {
			e.printStackTrace();
		}

		int textureID = texture != null ? texture.getTextureID() : 0;
		textures.add(textureID);

		return textureID;
	}

	/**
	 * Remove all the creates VAOs and VBOs from memory.
	 */
	public void cleanUp()
	{
		vaos.forEach(GL30::glDeleteVertexArrays);

		vbos.forEach(GL15::glDeleteBuffers);

		textures.forEach(GL11::glDeleteTextures);
	}

	/**
	 * Create a new empty VAO.
	 *
	 * @return The ID of the VAO created.
	 */
	private int createVAO()
	{
		int vaoID = GL30.glGenVertexArrays();
		vaos.add(vaoID);

		GL30.glBindVertexArray(vaoID);

		return vaoID;
	}

	/**
	 * Store the data in the attribute list of the VAO.
	 *
	 * @param  attributeNumber  The number of the attribute list where we want to store the datas
	 * @param  coordinateSize   The size of each coordinate in the attribute list
	 * @param  data             The actual data we want to store
	 */
	private void storeDataInAttributeList(int attributeNumber, int coordinateSize, float[] data)
	{
		// Generate a VBO to store our data.
		int vboID = GL15.glGenBuffers();
		vbos.add(vboID);

		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboID);

		// Store the data in the VBO.
		FloatBuffer buffer = storeDataInFloatBuffer(data);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);

		// Put the VBO in the attribute list of a VAO and unbind the VBO.
		GL20.glVertexAttribPointer(attributeNumber, coordinateSize, GL11.GL_FLOAT, false, 0, 0);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
	}

	/**
	 * Unbind the currently bound VAO.
	 */
	private void unbindVAO()
	{
		GL30.glBindVertexArray(0);
	}

	private void bindIndicesBuffer(int[] indices)
	{
		// Generate a VBO.
		int vboID = GL15.glGenBuffers();
		vbos.add(vboID);

		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboID);

		IntBuffer buffer = storeDateInIntBuffer(indices);
		GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
	}

	/**
	 * Convert an int array to a IntBuffer object.
	 *
	 * @param  data the int array to be converted
	 * @return A IntBuffer object of the passed data
	 */
	private IntBuffer storeDateInIntBuffer(int[] data)
	{
		IntBuffer buffer = BufferUtils.createIntBuffer(data.length);
		buffer.put(data);
		buffer.flip();

		return buffer;
	}

	/**
	 * Convert a float array to a FloatBuffer object.
	 *
	 * @param  data  The float array to be converted
	 * @return A FloatArray object of the passed data
	 */
	private FloatBuffer storeDataInFloatBuffer(float[] data)
	{
		FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
		buffer.put(data);
		buffer.flip();

		return buffer;
	}
}
