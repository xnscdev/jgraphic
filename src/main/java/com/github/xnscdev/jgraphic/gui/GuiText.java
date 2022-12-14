package com.github.xnscdev.jgraphic.gui;

import com.github.xnscdev.jgraphic.render.Shader;
import com.github.xnscdev.jgraphic.render.TextureData;
import com.github.xnscdev.jgraphic.util.MathUtils;
import com.github.xnscdev.jgraphic.util.ObjectManager;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

import static org.lwjgl.opengl.GL30.*;

/**
 * A string of text that can be displayed as a two-dimensional GUI component.
 * @author XNSC
 */
public class GuiText extends GuiComponent {
    private final int fontSize;
    private final float lineMaxSize;
    private final String font;
    private final boolean centered;
    private TextureData textureData;
    private String text;
    private Vector3f color = new Vector3f();

    /**
     * Creates a new left-aligned text string.
     * @param text the text to render
     * @param fontSize the font size of the text, which may not equal the GUI height
     * @param font name of the font to use when rendering the text
     * @param position position in device coordinates relative to the parent GUI
     */
    public GuiText(String text, int fontSize, String font, Vector2f position) {
        super(position);
        this.text = text;
        this.fontSize = fontSize;
        this.font = font;
        this.lineMaxSize = 0;
        this.centered = false;
        textureData = ObjectManager.loadText(text, fontSize, font);
        setSize(new Vector2f(textureData.getWidth(), textureData.getHeight()));
    }

    /**
     * Creates a new centered text string.
     * @param text the text to render
     * @param fontSize the font size of the text, which may not equal the GUI height
     * @param font name of the font to use when rendering the text
     * @param position position in device coordinates relative to the parent GUI
     * @param lineMaxSize length of the text string, used to calculate the centered position of the text
     */
    public GuiText(String text, int fontSize, String font, Vector2f position, float lineMaxSize) {
        super(position);
        this.text = text;
        this.fontSize = fontSize;
        this.font = font;
        this.lineMaxSize = lineMaxSize;
        this.centered = true;
        textureData = ObjectManager.loadText(text, fontSize, font);
        setSize(new Vector2f(textureData.getWidth(), textureData.getHeight()));
    }

    @Override
    public void render(GuiView view, Vector2f screenPos) {
        Vector2f pos = position;
        if (centered)
            pos = new Vector2f(pos).add((lineMaxSize - size.x) / 2, 0);
        GuiView newView = view.getVisibleArea(pos, size, screenPos);
        if (newView == null)
            return;
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glDisable(GL_DEPTH_TEST);
        GuiManager.TEXT_SHADER.start();
        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, textureData.getTexture());
        glBindVertexArray(GuiManager.RECT_MODEL.getVAO());
        glEnableVertexAttribArray(0);
        Matrix4f transformMatrix = MathUtils.transformMatrix(newView.screenPos(), newView.screenSize());
        GuiManager.TEXT_SHADER.loadTransformMatrix(transformMatrix);
        GuiManager.TEXT_SHADER.loadView(newView);
        GuiManager.TEXT_SHADER.loadForegroundColor(color);
        glDrawArrays(GL_TRIANGLES, 0, GuiManager.RECT_MODEL.getVertexCount());
        glDisableVertexAttribArray(0);
        glBindVertexArray(0);
        Shader.stop();
        glDisable(GL_BLEND);
        glEnable(GL_DEPTH_TEST);
    }

    public String getFont() {
        return font;
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

    public void setText(String text) {
        if (!this.text.equals(text)) {
            this.text = text;
            glDeleteTextures(textureData.getTexture());
            textureData = ObjectManager.loadText(text, fontSize, font);
            setSize(new Vector2f(textureData.getWidth(), textureData.getHeight()));
        }
    }

    public int getFontSize() {
        return fontSize;
    }

    public float getLineMaxSize() {
        return lineMaxSize;
    }

    public boolean isCentered() {
        return centered;
    }
}
