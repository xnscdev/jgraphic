package com.github.xnscdev.jgraphic.model;

import com.github.xnscdev.jgraphic.util.ObjectManager;

public class ModelMesh {
    private final int vao;
    private final int vertexCount;
    private boolean transparent = false;
    private boolean fakeLighting = false;

    public ModelMesh(ModelData data) {
        vao = ObjectManager.createVAO();
        vertexCount = data.indices().length;
        ObjectManager.storeAttribute(0, 3, data.vertices());
        ObjectManager.storeAttribute(1, 2, data.textures());
        ObjectManager.storeAttribute(2, 3, data.normals());
        ObjectManager.storeIndices(data.indices());
        ObjectManager.unbindVAO();
    }

    public ModelMesh(float[] vertices, int dimensions) {
        vao = ObjectManager.createVAO();
        vertexCount = vertices.length / dimensions;
        ObjectManager.storeAttribute(0, dimensions, vertices);
        ObjectManager.unbindVAO();
    }

    public ModelMesh(float[] vertices, float[] textures, int dimensions) {
        vao = ObjectManager.createVAO();
        vertexCount = vertices.length / dimensions;
        ObjectManager.storeAttribute(0, dimensions, vertices);
        ObjectManager.storeAttribute(1, 2, textures);
        ObjectManager.unbindVAO();
    }

    public int getVAO() {
        return vao;
    }

    public int getVertexCount() {
        return vertexCount;
    }

    public boolean isTransparent() {
        return transparent;
    }

    public void setTransparent(boolean transparent) {
        this.transparent = transparent;
    }

    public boolean isFakeLighting() {
        return fakeLighting;
    }

    public void setFakeLighting(boolean fakeLighting) {
        this.fakeLighting = fakeLighting;
    }

    public void preRender() {
    }
}
