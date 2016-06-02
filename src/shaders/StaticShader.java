package shaders;

import org.lwjgl.util.vector.Matrix4f;

public class StaticShader extends ShaderProgram
{
	private static final String VERTEX_FILE = "src/shaders/vertexShader.vsh";
	private static final String FRAGMENT_FILE = "src/shaders/fragmentShader.fsh";

	/**
	 * The location of the transformationMatrix in the shader program.
	 */
	private int location_transformationMatrix;

	/**
	 * The location of the projectionMatrix in the shader program.
	 */
	private int location_projectionMatrix;

	public StaticShader()
	{
		super(VERTEX_FILE, FRAGMENT_FILE);
	}

	@Override
	protected void bindAttributes()
	{
		super.bindAttribute(0, "position");
		super.bindAttribute(1, "textureCoords");
	}

	/**
	 * Get a reference to all of the uniforms in the shader program.
	 */
	@Override
	protected void getAllUniformLocations()
	{
		location_transformationMatrix = super.getUniformLocation("transformationMatrix");
		location_projectionMatrix = super.getUniformLocation("projectionMatrix");
	}

	/**
	 * Load a transformation matrix in the appropriate uniform location.
	 *
	 * @param  matrix  The matrix being loaded
	 */
	public void loadTransformationMatrix(Matrix4f matrix)
	{
		super.loadMatrix(location_transformationMatrix, matrix);
	}

	/**
	 * Load a projection matrix in the appropriate uniform location.
	 *
	 * @param  projection  The matrix being loaded
	 */
	public void loadProjectionMatrix(Matrix4f projection)
	{
		super.loadMatrix(location_projectionMatrix, projection);
	}
}