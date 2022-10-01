package org.xnsc.jworld.render.entity;

import org.xnsc.jworld.render.ObjectShader;

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
