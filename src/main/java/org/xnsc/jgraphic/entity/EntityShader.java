package org.xnsc.jgraphic.entity;

import org.joml.Vector2f;
import org.xnsc.jgraphic.render.ObjectShader;

public class EntityShader extends ObjectShader {
    private int locFakeLighting;
    private int locTextureRows;
    private int locTextureOffset;

    public EntityShader() {
        super("entity");
    }

    @Override
    protected void getUniforms() {
        super.getUniforms();
        locFakeLighting = getUniform("fake_lighting");
        locTextureRows = getUniform("texture_rows");
        locTextureOffset = getUniform("texture_offset");
    }

    public void loadFakeLighting(boolean use) {
        loadBoolean(locFakeLighting, use);
    }

    public void setTextureRows(float rows) {
        loadFloat(locTextureRows, rows);
    }

    public void setTextureOffset(float x, float y) {
        loadVector2f(locTextureOffset, new Vector2f(x, y));
    }
}
