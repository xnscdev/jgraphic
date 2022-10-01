package org.xnsc.jworld.render.terrain;

import org.xnsc.jworld.render.model.ModelData;
import org.xnsc.jworld.render.model.TexturedModel;

public class TerrainPiece {
    private static final float SIZE = 800;
    private static final int VERTEX_COUNT = 128;
    private final TexturedModel model;
    private final float x;
    private final float z;

    public TerrainPiece(int gx, int gz, String texture) {
        x = gx * SIZE;
        z = gz * SIZE;
        model = buildModel(texture);
    }

    public TexturedModel getModel() {
        return model;
    }

    public float getX() {
        return x;
    }

    public float getZ() {
        return z;
    }

    private TexturedModel buildModel(String texture) {
        int count = VERTEX_COUNT * VERTEX_COUNT;
        float[] vertices = new float[count * 3];
        float[] textures = new float[count * 2];
        float[] normals = new float[count * 3];
        int[] indices = new int[6 * (VERTEX_COUNT - 1) * (VERTEX_COUNT - 1)];
        int index = 0;
        for (int i = 0; i < VERTEX_COUNT; i++) {
            for (int j = 0; j < VERTEX_COUNT; j++) {
                vertices[index * 3] = (float) j / ((float) VERTEX_COUNT - 1) * SIZE;
                vertices[index * 3 + 1] = 0;
                vertices[index * 3 + 2] = (float) i / ((float) VERTEX_COUNT - 1) * SIZE;
                textures[index * 2] = (float) j / ((float) VERTEX_COUNT - 1);
                textures[index * 2 + 1] = (float) i / ((float) VERTEX_COUNT - 1);
                normals[index * 3] = 0;
                normals[index * 3 + 1] = 1;
                normals[index * 3 + 2] = 0;
                index++;
            }
        }
        index = 0;
        for (int gz = 0; gz < VERTEX_COUNT - 1; gz++) {
            for (int gx = 0; gx < VERTEX_COUNT - 1; gx++) {
                int topLeft = gz * VERTEX_COUNT + gx;
                int topRight = topLeft + 1;
                int bottomLeft = (gz + 1) * VERTEX_COUNT + gx;
                int bottomRight = bottomLeft + 1;
                indices[index++] = topLeft;
                indices[index++] = bottomLeft;
                indices[index++] = topRight;
                indices[index++] = topRight;
                indices[index++] = bottomLeft;
                indices[index++] = bottomRight;
            }
        }
        ModelData data = new ModelData(vertices, textures, normals, indices);
        return new TexturedModel(data, texture);
    }
}
