package org.xnsc.jgraphic.terrain;

import org.xnsc.jgraphic.world.ObjectShader;

public class TerrainShader extends ObjectShader {
    private int locTextureScale;

    public TerrainShader() {
        super("terrain");
    }

    @Override
    protected void getUniforms() {
        super.getUniforms();
        locTextureScale = getUniform("texture_scale");
    }

    public void setTextureScale(float scale) {
        loadFloat(locTextureScale, scale);
    }
}
