package renderEngine;

import models.RawModel;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * This class allows loading files in OBJ format.
 */
public class OBJLoader
{
	/**
	 * Load an OBJ model and return it as a RawModel instance.
	 *
	 * @param  filename  The file to load
	 * @param  loader    The Loader instance
	 * @return A RawModel of the loaded OBJ model
	 */
	public static RawModel loadObjModel(String filename, Loader loader)
	{
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(new File("res/" + filename + ".obj")));
		} catch (FileNotFoundException e) {
			System.err.println("File [" + filename + "] not found");
			e.printStackTrace();
			System.exit(-1);
		}

		String line;
		List<Vector3f> vertices = new ArrayList<>();
		List<Vector2f> textures = new ArrayList<>();
		List<Vector3f> normals = new ArrayList<>();
		List<Integer> indices = new ArrayList<>();

		float[] verticesArray = null;
		float[] normalsArray = null;
		float[] textureArray = null;
		int[] indicesArray = null;

		try {
			while (true) {
				line = reader.readLine();
				String[] currentLine = line.split(" ");

				// Is this line a vertex position? If so, read the line which consists of
				// three float values for the x, y, and z and put those in the vertices
				// array.
				if (line.startsWith("v ")) {
					Vector3f vertex = new Vector3f(
						Float.parseFloat(currentLine[1]),
						Float.parseFloat(currentLine[2]),
						Float.parseFloat(currentLine[3])
					);

					vertices.add(vertex);

				// vt is a texture coordinate. Texture coordinate are just 2 values, so we
				// store those in a Vector2f.
				} else if (line.startsWith("vt ")) {
					Vector2f texture = new Vector2f(
							Float.parseFloat(currentLine[1]),
							Float.parseFloat(currentLine[2])
					);

					textures.add(texture);

				// Normals are also a vector with an x, y, and z value.
				} else if (line.startsWith("vn ")) {
					Vector3f normal = new Vector3f(
						Float.parseFloat(currentLine[1]),
						Float.parseFloat(currentLine[2]),
						Float.parseFloat(currentLine[3])
					);

					normals.add(normal);

				// Faces.
				} else if (line.startsWith("f ")) {
					textureArray = new float[vertices.size() * 2];
					normalsArray = new float[vertices.size() * 3];
					break;
				}
			}

			while (line != null) {
				if (! line.startsWith("f ")) {
					line = reader.readLine();
					continue;
				}
				String[] currentLine = line.split(" ");
				String[] vertex1 = currentLine[1].split("/");
				String[] vertex2 = currentLine[2].split("/");
				String[] vertex3 = currentLine[3].split("/");

				processVertex(vertex1, indices, textures, normals, textureArray, normalsArray);
				processVertex(vertex2, indices, textures, normals, textureArray, normalsArray);
				processVertex(vertex3, indices, textures, normals, textureArray, normalsArray);

				line = reader.readLine();
			}

			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		verticesArray = new float[vertices.size() * 3];
		indicesArray = new int[indices.size()];

		int vertexPointer = 0;
		for (Vector3f vertex : vertices) {
			verticesArray[vertexPointer++] = vertex.x;
			verticesArray[vertexPointer++] = vertex.y;
			verticesArray[vertexPointer++] = vertex.z;
		}

		for (int i = 0; i < indices.size(); i++) {
			indicesArray[i] = indices.get(i);
		}

		return loader.loadToVAO(verticesArray, textureArray, indicesArray);
	}

	private static void processVertex(
		String[] vertexData,
		List<Integer> indices,
		List<Vector2f> textures,
	    List<Vector3f> normals,
	    float[] textureArray,
	    float[] normalsArray
	) {
		int currentVertexPointer = Integer.parseInt(vertexData[0]) - 1;
		indices.add(currentVertexPointer);

		Vector2f currentTex = textures.get(Integer.parseInt(vertexData[1]) - 1);
		textureArray[currentVertexPointer * 2] = currentTex.x;
		textureArray[currentVertexPointer * 2 + 1] = 1 - currentTex.y;

		Vector3f currentNorm = normals.get(Integer.parseInt(vertexData[2]) - 1);
		normalsArray[currentVertexPointer * 3] = currentNorm.x;
		normalsArray[currentVertexPointer * 3 + 1] = currentNorm.y;
		normalsArray[currentVertexPointer * 3 + 2] = currentNorm.z;
	}
}
