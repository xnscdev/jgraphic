package org.xnsc.jgraphic.gui;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.xnsc.jgraphic.util.DisplayManager;

public class DebugGui extends SolidGui {
    public DebugGui() {
        super(new Vector2f(), new Vector2f(DisplayManager.getWidth(), 50), new Vector3f(0.6f));
    }

    @Override
    public void render() {
    }
}
