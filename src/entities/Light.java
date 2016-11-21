package entities;

import org.lwjgl.util.vector.Vector3f;

public class Light
{
	/**
	 * The position of the light.
	 */
	private Vector3f position;

	/**
	 * The colour of the light.
	 */
	private Vector3f colour;

	/**
	 * Create a new Light instance with a given position and colour.
	 *
	 * @param position  The Vector3f position of the light.
	 * @param colour    The colour of the light, in RGB value.
	 */
	public Light(Vector3f position, Vector3f colour)
	{
		this.position = position;
		this.colour = colour;
	}

	/**
	 * Get the position of the light.
	 *
	 * @return The position
	 */
	public Vector3f getPosition()
	{
		return position;
	}

	/**
	 * Set a new position for the light.
	 *
	 * @param  position  The new Vector3 position of the light
	 */
	public void setPosition(Vector3f position)
	{
		this.position = position;
	}

	public Vector3f getColour()
	{
		return colour;
	}

	public void setColour(Vector3f colour)
	{
		this.colour = colour;
	}
}
