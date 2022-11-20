package com.github.xnscdev.jgraphic.gui;

import org.joml.Vector2f;
import org.joml.Vector4f;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

/**
 * Represents a GUI. A standard GUI consists of a solid-filled or textured shape as the background with its
 * contents rendered in front of it. This class provides builtin support for child GUI components, unlike
 * the base {@link GuiComponent} class.
 * @author XNSC
 * @see GuiComponent
 */
public class Gui extends GuiComponent {
    protected final GuiBackground background;
    protected final List<GuiComponent> children = new ArrayList<>();

    private Gui(GuiModel model, Vector2f position, Vector2f size) {
        super(position);
        background = new GuiBackground(model);
        setSize(size);
    }

    /**
     * Creates a new GUI with a solid-color background.
     * @param model the shape of the background
     * @param position position of the GUI in device coordinates relative to its parent
     * @param size size of the GUI in pixels
     * @param backgroundColor the background color
     */
    public Gui(GuiModel model, Vector2f position, Vector2f size, Vector4f backgroundColor) {
        this(model, position, size);
        background.setSolidColor(backgroundColor);
    }

    /**
     * Creates a new GUI with a textured background. The texture file should be in the appropriate directory.
     * @param model the shape of the background
     * @param position position of the GUI in device coordinates relative to its parent
     * @param size size of the GUI in pixels
     * @param texture name of the texture
     */
    public Gui(GuiModel model, Vector2f position, Vector2f size, String texture) {
        this(model, position, size);
        background.setTexture(texture);
    }

    /**
     * Changes the GUI's background to a solid color.
     * @param backgroundColor the background color
     */
    public void setSolidColor(Vector4f backgroundColor) {
        background.setSolidColor(backgroundColor);
    }

    /**
     * Changes the GUI's background to a texture.
     * @param texture name of the texture
     */
    public void setTexture(String texture) {
        background.setTexture(texture);
    }

    public List<GuiComponent> getChildren() {
        return children;
    }

    /**
     * Adds a child GUI component. When a GUI is rendered, the background is rendered first, then all children
     * are rendered in the order they were added.
     * @param gui the child GUI to add
     */
    public void addChild(GuiComponent gui) {
        children.add(gui);
        gui.setParent(this);
    }

    /**
     * Removes a child GUI component. If the specified GUI is not a child of this GUI, no action is performed.
     * @param gui the child GUI to remove
     */
    public void removeChild(GuiComponent gui) {
        if (children.remove(gui))
            gui.setParent(null);
    }

    @Override
    public void tick(double delta) {
        for (GuiComponent gui : children)
            gui.tick(delta);
    }

    @Override
    public void render(GuiView view, Vector2f screenPos) {
        GuiView newView = background.render(this, view, screenPos);
        if (newView == null)
            return;
        Vector2f newScreenPos = new Vector2f(screenPos).add(position);
        for (GuiComponent gui : children) {
            gui.render(view, newScreenPos);
        }
    }

    @Override
    public boolean mousePressed(float x, float y) {
        ListIterator<GuiComponent> iter = children.listIterator(children.size());
        while (iter.hasPrevious()) {
            GuiComponent gui = iter.previous();
            if (gui.containsAbsolutePoint(x, y) && gui.mousePressed(x, y))
                return true;
        }
        return false;
    }

    @Override
    public boolean mouseReleased(float x, float y) {
        ListIterator<GuiComponent> iter = children.listIterator(children.size());
        while (iter.hasPrevious()) {
            GuiComponent gui = iter.previous();
            if (gui.containsAbsolutePoint(x, y) && gui.mouseReleased(x, y))
                return true;
        }
        GuiManager.resetFocusedComponent();
        return false;
    }
}
