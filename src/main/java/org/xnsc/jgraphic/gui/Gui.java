package org.xnsc.jgraphic.gui;

import org.joml.Vector2f;
import org.xnsc.jgraphic.util.DisplayManager;

public class Gui {
    protected final Vector2f position;
    protected final Vector2f size;

    public Gui(Vector2f position, Vector2f size) {
        this.position = new Vector2f(position.x / DisplayManager.getWidth() * 2 - 1, position.y / DisplayManager.getHeight() * -2 + 1);
        this.size = new Vector2f(size.x / DisplayManager.getWidth() * 2, size.y / DisplayManager.getHeight() * 2);
    }

    public Vector2f getPosition() {
        return position;
    }

    public Vector2f getSize() {
        return size;
    }

    public void render() {
    }
}
