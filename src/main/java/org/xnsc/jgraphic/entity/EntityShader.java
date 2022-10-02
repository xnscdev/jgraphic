package org.xnsc.jgraphic.entity;

import org.xnsc.jgraphic.world.ObjectShader;

public class EntityShader extends ObjectShader {
    private int locFakeLighting;
    private int locAmbientThreshold;

    public EntityShader() {
        super("entity");
    }

    @Override
    protected void getUniforms() {
        super.getUniforms();
        locFakeLighting = getUniform("fake_lighting");
        locAmbientThreshold = getUniform("ambient_threshold");
    }

    public void loadFakeLighting(boolean use) {
        loadBoolean(locFakeLighting, use);
    }

    public void setAmbientThreshold(float threshold) {
        loadFloat(locAmbientThreshold, threshold);
    }
}
