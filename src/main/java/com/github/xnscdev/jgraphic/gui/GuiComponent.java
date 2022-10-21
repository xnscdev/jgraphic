package com.github.xnscdev.jgraphic.gui;

import org.joml.Vector2f;

public interface GuiComponent {
    void render(Vector2f translation);

    default void tick(double delta) {
    }
}
