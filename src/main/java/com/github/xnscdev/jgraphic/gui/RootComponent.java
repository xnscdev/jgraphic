package com.github.xnscdev.jgraphic.gui;

import org.joml.Vector2f;

/**
 * Represents the root component in the JGraphic GUI system. The root component is not rendered and serves as
 * the parent of all top-level GUI components added to the GUI manager. This class should never be used directly.
 * @author XNSC
 * @see GuiManager#addGui(GuiComponent)
 */
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
