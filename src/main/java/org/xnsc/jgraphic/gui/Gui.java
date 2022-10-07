package org.xnsc.jgraphic.gui;

import org.joml.Vector2f;
import org.xnsc.jgraphic.util.DisplayManager;
import org.xnsc.jgraphic.util.MathUtils;

import java.util.ArrayList;
import java.util.List;

public class Gui {
    protected final List<GuiText> texts = new ArrayList<>();
    protected Vector2f position;
    protected Vector2f screenPosition;
    protected Vector2f textPosition;
    protected Vector2f size;
    protected Vector2f screenSize;

    public Gui(Vector2f position, Vector2f size) {
        setPosition(position);
        setSize(size);
    }

    public List<GuiText> getTexts() {
        return texts;
    }

    public void addText(GuiText text) {
        texts.add(text);
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
}
