package org.xnsc.jworld.render.util;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.xnsc.jworld.render.model.ModelData;
import org.xnsc.jworld.render.model.ModelVertex;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class OBJLoader {
    public static ModelData loadModel(String name) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(Objects.requireNonNull(OBJLoader.class.getResourceAsStream("/models/" + name + ".obj"))))) {
            String line;
            List<ModelVertex> vertices = new ArrayList<>();
            List<Vector2f> textures = new ArrayList<>();
            List<Vector3f> normals = new ArrayList<>();
            List<Integer> indices = new ArrayList<>();

            while (true) {
                line = reader.readLine();
                String[] data = line.split(" ");
                if (line.startsWith("v ")) {
                    Vector3f vertex = new Vector3f(Float.parseFloat(data[1]), Float.parseFloat(data[2]), Float.parseFloat(data[3]));
                    ModelVertex modelVertex = new ModelVertex(vertices.size(), vertex);
                    vertices.add(modelVertex);
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

            while (line != null && line.startsWith("f ")) {
                String[] data = line.split(" ");
                String[] v1 = data[1].split("/");
                String[] v2 = data[2].split("/");
                String[] v3 = data[3].split("/");
                processVertex(v1, vertices, indices);
                processVertex(v2, vertices, indices);
                processVertex(v3, vertices, indices);
                line = reader.readLine();
            }

            removeUnusedVertices(vertices);
            float[] verticesArray = new float[vertices.size() * 3];
            float[] texturesArray = new float[vertices.size() * 2];
            float[] normalsArray = new float[vertices.size() * 3];
            int[] indicesArray = indices.stream().mapToInt(Integer::intValue).toArray();
            float furthest = convertData(vertices, textures, normals, verticesArray, texturesArray, normalsArray);
            return new ModelData(verticesArray, texturesArray, normalsArray, indicesArray, furthest);
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void processVertex(String[] data, List<ModelVertex> vertices, List<Integer> indices) {
        int index = Integer.parseInt(data[0]) - 1;
        ModelVertex vertex = vertices.get(index);
        int texture = Integer.parseInt(data[1]) - 1;
        int normal = Integer.parseInt(data[2]) - 1;
        if (!vertex.isSet()) {
            vertex.setTextureIndex(texture);
            vertex.setNormalIndex(normal);
            indices.add(index);
        }
        else
            handleProcessedVertex(vertex, texture, normal, indices, vertices);
    }

    private static float convertData(List<ModelVertex> vertices, List<Vector2f> textures, List<Vector3f> normals, float[] verticesArray, float[] texturesArray, float[] normalsArray) {
        float furthest = 0;
        for (int i = 0; i < vertices.size(); i++) {
            ModelVertex vertex = vertices.get(i);
            if (vertex.getLength() > furthest)
                furthest = vertex.getLength();
            Vector3f position = vertex.getPosition();
            Vector2f texture = textures.get(vertex.getTextureIndex());
            Vector3f normal = normals.get(vertex.getNormalIndex());
            verticesArray[i * 3] = position.x;
            verticesArray[i * 3 + 1] = position.y;
            verticesArray[i * 3 + 2] = position.z;
            texturesArray[i * 2] = texture.x;
            texturesArray[i * 2 + 1] = 1 - texture.y;
            normalsArray[i * 3] = normal.x;
            normalsArray[i * 3 + 1] = normal.y;
            normalsArray[i * 3 + 2] = normal.z;
        }
        return furthest;
    }

    private static void handleProcessedVertex(ModelVertex vertex, int texture, int normal, List<Integer> indices, List<ModelVertex> vertices) {
        if (vertex.sameTextureNormal(texture, normal))
            indices.add(vertex.getIndex());
        else {
            ModelVertex next = vertex.getDuplicateVertex();
            if (next != null)
                handleProcessedVertex(next, texture, normal, indices, vertices);
            else {
                ModelVertex duplicate = new ModelVertex(vertices.size(), vertex.getPosition());
                duplicate.setTextureIndex(texture);
                duplicate.setNormalIndex(normal);
                vertex.setDuplicateVertex(duplicate);
                vertices.add(duplicate);
                indices.add(duplicate.getIndex());
            }
        }
    }

    private static void removeUnusedVertices(List<ModelVertex> vertices) {
        for (ModelVertex vertex : vertices) {
            if (!vertex.isSet()) {
                vertex.setTextureIndex(0);
                vertex.setNormalIndex(0);
            }
        }
    }
}
