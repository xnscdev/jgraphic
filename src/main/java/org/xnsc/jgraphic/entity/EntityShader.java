package org.xnsc.jgraphic.entity;

import org.xnsc.jgraphic.world.ObjectShader;

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
