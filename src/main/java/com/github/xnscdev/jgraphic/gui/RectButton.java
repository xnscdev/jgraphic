package com.github.xnscdev.jgraphic.gui;

import com.github.xnscdev.jgraphic.text.FontType;
import org.joml.Vector2f;
import org.joml.Vector4f;

public class RectButton extends Gui {
    private final Runnable callback;
    private final GuiText labelText;

    public RectButton(String label, Vector2f position, Vector2f size, FontType font, Vector4f backgroundColor, Runnable callback) {
        super(GuiManager.RECT_MODEL, position, size, backgroundColor);
        this.callback = callback;
        float fontSize = size.y * 0.6f;
        labelText = new GuiText(label, fontSize, font, new Vector2f(0, size.y * 0.2f), size.x, true);
        addChild(labelText);
    }

    public RectButton(String label, Vector2f position, Vector2f size, FontType font, String texture, Runnable callback) {
        super(GuiManager.RECT_MODEL, position, size, texture);
        this.callback = callback;
        float fontSize = size.y * 0.6f;
        labelText = new GuiText(label, fontSize, font, new Vector2f(0, size.y * 0.2f), size.x, true);
        addChild(labelText);
    }

    public void setLabelText(String label) {
        labelText.setText(label);
    }

    @Override
    public boolean mouseReleased(float x, float y) {
        callback.run();
        return true;
    }
}
