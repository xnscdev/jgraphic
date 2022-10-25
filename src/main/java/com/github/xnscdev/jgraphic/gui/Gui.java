package com.github.xnscdev.jgraphic.gui;

import org.joml.Vector2f;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public class Gui extends GuiComponent {
    private final GuiBackground background;
    protected final List<GuiComponent> children = new ArrayList<>();

    private Gui(GuiModel model, Vector2f position, Vector2f size) {
        super(position);
        background = new GuiBackground(model);
        setSize(size);
    }

    public Gui(GuiModel model, Vector2f position, Vector2f size, Vector3f backgroundColor) {
        this(model, position, size);
        background.setSolidColor(backgroundColor);
    }

    public Gui(GuiModel model, Vector2f position, Vector2f size, String texture) {
        this(model, position, size);
        background.setTexture(texture);
    }

    public List<GuiComponent> getChildren() {
        return children;
    }

    public void addChild(GuiComponent gui) {
        children.add(gui);
        gui.setParent(this);
    }

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
    public void render(Vector2f screenOffset) {
        Vector2f newOffset = new Vector2f(screenOffset).add(screenPosition);
        background.render(newOffset, screenSize);
        for (GuiComponent gui : children) {
            gui.render(newOffset);
        }
    }

    @Override
    public boolean mousePressed(float x, float y) {
        ListIterator<GuiComponent> iter = children.listIterator(children.size());
        while (iter.hasPrevious()) {
            GuiComponent gui = iter.previous();
            if (gui.containsPoint(x, y) && gui.mousePressed(x, y))
                return true;
        }
        return false;
    }

    @Override
    public boolean mouseReleased(float x, float y) {
        ListIterator<GuiComponent> iter = children.listIterator(children.size());
        while (iter.hasPrevious()) {
            GuiComponent gui = iter.previous();
            if (gui.containsPoint(x, y) && gui.mouseReleased(x, y))
                return true;
        }
        return false;
    }
}
