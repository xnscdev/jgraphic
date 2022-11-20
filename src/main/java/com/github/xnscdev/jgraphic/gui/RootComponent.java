package com.github.xnscdev.jgraphic.gui;

import org.joml.Vector2f;

public class RootComponent extends GuiComponent {
    protected RootComponent() {
        super(new Vector2f());
    }

    @Override
    public void render(GuiView view, Vector2f screenPos) {
    }

    @Override
    public Vector2f getAbsolutePosition() {
        return new Vector2f();
    }

    @Override
    public Vector2f getAbsoluteScreenPosition() {
        return new Vector2f(-1, 1);
    }
}
