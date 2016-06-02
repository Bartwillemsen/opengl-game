package shaders;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.FloatBuffer;

public abstract class ShaderProgram
{
	private int programID;
	private int vertexShaderID;
	private int fragmentShaderID;

	// Float Buffer used for storing a 4x4 matrix.
	private static FloatBuffer matrixBuffer = BufferUtils.createFloatBuffer(16);

	/**
	 * Create a new Shader Program.
	 *
	 * @param vertexFile    Path to the vertex shader
	 * @param fragmentFile  Path to the fragment shader
	 */
	public ShaderProgram(String vertexFile, String fragmentFile)
	{
		// Load the vertex and fragment shader.
		vertexShaderID = loadShader(vertexFile, GL20.GL_VERTEX_SHADER);
		fragmentShaderID = loadShader(fragmentFile, GL20.GL_FRAGMENT_SHADER);

		// Create a full shader program by attaching the vertex and fragment
		// shader together in the program.
		programID = GL20.glCreateProgram();
		GL20.glAttachShader(programID, vertexShaderID);
		GL20.glAttachShader(programID, fragmentShaderID);

		// Link and validate the created program.
		bindAttributes();
		GL20.glLinkProgram(programID);
		GL20.glValidateProgram(programID);

		// Get all the uniform locations.
		getAllUniformLocations();
	}

	/**
	 * Get the location of all uniforms in the shader program.
	 */
	protected abstract void getAllUniformLocations();

	/**
	 * Get the location of a given uniform variable.
	 *
	 * @param  uniformName  The name of the uniform variable
	 * @return An int representing the location of a specific uniform variable
	 */
	protected int getUniformLocation(String uniformName)
	{
		return GL20.glGetUniformLocation(programID, uniformName);
	}

	/**
	 * Start a shader program.
	 */
	public void start()
	{
		GL20.glUseProgram(programID);
	}

	/**
	 * Stop the currently started shader program.
	 */
	public void stop()
	{
		GL20.glUseProgram(0);
	}

	/**
	 * Clean up by first detaching and then deleting both the vertex and
	 * fragment shaders. Finally delete the program itself.
	 */
	public void cleanUp()
	{
		stop();
		GL20.glDetachShader(programID, vertexShaderID);
		GL20.glDetachShader(programID, fragmentShaderID);

		GL20.glDeleteShader(vertexShaderID);
		GL20.glDeleteShader(fragmentShaderID);

		GL20.glDeleteProgram(programID);
	}

	/**
	 * Link the inputs to the shader programs to one of the attributes of the VAO.
	 */
	protected abstract void bindAttributes();

	/**
	 * Bind an attribute of the VAO to a variable in the shader program.
	 *
	 * @param attribute     The index of the attribute
	 * @param variableName  The name of the variable in the shader code
	 */
	protected void bindAttribute(int attribute, String variableName)
	{
		GL20.glBindAttribLocation(programID, attribute, variableName);
	}

	/**
	 * Load a float value in the uniform location.
	 *
	 * @param location  The reference to the uniform
	 * @param value     The value being loaded in the uniform
	 */
	protected void loadFloat(int location, float value)
	{
		GL20.glUniform1f(location, value);
	}

	/**
	 * Load a vector in the uniform location.
	 *
	 * @param  location  The reference to the uniform
	 * @param  vector    The vector being loaded in the location
	 */
	protected void loadVector(int location, Vector3f vector)
	{
		GL20.glUniform3f(location, vector.x, vector.y, vector.z);
	}

	/**
	 * Load a boolean value in the uniform location.
	 *
	 * @param  location  The reference to the uniform
	 * @param  value     The value (0 or 1) being loaded in the location
	 */
	protected void loadBoolean(int location, boolean value)
	{
		GL20.glUniform1f(location, (value) ? 1 : 0);
	}

	/**
	 * Load a matrix in the uniform location.
	 *
	 * @param  location  The reference to the uniform
	 * @param  matrix    The matrix being loaded in the location
	 */
	protected void loadMatrix(int location, Matrix4f matrix)
	{
		matrix.store(matrixBuffer);
		matrixBuffer.flip();

		GL20.glUniformMatrix4(location, false, matrixBuffer);
	}

	/**
	 * Load the shader files and read the source code.
	 *
	 * @param  file  The filename of the source file
	 * @param  type  shader type (vertex or fragment)
	 * @return The ID of the loaded shader.
	 */
	private static int loadShader(String file, int type)
	{
		StringBuilder shaderSource = new StringBuilder();

		try {
			BufferedReader reader = new BufferedReader(new FileReader(file));

			String line;
			while ((line = reader.readLine()) != null) {
				shaderSource.append(line).append('\n');
			}

			reader.close();
		} catch (IOException e) {
			System.err.println("Could not read file!");
			e.printStackTrace();
			System.exit(-1);
		}

		int shaderID = GL20.glCreateShader(type);
		GL20.glShaderSource(shaderID, shaderSource);
		GL20.glCompileShader(shaderID);

		if (GL20.glGetShaderi(shaderID, GL20.GL_COMPILE_STATUS) == GL11.GL_FALSE) {
			System.out.println(GL20.glGetShaderInfoLog(shaderID, 500));
			System.err.println("Could not compile shader.");
			System.exit(-1);
		}

		return shaderID;
	}
}
