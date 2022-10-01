package org.xnsc.jworld.render.model;

import org.xnsc.jworld.render.util.ObjectManager;

import static org.lwjgl.opengl.GL13.*;

public class TexturedModel extends RawModel {
    private final int texture;

    public TexturedModel(ModelData data, String texture) {
        super(data);
        this.texture = ObjectManager.createTexture(texture);
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
