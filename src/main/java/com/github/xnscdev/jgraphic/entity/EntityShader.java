package com.github.xnscdev.jgraphic.entity;

import com.github.xnscdev.jgraphic.model.ModelMaterial;
import org.joml.Vector2f;
import com.github.xnscdev.jgraphic.render.ObjectShader;

public class EntityShader extends ObjectShader {
    private int locFakeLighting;
    private int locMaterialUseTexture;
    private int locMaterialColor;

    public EntityShader() {
        super("entity");
    }

    @Override
    protected void getUniforms() {
        super.getUniforms();
        locFakeLighting = getUniform("fake_lighting");
        locMaterialUseTexture = getUniform("material.use_texture");
        locMaterialColor = getUniform("material.color");
    }

    public void loadFakeLighting(boolean use) {
        loadBoolean(locFakeLighting, use);
    }

    public void loadMaterial(ModelMaterial material, boolean useTexture) {
        loadBoolean(locMaterialUseTexture, useTexture);
        loadVector4f(locMaterialColor, material.getDiffuseColor());
    }
}
