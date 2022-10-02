package org.xnsc.jgraphic.model;

import org.xnsc.jgraphic.util.ObjectManager;

public class RawModel {
    private final int vao;
    private final int vertexCount;
    private float reflectivity = 0;
    private float shineDamper = 1;
    private boolean transparent = false;
    private boolean fakeLighting = false;

    public RawModel(ModelData data) {
        vao = ObjectManager.createVAO();
        vertexCount = data.indices().length;
        ObjectManager.storeAttribute(0, 3, data.vertices());
        ObjectManager.storeAttribute(1, 2, data.textures());
        ObjectManager.storeAttribute(2, 3, data.normals());
        ObjectManager.storeIndices(data.indices());
        ObjectManager.unbindVAO();
    }

    public int getVAO() {
        return vao;
    }

    public int getVertexCount() {
        return vertexCount;
    }

    public float getReflectivity() {
        return reflectivity;
    }

    public void setReflectivity(float reflectivity) {
        this.reflectivity = reflectivity;
    }

    public float getShineDamper() {
        return shineDamper;
    }

    public void setShineDamper(float shineDamper) {
        this.shineDamper = shineDamper;
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

    public void preRender() {}

    public void postRender() {}
}
