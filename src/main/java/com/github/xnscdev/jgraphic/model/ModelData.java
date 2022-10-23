package com.github.xnscdev.jgraphic.model;

import org.joml.Vector3f;

public record ModelData(float[] vertices, float[] textures, float[] normals, int[] indices, AnimatedModelData animated,
                        Vector3f aabbMin, Vector3f aabbMax, ModelMaterial material) {
    public ModelData(float[] vertices, float[] textures, float[] normals, int[] indices) {
        this(vertices, textures, normals, indices, null, null, null, null);
    }
}
