package com.github.xnscdev.jgraphic.gui;

import org.joml.Vector2f;
import org.joml.Vector4f;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public class Gui extends GuiComponent {
    protected final GuiBackground background;
    protected final List<GuiComponent> children = new ArrayList<>();

    private Gui(GuiModel model, Vector2f position, Vector2f size) {
        super(position);
        background = new GuiBackground(model);
        setSize(size);
    }

    public Gui(GuiModel model, Vector2f position, Vector2f size, Vector4f backgroundColor) {
        this(model, position, size);
        background.setSolidColor(backgroundColor);
    }

    public Gui(GuiModel model, Vector2f position, Vector2f size, String texture) {
        this(model, position, size);
        background.setTexture(texture);
    }

    public void setSolidColor(Vector4f backgroundColor) {
        background.setSolidColor(backgroundColor);
    }

    public void setTexture(String texture) {
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
    public void render(GuiView view, Vector2f screenPos) {
        GuiView newView = background.render(this, view, screenPos);
        if (newView == null)
            return;
        Vector2f newScreenPos = new Vector2f(screenPos).add(position);
        for (GuiComponent gui : children) {
            gui.render(newView, newScreenPos);
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
