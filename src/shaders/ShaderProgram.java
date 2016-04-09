package shaders;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public abstract class ShaderProgram
{
	private int programID;
	private int vertexShaderID;
	private int fragmentShaderID;

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
