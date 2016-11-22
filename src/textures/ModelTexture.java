package textures;

public class ModelTexture
{
	private int textureID;

	/**
	 * The amount of light that is reflected from the surface of a model. A higher value
	 * means a stronger reflection, lower means a weaker reflection.
	 */
	private float reflectivity = 0;

	/**
	 * Indicates how close the camera needs to be to the reflected light to see any
	 * change in the brightness on the surface of the model.
	 */
	private float shineDamper = 1;

	public ModelTexture(int id)
	{
		this.textureID = id;
	}

	public int getID()
	{
		return textureID;
	}

	public float getShineDamper()
	{
		return shineDamper;
	}

	public void setShineDamper(float shineDamper)
	{
		this.shineDamper = shineDamper;
	}

	public float getReflectivity()
	{
		return reflectivity;
	}

	public void setReflectivity(float reflectivity)
	{
		this.reflectivity = reflectivity;
	}
}
