package com.github.xnscdev.jgraphic.gui;

import com.github.xnscdev.jgraphic.util.DisplayManager;
import org.joml.Vector2f;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;

public class Gui implements GuiComponent {
    private final GuiBackground background;
    protected final List<GuiComponent> children = new ArrayList<>();
    protected Vector2f position;
    protected Vector2f screenPosition;
    protected Vector2f textPosition;
    protected Vector2f size;
    protected Vector2f screenSize;

    private Gui(GuiModel model, Vector2f position, Vector2f size) {
        background = new GuiBackground(model);
        setPosition(position);
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
    }

    public Vector2f getPosition() {
        return position;
    }

    public void setPosition(Vector2f position) {
        this.position = position;
        this.screenPosition = new Vector2f(position.x / DisplayManager.getWidth() * 2 - 1, position.y / DisplayManager.getHeight() * -2 + 1);
        this.textPosition = new Vector2f(position.x / DisplayManager.getWidth() * 2, position.y / DisplayManager.getHeight() * -2);
    }

    public Vector2f getScreenPosition() {
        return screenPosition;
    }

    public Vector2f getTextPosition() {
        return textPosition;
    }

    public Vector2f getSize() {
        return size;
    }

    public void setSize(Vector2f size) {
        this.size = size;
        this.screenSize = new Vector2f(size.x / DisplayManager.getWidth() * 2, size.y / DisplayManager.getHeight() * 2);
    }

    public Vector2f getScreenSize() {
        return screenSize;
    }

    @Override
    public void tick(double delta) {
        for (GuiComponent gui : children)
            gui.tick(delta);
    }

    @Override
    public void render(Vector2f translation) {
        background.render(screenPosition, screenSize);
        for (GuiComponent gui : children)
            gui.render(new Vector2f(translation).add(textPosition));
    }
}
