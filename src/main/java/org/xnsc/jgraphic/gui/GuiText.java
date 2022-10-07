package org.xnsc.jgraphic.gui;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.xnsc.jgraphic.text.FontType;
import org.xnsc.jgraphic.text.TextModel;
import org.xnsc.jgraphic.util.DisplayManager;
import org.xnsc.jgraphic.util.MathUtils;

public class GuiText {
    private final String text;
    private final float fontSize;
    private final Vector2f position;
    private final float lineMaxSize;
    private final FontType font;
    private final boolean centered;
    private final TextModel model;
    private Vector3f color = new Vector3f();
    private int lineCount;

    public GuiText(String text, float fontSize, FontType font, Vector2f position, float lineMaxSize, boolean centered) {
        this.text = text;
        this.fontSize = fontSize;
        this.font = font;
        this.position = new Vector2f(position.x / DisplayManager.getWidth() * 2, position.y / DisplayManager.getHeight() * -2);
        this.lineMaxSize = lineMaxSize / DisplayManager.getWidth();
        this.centered = centered;
        this.model = font.loadText(this);
    }

    public FontType getFont() {
        return font;
    }

    public Vector2f getPosition() {
        return position;
    }

    public TextModel getModel() {
        return model;
    }

    public Vector3f getColor() {
        return color;
    }

    public void setColor(Vector3f color) {
        this.color = color;
    }

    public String getText() {
        return text;
    }

    public float getFontSize() {
        return fontSize;
    }

    public float getLineMaxSize() {
        return lineMaxSize;
    }

    public boolean isCentered() {
        return centered;
    }

    public int getLineCount() {
        return lineCount;
    }

    public void setLineCount(int lineCount) {
        this.lineCount = lineCount;
    }
}
