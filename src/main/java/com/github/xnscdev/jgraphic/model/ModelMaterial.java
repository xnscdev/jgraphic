package com.github.xnscdev.jgraphic.model;

import org.lwjgl.assimp.AIColor4D;
import org.lwjgl.assimp.AIMaterial;

import static org.lwjgl.assimp.Assimp.*;

public class ModelMaterial {
    private final AIMaterial material;
    private final AIColor4D ambientColor;
    private final AIColor4D diffuseColor;
    private final AIColor4D specularColor;

    public ModelMaterial(AIMaterial material) {
        this.material = material;
        ambientColor = AIColor4D.create();
        if (aiGetMaterialColor(material, AI_MATKEY_COLOR_AMBIENT, aiTextureType_NONE, 0, ambientColor) != 0)
            throw new RuntimeException(aiGetErrorString());
        diffuseColor = AIColor4D.create();
        if (aiGetMaterialColor(material, AI_MATKEY_COLOR_DIFFUSE, aiTextureType_NONE, 0, diffuseColor) != 0)
            throw new RuntimeException(aiGetErrorString());
        specularColor = AIColor4D.create();
        if (aiGetMaterialColor(material, AI_MATKEY_COLOR_SPECULAR, aiTextureType_NONE, 0, specularColor) != 0)
            throw new RuntimeException(aiGetErrorString());
    }

    public AIMaterial getMaterial() {
        return material;
    }

    public AIColor4D getAmbientColor() {
        return ambientColor;
    }

    public AIColor4D getDiffuseColor() {
        return diffuseColor;
    }

    public AIColor4D getSpecularColor() {
        return specularColor;
    }
}
