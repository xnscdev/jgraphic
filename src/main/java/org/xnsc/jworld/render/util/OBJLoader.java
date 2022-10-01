package org.xnsc.jworld.render.util;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.xnsc.jworld.render.RawModel;
import org.xnsc.jworld.render.TexturedModel;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class OBJLoader {
    public static RawModel loadModel(String name) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(Objects.requireNonNull(OBJLoader.class.getResourceAsStream("/models/" + name + ".obj"))))) {
            String line;
            List<Vector3f> vertices = new ArrayList<>();
            List<Vector2f> textures = new ArrayList<>();
            List<Vector3f> normals = new ArrayList<>();
            List<Integer> indices = new ArrayList<>();

            while (true) {
                line = reader.readLine();
                String[] data = line.split(" ");
                if (line.startsWith("v ")) {
                    Vector3f vertex = new Vector3f(Float.parseFloat(data[1]), Float.parseFloat(data[2]), Float.parseFloat(data[3]));
                    vertices.add(vertex);
                }
                else if (line.startsWith("vt ")) {
                    Vector2f texture = new Vector2f(Float.parseFloat(data[1]), Float.parseFloat(data[2]));
                    textures.add(texture);
                }
                else if (line.startsWith("vn ")) {
                    Vector3f normal = new Vector3f(Float.parseFloat(data[1]), Float.parseFloat(data[2]), Float.parseFloat(data[3]));
                    normals.add(normal);
                }
                else if (line.startsWith("f "))
                    break;
            }

            float[] texturesArray = new float[vertices.size() * 2];
            float[] normalsArray = new float[vertices.size() * 3];
            while (line != null) {
                if (line.startsWith("f ")) {
                    String[] data = line.split(" ");
                    String[] v1 = data[1].split("/");
                    String[] v2 = data[2].split("/");
                    String[] v3 = data[3].split("/");
                    processVertex(v1, indices, textures, normals, texturesArray, normalsArray);
                    processVertex(v2, indices, textures, normals, texturesArray, normalsArray);
                    processVertex(v3, indices, textures, normals, texturesArray, normalsArray);
                }
                line = reader.readLine();
            }

            float[] verticesArray = new float[vertices.size() * 3];
            int curr = 0;
            for (Vector3f vertex : vertices) {
                verticesArray[curr++] = vertex.x;
                verticesArray[curr++] = vertex.y;
                verticesArray[curr++] = vertex.z;
            }

            int[] indicesArray = indices.stream().mapToInt(Integer::intValue).toArray();
            try {
                int texture = ObjectManager.createTexture(name);
                return new TexturedModel(verticesArray, texturesArray, normalsArray, indicesArray, texture);
            }
            catch (Exception e) {
                return new RawModel(verticesArray, texturesArray, normalsArray, indicesArray);
            }
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void processVertex(String[] data, List<Integer> indices, List<Vector2f> textures, List<Vector3f> normals, float[] texturesArray, float[] normalsArray) {
        int curr = Integer.parseInt(data[0]) - 1;
        indices.add(curr);
        Vector2f texture = textures.get(Integer.parseInt(data[1]) - 1);
        texturesArray[curr * 2] = texture.x;
        texturesArray[curr * 2 + 1] = 1 - texture.y;
        Vector3f normal = normals.get(Integer.parseInt(data[2]) - 1);
        normalsArray[curr * 3] = normal.x;
        normalsArray[curr * 3 + 1] = normal.y;
        normalsArray[curr * 3 + 2] = normal.z;
    }
}
