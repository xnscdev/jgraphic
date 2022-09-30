package org.xnsc.jworld.render;

public class RawModel {
    private final int vao;
    private final int vertexCount;

    public RawModel(float[] vertices) {
        vao = ObjectManager.createVAO();
        vertexCount = vertices.length / 3;
        ObjectManager.storeVertices(0, vertices);
        ObjectManager.unbindVAO();
    }

    public int getVAO() {
        return vao;
    }

    public int getVertexCount() {
        return vertexCount;
    }
}
