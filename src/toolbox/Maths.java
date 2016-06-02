package toolbox;

import entities.Camera;
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

	/**
	 * Create a view matrix. This acts as a camera in the game world.
	 *
	 * The camera actually moves the world in the opposite direction instead of
	 * moving an actual camera around the world in the direction we want too.
	 *
	 * @param  camera  The camera object
	 * @return The view matrix with the camera positions applied
	 */
	public static Matrix4f createViewMatrix(Camera camera)
	{
		Matrix4f viewMatrix = new Matrix4f();
		viewMatrix.setIdentity();

		// Rotate the matrix by using the camera pith and yaw (x and y values).
		Matrix4f.rotate((float) Math.toRadians(camera.getPitch()), new Vector3f(1, 0, 0), viewMatrix, viewMatrix);
		Matrix4f.rotate((float) Math.toRadians(camera.getYaw()), new Vector3f(0, 1, 0), viewMatrix, viewMatrix);

		// Get the camera position and reverse the values.
		Vector3f cameraPos = camera.getPosition();
		Vector3f negativeCameraPos = new Vector3f(-cameraPos.x, -cameraPos.y, -cameraPos.z);

		Matrix4f.translate(negativeCameraPos, viewMatrix, viewMatrix);

		return viewMatrix;
	}
}
