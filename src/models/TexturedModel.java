package models;

import textures.ModelTexture;

public class TexturedModel
{
	private RawModel rawModel;
	private ModelTexture texture;

	/**
	 * Create a new textured model.
	 *
	 * @param  model    A RawModel object being textured
	 * @param  texture  A texture object used as the texture
	 */
	public TexturedModel(RawModel model, ModelTexture texture)
	{
		this.rawModel = model;
		this.texture = texture;
	}

	public RawModel getRawModel()
	{
		return rawModel;
	}

	public ModelTexture getTexture()
	{
		return texture;
	}
}
