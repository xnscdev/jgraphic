package com.github.xnscdev.jgraphic.model;

import org.joml.Vector4f;
import org.lwjgl.assimp.AIColor4D;
import org.lwjgl.assimp.AIMaterial;
import org.lwjgl.assimp.AIString;
import org.lwjgl.system.MemoryStack;

import java.io.File;
import java.nio.IntBuffer;

import static org.lwjgl.assimp.Assimp.*;

public class ModelMaterial {
    private static final Vector4f DEFAULT_COLOR = new Vector4f(0, 0, 0, 1);
    private final AIMaterial material;
    private Vector4f ambientColor;
    private Vector4f diffuseColor;
    private Vector4f specularColor;
    private float reflectance;
    private String texturePath;
    private int index;

    public ModelMaterial(AIMaterial material) {
        this.material = material;
        try (MemoryStack stack = MemoryStack.stackPush()) {
            AIColor4D ambient = AIColor4D.create();
            if (aiGetMaterialColor(material, AI_MATKEY_COLOR_AMBIENT, aiTextureType_NONE, 0, ambient) != 0)
                throw new RuntimeException(aiGetErrorString());
            ambientColor = new Vector4f(ambient.r(), ambient.g(), ambient.b(), ambient.a());
            AIColor4D diffuse = AIColor4D.create();
            if (aiGetMaterialColor(material, AI_MATKEY_COLOR_DIFFUSE, aiTextureType_NONE, 0, diffuse) != 0)
                throw new RuntimeException(aiGetErrorString());
            Vector4f tempDiffuseColor = new Vector4f(diffuse.r(), diffuse.g(), diffuse.b(), diffuse.a());
            AIColor4D specular = AIColor4D.create();
            if (aiGetMaterialColor(material, AI_MATKEY_COLOR_SPECULAR, aiTextureType_NONE, 0, specular) != 0)
                throw new RuntimeException(aiGetErrorString());
            specularColor = new Vector4f(specular.r(), specular.g(), specular.b(), specular.a());

            float reflectance = 0;
            float[] shininess = new float[]{0};
            int[] pMax = new int[]{1};
            int ret = aiGetMaterialFloatArray(material, AI_MATKEY_SHININESS_STRENGTH, aiTextureType_NONE, 0, shininess, pMax);
            if (ret != aiReturn_SUCCESS)
                reflectance = shininess[0];
            this.reflectance = reflectance;

            AIString texturePath = AIString.calloc(stack);
            aiGetMaterialTexture(material, aiTextureType_DIFFUSE, 0, texturePath, (IntBuffer) null, null, null, null, null, null);
            String path = texturePath.dataString();
            if (path.length() > 0) {
                this.texturePath = new File(path).getName();
                tempDiffuseColor = DEFAULT_COLOR;
            }
            diffuseColor = tempDiffuseColor;
        }
    }

    public AIMaterial getMaterial() {
        return material;
    }

    public Vector4f getAmbientColor() {
        return ambientColor;
    }

    public void setAmbientColor(Vector4f ambientColor) {
        this.ambientColor = ambientColor;
    }

    public Vector4f getDiffuseColor() {
        return diffuseColor;
    }

    public void setDiffuseColor(Vector4f diffuseColor) {
        this.diffuseColor = diffuseColor;
    }

    public Vector4f getSpecularColor() {
        return specularColor;
    }

    public void setSpecularColor(Vector4f specularColor) {
        this.specularColor = specularColor;
    }

    public float getReflectance() {
        return reflectance;
    }

    public void setReflectance(float reflectance) {
        this.reflectance = reflectance;
    }

    public String getTexturePath() {
        return texturePath;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
