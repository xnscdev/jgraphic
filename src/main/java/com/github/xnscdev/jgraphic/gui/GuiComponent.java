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

    public abstract void render(Vector2f screenOffset);

    public GuiComponent getParent() {
        return parent;
    }

    public void setParent(GuiComponent parent) {
        this.parent = parent;
    }

    public Vector2f getPosition() {
        return position;
    }

    public Vector2f getAbsolutePosition() {
        return parent.getAbsolutePosition().add(position);
    }

    public void setPosition(Vector2f position) {
        this.position = position;
        this.screenPosition = new Vector2f(position.x / DisplayManager.getWidth() * 2, position.y / DisplayManager.getHeight() * -2);
    }

    public Vector2f getScreenPosition() {
        return screenPosition;
    }

    public Vector2f getAbsoluteScreenPosition() {
        return parent.getAbsoluteScreenPosition().add(screenPosition);
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

    public boolean mousePressed(float x, float y) {
        return false;
    }

    public boolean mouseReleased(float x, float y) {
        return false;
    }

    public boolean containsPoint(float x, float y) {
        Vector2f pos = getAbsolutePosition();
        return x >= pos.x && x <= pos.x + size.x && y >= pos.y && y <= pos.y + size.y;
    }
}
