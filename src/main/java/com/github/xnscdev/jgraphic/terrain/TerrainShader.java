package com.github.xnscdev.jgraphic.terrain;

import com.github.xnscdev.jgraphic.render.ObjectShader;

public class TerrainShader extends ObjectShader {
    private int locBackgroundSampler;
    private int locRedSampler;
    private int locGreenSampler;
    private int locBlueSampler;
    private int locBlendMapSampler;
    private int locTextureScale;

    public TerrainShader() {
        super("terrain");
    }

    @Override
    protected void getUniforms() {
        super.getUniforms();
        locTextureScale = getUniform("texture_scale");
        locBackgroundSampler = getUniform("background_sampler");
        locRedSampler = getUniform("red_sampler");
        locGreenSampler = getUniform("green_sampler");
        locBlueSampler = getUniform("blue_sampler");
        locBlendMapSampler = getUniform("blend_map_sampler");
    }

    public void loadTextureUnits() {
        loadInt(locBackgroundSampler, 0);
        loadInt(locRedSampler, 1);
        loadInt(locGreenSampler, 2);
        loadInt(locBlueSampler, 3);
        loadInt(locBlendMapSampler, 4);
    }

    public void setTextureScale(float scale) {
        loadFloat(locTextureScale, scale);
    }
}
