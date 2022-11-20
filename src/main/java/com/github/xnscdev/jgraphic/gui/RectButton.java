package com.github.xnscdev.jgraphic.gui;

import org.joml.Vector2f;
import org.joml.Vector4f;

/**
 * A rectangular button GUI component that executes code when clicked.
 * @author XNSC
 */
public class RectButton extends Gui {
    private final Runnable callback;
    private final GuiText labelText;

    /**
     * Creates a new button with a solid-color background.
     * @param label text to display on the button
     * @param position position in device coordinates relative to the parent GUI
     * @param size size in device coordinates
     * @param font font for displaying the label text
     * @param backgroundColor the background color
     * @param callback function to execute when the button is clicked
     */
    public RectButton(String label, Vector2f position, Vector2f size, String font, Vector4f backgroundColor, Runnable callback) {
        super(GuiManager.RECT_MODEL, position, size, backgroundColor);
        this.callback = callback;
        int fontSize = (int) (size.y * 0.5f);
        labelText = new GuiText(label, fontSize, font, new Vector2f(), size.x);
        labelText.setPosition(new Vector2f(0, (size.y - labelText.getSize().y) / 2));
        addChild(labelText);
    }

    /**
     * Creates a new button with a textured background.
     * @param label text to display on the button
     * @param position position in device coordinates relative to the parent GUI
     * @param size size in device coordinates
     * @param font font for displaying the label text
     * @param texture name of the background texture
     * @param callback function to execute when the button is clicked
     */
    public RectButton(String label, Vector2f position, Vector2f size, String font, String texture, Runnable callback) {
        super(GuiManager.RECT_MODEL, position, size, texture);
        this.callback = callback;
        int fontSize = (int) (size.y * 0.5f);
        labelText = new GuiText(label, fontSize, font, new Vector2f(0, size.y * 0.25f), size.x);
        addChild(labelText);
    }

    public void setLabelText(String label) {
        labelText.setText(label);
    }

    @Override
    public boolean mousePressed(float x, float y) {
        return true;
    }

    @Override
    public boolean mouseReleased(float x, float y) {
        callback.run();
        return true;
    }
}

