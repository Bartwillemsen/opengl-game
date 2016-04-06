package shaders;

public class StaticShader extends ShaderProgram
{
	private static final String VERTEX_FILE = "src/shaders/vertexShader.vsh";
	private static final String FRAGMENT_FILE = "src/shaders/fragmentShader.fsh";

	public StaticShader()
	{
		super(VERTEX_FILE, FRAGMENT_FILE);
	}

	@Override
	protected void bindAttributes()
	{
		// We stored our vertex positions on the 0th index in the VAO.
		super.bindAttribute(0, "position");
	}
}
