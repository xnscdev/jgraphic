package com.github.xnscdev.jgraphic.model;

import com.github.xnscdev.jgraphic.util.ObjectManager;
import com.github.xnscdev.jgraphic.util.OBJLoader;

import static org.lwjgl.opengl.GL13.*;

public class TexturedModel extends RawModel {
    private final int texture;
    private int rows = 1;

    public TexturedModel(ModelData data, String texture) {
        super(data);
        this.texture = ObjectManager.createTexture(texture);
    }

    public TexturedModel(String texture) {
        this(OBJLoader.loadModel(texture), texture);
    }

    public int getTexture() {
        return texture;
    }

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    @Override
    public void preRender() {
        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, texture);
    }
}
