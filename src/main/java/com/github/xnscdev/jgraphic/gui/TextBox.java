package com.github.xnscdev.jgraphic.gui;

import com.github.xnscdev.jgraphic.text.FontType;
import org.joml.Vector2f;
import org.joml.Vector4f;

import static org.lwjgl.glfw.GLFW.*;

public class TextBox extends Gui {
    private final GuiText text;
    private String value = "";

    public TextBox(Vector2f position, Vector2f size, FontType font) {
        super(GuiManager.RECT_MODEL, position, size, new Vector4f(1));
        text = new GuiText(value, size.y * 0.6f, font, new Vector2f(size.y * 0.2f), size.x - size.y * 0.4f, false);
        addChild(text);
    }

    public void setText(String text) {
        this.value = text;
        this.text.setText(text);
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean mousePressed(float x, float y) {
        takeFocus();
        return true;
    }

    @Override
    public void unfocused() {
    }

    @Override
    public void receiveKey(int key, int action, int mods) {
        if (key == GLFW_KEY_ENTER && action == GLFW_PRESS)
            dropFocus();
        else if (!value.isEmpty() && key == GLFW_KEY_BACKSPACE && (action == GLFW_PRESS || action == GLFW_REPEAT)) {
            value = value.substring(0, value.length() - 1);
            text.setText(value);
        }
    }

    @Override
    public void receiveChar(int codepoint) {
        value += Character.toString(codepoint);
        text.setText(value);
    }
}
