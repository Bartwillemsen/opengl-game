package shaders;

import entities.Camera;
import entities.Light;
import org.lwjgl.util.vector.Matrix4f;
import toolbox.Maths;

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

	/**
	 * The location of the viewMatrix in the shader program.
	 */
	private int location_viewMatrix;

	/**
	 * The location of the light position uniform in the shader program.
	 */
	private int location_lightPosition;

	/**
	 * The location of the light colour uniform in the shader program.
	 */
	private int location_lightColour;

	public StaticShader()
	{
		super(VERTEX_FILE, FRAGMENT_FILE);
	}

	@Override
	protected void bindAttributes()
	{
		super.bindAttribute(0, "position");
		super.bindAttribute(1, "textureCoords");
		super.bindAttribute(2, "normal");
	}

	/**
	 * Get a reference to all of the uniforms in the shader program.
	 */
	@Override
	protected void getAllUniformLocations()
	{
		location_transformationMatrix = super.getUniformLocation("transformationMatrix");
		location_projectionMatrix = super.getUniformLocation("projectionMatrix");
		location_viewMatrix = super.getUniformLocation("viewMatrix");
		location_lightPosition = super.getUniformLocation("lightPosition");
		location_lightColour = super.getUniformLocation("lightColour");
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
	 * Load the light instance in the uniform location in the shader program.
	 *
	 * @param  light  The Light instance
	 */
	public void loadLight(Light light)
	{
		super.loadVector(location_lightPosition, light.getPosition());
		super.loadVector(location_lightColour, light.getColour());
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

	/**
	 * Load a view matrix in the appropriate uniform location.
	 *
	 * @param  camera  The camera we create the view matrix from
	 */
	public void loadViewMatrix(Camera camera)
	{
		Matrix4f viewMatrix = Maths.createViewMatrix(camera);
		super.loadMatrix(location_viewMatrix, viewMatrix);
	}

}