package terrains;

import models.RawModel;
import renderEngine.Loader;
import textures.ModelTexture;

public class Terrain
{
	/**
	 * The size of a terrain tile.
	 */
	private static final float SIZE = 800;

	/**
	 * The amount of vertices that make up a single terrain tile.
	 */
	private static final int VERTEX_COUNT = 128;

	/**
	 * The X position of the tile.
	 */
	private float x;

	/**
	 * The Z position of the tile.
	 */
	private float z;

	private RawModel model;

	private ModelTexture texture;

	/**
	 * Create a new Terrain tile instance.
	 *
	 * @param  gridX    The X position of this tile
	 * @param  gridZ    The Z position of this tile
	 * @param  loader   The loader instance used to save the terrain in the VAO
	 * @param  texture  The texture to apply onto this terrain tile.
	 */
	public Terrain(int gridX, int gridZ, Loader loader, ModelTexture texture)
	{
		this.texture = texture;
		this.x = gridX * SIZE;
		this.z = gridZ * SIZE;

		this.model = generateTerrain(loader);
	}

	/**
	 * Generate the terrain.
	 *
	 * @param  loader  The loader instance to save the terrain in the VAO
	 * @return A RawModel instance that represents the terrain
	 */
	private RawModel generateTerrain(Loader loader)
	{
		int count = VERTEX_COUNT * VERTEX_COUNT;

		float[] vertices = new float[count * 3];
		float[] normals = new float[count * 3];
		float[] textureCoords = new float[count * 2];
		int indices[] = new int[6 * (VERTEX_COUNT - 1) * (VERTEX_COUNT - 1)];

		int vertexPointer = 0;
		for (int i = 0; i < VERTEX_COUNT; i++) {
			for (int j = 0; j < VERTEX_COUNT; j++) {
				vertices[vertexPointer * 3] = (float) j / ((float) VERTEX_COUNT - 1) * SIZE;
				vertices[vertexPointer * 3 + 1] = 0;
				vertices[vertexPointer * 3 + 2] = (float) i / ((float) VERTEX_COUNT - 1) * SIZE;

				normals[vertexPointer * 3] = 0;
				normals[vertexPointer * 3 + 1] = 1;
				normals[vertexPointer * 3 + 2] = 0;

				textureCoords[vertexPointer * 2] = (float) j / ((float) VERTEX_COUNT - 1);
				textureCoords[vertexPointer * 2 + 1] = (float) i / ((float) VERTEX_COUNT - 1);

				vertexPointer++;
			}
		}

		int pointer = 0;
		for (int gz = 0; gz < VERTEX_COUNT - 1; gz++) {
			for (int gx = 0; gx < VERTEX_COUNT - 1; gx++) {
				int topLeft = (gz * VERTEX_COUNT) + gx;
				int topRight = topLeft + 1;
				int bottomLeft = ((gz + 1) * VERTEX_COUNT) + gx;
				int bottomRight = bottomLeft + 1;

				indices[pointer++] = topLeft;
				indices[pointer++] = bottomLeft;
				indices[pointer++] = topRight;
				indices[pointer++] = topRight;
				indices[pointer++] = bottomLeft;
				indices[pointer++] = bottomRight;
			}
		}

		return loader.loadToVAO(vertices, textureCoords, normals, indices);
	}

	/*
	 * Getters
	 */

	public float getX()
	{
		return x;
	}

	public float getZ()
	{
		return z;
	}

	public RawModel getModel()
	{
		return model;
	}

	public ModelTexture getTexture()
	{
		return texture;
	}
}
