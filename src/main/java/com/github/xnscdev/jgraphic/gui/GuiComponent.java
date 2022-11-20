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

    public abstract void render(GuiView view, Vector2f offset);

    public final GuiComponent getParent() {
        return parent;
    }

    public final void setParent(GuiComponent parent) {
        this.parent = parent;
    }

    public final Vector2f getPosition() {
        return position;
    }

    public Vector2f getAbsolutePosition() {
        return parent.getAbsolutePosition().add(position);
    }

    public final void setPosition(Vector2f position) {
        this.position = position;
        this.screenPosition = new Vector2f(position.x / DisplayManager.getWidth() * 2, position.y / DisplayManager.getHeight() * -2);
    }

    public final Vector2f getScreenPosition() {
        return screenPosition;
    }

    public Vector2f getAbsoluteScreenPosition() {
        return parent.getAbsoluteScreenPosition().add(screenPosition);
    }

    public final Vector2f getSize() {
        return size;
    }

    public final void setSize(Vector2f size) {
        this.size = size;
        this.screenSize = new Vector2f(size.x / DisplayManager.getWidth() * 2, size.y / DisplayManager.getHeight() * 2);
    }

    public final Vector2f getScreenSize() {
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

    public void unfocused() {
    }

    public void receiveKey(int key, int action, int mods) {
    }

    public void receiveChar(int codepoint) {
    }

    public final void takeFocus() {
        if (GuiManager.focusedComponent != null)
            GuiManager.focusedComponent.unfocused();
        GuiManager.focusedComponent = this;
    }

    public final void dropFocus() {
        unfocused();
        GuiManager.focusedComponent = null;
    }

    public final boolean containsAbsolutePoint(float x, float y) {
        Vector2f pos = getAbsolutePosition();
        return x >= pos.x && x <= pos.x + size.x && y >= pos.y && y <= pos.y + size.y;
    }
}
