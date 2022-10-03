package org.xnsc.jgraphic.gui;

import org.joml.Vector2f;
import org.xnsc.jgraphic.util.ObjectManager;

import static org.lwjgl.opengl.GL13.*;

public class TexturedGui extends Gui {
    private final int texture;

    public TexturedGui(Vector2f position, Vector2f size, String texture) {
        super(position, size);
        this.texture = ObjectManager.createTexture(texture);
    }

    public void loadTexture() {
        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, texture);
    }

    public int getTexture() {
        return texture;
    }
}
