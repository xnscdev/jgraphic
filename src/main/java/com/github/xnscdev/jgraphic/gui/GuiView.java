package com.github.xnscdev.jgraphic.gui;

import com.github.xnscdev.jgraphic.util.DisplayManager;
import org.joml.Vector2f;

public record GuiView(Vector2f pos, Vector2f size, Vector2f texturePos, Vector2f textureSize) {
    public GuiView getVisibleArea(Vector2f childPos, Vector2f childSize, Vector2f screenPos) {
        Vector2f realPos = new Vector2f(screenPos).add(childPos);
        if (realPos.x + childSize.x < pos.x || realPos.x > pos.x + size.x || realPos.y + childSize.y < pos.y || realPos.y > pos.y + size.y)
            return null;
        Vector2f newPos = new Vector2f(realPos);
        Vector2f newSize = new Vector2f(childSize);
        Vector2f texturePos = new Vector2f();
        Vector2f textureSize = new Vector2f(1);
        if (realPos.x < pos.x) {
            float diff = pos.x - realPos.x;
            newPos.x += diff;
            newSize.x -= diff;
            texturePos.x += diff / childSize.x;
            textureSize.x -= diff / childSize.x;
        }
        if (realPos.x + childSize.x > pos.x + size.x) {
            float diff = realPos.x + childSize.x - pos.x - size.x;
            newSize.x -= diff;
            textureSize.x -= diff / childSize.x;
        }
        if (realPos.y < pos.y) {
            float diff = pos.y - realPos.y;
            newPos.y += diff;
            newSize.y -= diff;
            texturePos.y += diff / childSize.y;
            textureSize.y -= diff / childSize.y;
        }
        if (realPos.y + childSize.y > pos.y + size.y) {
            float diff = realPos.y + childSize.y - pos.y - size.y;
            newSize.y -= diff;
            textureSize.y -= diff / childSize.y;
        }
        return new GuiView(newPos, newSize, texturePos, textureSize);
    }

    public Vector2f screenPos() {
        return new Vector2f(pos.x * 2 / DisplayManager.getWidth() - 1, pos.y * -2 / DisplayManager.getHeight() + 1);
    }

    public Vector2f screenSize() {
        return new Vector2f(size.x * 2 / DisplayManager.getWidth(), size.y * 2 / DisplayManager.getHeight());
    }
}
