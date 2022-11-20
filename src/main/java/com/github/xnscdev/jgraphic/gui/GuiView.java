package com.github.xnscdev.jgraphic.gui;

import com.github.xnscdev.jgraphic.util.DisplayManager;
import org.joml.Vector2f;

/**
 * Represents a rectangular area where GUI components should be displayed. This class is normally used when
 * child GUIs are expected to cross the boundaries of the GUI and any out-of-bounds regions of child GUIs should
 * be hidden from view.
 * @param pos top-left corner of the area in device coordinates
 * @param size size of the area in device coordinates
 * @param texturePos top-left corner of the region in the texture to render the visible part of the GUI
 * @param textureSize size of the region in the texture texture to render the visible part of the GUI
 * @author XNSC
 */
public record GuiView(Vector2f pos, Vector2f size, Vector2f texturePos, Vector2f textureSize) {
    /**
     * Determines the visible area of a child GUI based on the positions and sizes of the child and parent GUIs.
     * @param childPos top-left corner of the child GUI in device coordinates relative to the parent GUI
     * @param childSize size of the child GUI in device coordinates
     * @param screenPos top-left corner of the parent GUI in device coordinates relative to the origin
     * @return the visible area of the child GUI
     */
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

    /**
     * Calculates the position of the top-left corner of the area in viewport coordinates.
     * @return the position vector
     */
    public Vector2f screenPos() {
        return new Vector2f(pos.x * 2 / DisplayManager.getWidth() - 1, pos.y * -2 / DisplayManager.getHeight() + 1);
    }

    /**
     * Calculates the size of the area in viewport coordinates.
     * @return the size vector
     */
    public Vector2f screenSize() {
        return new Vector2f(size.x * 2 / DisplayManager.getWidth(), size.y * 2 / DisplayManager.getHeight());
    }
}
