package com.github.xnscdev.jgraphic.entity;

import org.joml.Vector2f;
import com.github.xnscdev.jgraphic.render.ObjectShader;

public class EntityShader extends ObjectShader {
    private int locFakeLighting;

    public EntityShader() {
        super("entity");
    }

    @Override
    protected void getUniforms() {
        super.getUniforms();
        locFakeLighting = getUniform("fake_lighting");
    }

    public void loadFakeLighting(boolean use) {
        loadBoolean(locFakeLighting, use);
    }
}
