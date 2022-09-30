package org.xnsc.jworld.render;

import static org.lwjgl.opengl.GL13.*;

public class TexturedModel extends RawModel {
    private final int texture;

    public TexturedModel(float[] vertices, float[] textures, int[] indices, String texture) {
        super(vertices, textures, indices);
        this.texture = ObjectManager.createTexture("/textures/" + texture);
    }

    @Override
    public void preRender() {
        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, texture);
    }

    @Override
    public void postRender() {
    }
}
