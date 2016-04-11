package toolbox;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

public class Maths
{
	/**
	 * Create a full transformation matrix (translation, rotation and scaling).
	 *
	 * @param  translation  The translation vector
	 * @param  rx           The rotation over the X axis
	 * @param  ry           The rotation over the Y axis
	 * @param  rz           The rotation over the Z axis
	 * @param  scale        The scaling factor
	 * @return The resulting matrix
	 */
	public static Matrix4f createTransformationMatrix(Vector3f translation, float rx, float ry, float rz, float scale)
	{
		Matrix4f matrix = new Matrix4f();

		// Give the matrix as a Identity matrix (or simply, a "do nothing" matrix).
		// same idea: 1 * x = x
		matrix.setIdentity();

		// Translate (move) the matrix over the given vector.
		Matrix4f.translate(translation, matrix, matrix);

		// Apply the rotation given over all three axis (x, y, z)
		Matrix4f.rotate((float) Math.toRadians(rx), new Vector3f(1, 0, 0), matrix, matrix);
		Matrix4f.rotate((float) Math.toRadians(ry), new Vector3f(0, 1, 0), matrix, matrix);
		Matrix4f.rotate((float) Math.toRadians(rz), new Vector3f(0, 0, 1), matrix, matrix);

		// Scale the matrix by the given scale factor.
		Matrix4f.scale(new Vector3f(scale, scale, scale), matrix, matrix);

		return matrix;
	}
}
