package org.xnsc.jworld.render;

public class RawModel {
    private final int vao;
    private final int vertexCount;

    public RawModel(float[] vertices, int[] indices) {
        vao = ObjectManager.createVAO();
        vertexCount = indices.length;
        ObjectManager.storeVertices(0, vertices);
        ObjectManager.storeIndices(indices);
        ObjectManager.unbindVAO();
    }

    public int getVAO() {
        return vao;
    }

    public int getVertexCount() {
        return vertexCount;
    }
}
