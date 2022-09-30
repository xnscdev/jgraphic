package org.xnsc.jworld.render;

public class RawModel {
    private final int vao;
    private final int vertexCount;

    public RawModel(float[] vertices, float[] textures, int[] indices) {
        vao = ObjectManager.createVAO();
        vertexCount = indices.length;
        ObjectManager.storeAttribute(0, 3, vertices);
        ObjectManager.storeAttribute(1, 2, textures);
        ObjectManager.storeIndices(indices);
        ObjectManager.unbindVAO();
    }

    public int getVAO() {
        return vao;
    }

    public int getVertexCount() {
        return vertexCount;
    }

    public void preRender() {
    }

    public void postRender() {
    }
}
