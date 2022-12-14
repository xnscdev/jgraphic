package com.github.xnscdev.jgraphic.gui;

import org.joml.Vector2f;
import org.joml.Vector4f;

import static org.lwjgl.glfw.GLFW.*;

/**
 * A rectangular text box component that allows the user to edit and delete text.
 * @author XNSC
 */
public class TextBox extends Gui {
    private final GuiText text;
    private String value = "";

    /**
     * Creates a new text box.
     * @param position position in device coordinates relative to the parent GUI
     * @param size size in device coordinates
     * @param font font for displaying typed text
     */
    public TextBox(Vector2f position, Vector2f size, String font) {
        super(GuiManager.RECT_MODEL, position, size, new Vector4f(1));
        text = new GuiText(value, (int) (size.y * 0.5f), font, new Vector2f(size.y * 0.25f));
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
    public boolean mouseReleased(float x, float y) {
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
