package com.github.xnscdev.jgraphic.gui;

import com.github.xnscdev.jgraphic.util.DisplayManager;
import org.joml.Vector2f;

public abstract class GuiComponent {
    protected GuiComponent parent;
    protected Vector2f position;
    protected Vector2f screenPosition;
    protected Vector2f size = new Vector2f();
    protected Vector2f screenSize = new Vector2f();

    protected GuiComponent(Vector2f position) {
        setPosition(position);
    }

    public abstract void render();

    public GuiComponent getParent() {
        return parent;
    }

    public void setParent(GuiComponent parent) {
        this.parent = parent;
    }

    public Vector2f getPosition() {
        return position;
    }

    public void setPosition(Vector2f position) {
        this.position = position;
        this.screenPosition = new Vector2f(position.x / DisplayManager.getWidth() * 2 - 0.5f, position.y / DisplayManager.getHeight() * -2 + 0.5f);
    }

    public Vector2f getScreenPosition() {
        return screenPosition;
    }

    public Vector2f getSize() {
        return size;
    }

    public void setSize(Vector2f size) {
        this.size = size;
        this.screenSize = new Vector2f(size.x / DisplayManager.getWidth() * 2, size.y / DisplayManager.getHeight() * 2);
    }

    public Vector2f getScreenSize() {
        return screenSize;
    }

    public void tick(double delta) {
    }
}
