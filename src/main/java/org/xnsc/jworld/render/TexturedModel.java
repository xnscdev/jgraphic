package org.xnsc.jworld.render;

import static org.lwjgl.opengl.GL13.*;

public class TexturedModel extends RawModel {
    private final int texture;

    public TexturedModel(float[] vertices, float[] textures, float[] normals, int[] indices, int texture) {
        super(vertices, textures, normals, indices);
        this.texture = texture;
    }

    public int getTexture() {
        return texture;
    }

    @Override
    public void preRender() {
        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, texture);
    }
}
