package org.xnsc.jgraphic.gui;

import org.joml.Vector2f;
import org.joml.Vector3f;

public class SolidGui extends Gui {
    private final Vector3f backgroundColor;

    public SolidGui(Vector2f position, Vector2f size, Vector3f backgroundColor) {
        super(position, size);
        this.backgroundColor = backgroundColor;
    }

    public Vector3f getBackgroundColor() {
        return backgroundColor;
    }
}
