package org.xnsc.jworld.render.model;

import org.joml.Vector3f;

public class ModelVertex {
    private static final int NO_INDEX = -1;
    private final Vector3f position;
    private final int index;
    private final float length;
    private int textureIndex = NO_INDEX;
    private int normalIndex = NO_INDEX;
    private ModelVertex duplicateVertex = null;

    public ModelVertex(int index, Vector3f position) {
        this.index = index;
        this.position = position;
        this.length = position.length();
    }

    public Vector3f getPosition() {
        return position;
    }

    public int getIndex() {
        return index;
    }

    public float getLength() {
        return length;
    }

    public int getTextureIndex() {
        return textureIndex;
    }

    public void setTextureIndex(int textureIndex) {
        this.textureIndex = textureIndex;
    }

    public int getNormalIndex() {
        return normalIndex;
    }

    public void setNormalIndex(int normalIndex) {
        this.normalIndex = normalIndex;
    }

    public ModelVertex getDuplicateVertex() {
        return duplicateVertex;
    }

    public void setDuplicateVertex(ModelVertex duplicateVertex) {
        this.duplicateVertex = duplicateVertex;
    }

    public boolean isSet() {
        return textureIndex != NO_INDEX && normalIndex != NO_INDEX;
    }

    public boolean sameTextureNormal(int otherTexture, int otherNormal) {
        return textureIndex == otherTexture && normalIndex == otherNormal;
    }
}
