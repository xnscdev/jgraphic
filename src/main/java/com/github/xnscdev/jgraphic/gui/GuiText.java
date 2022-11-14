package com.github.xnscdev.jgraphic.gui;

import com.github.xnscdev.jgraphic.render.Shader;
import com.github.xnscdev.jgraphic.text.FontType;
import com.github.xnscdev.jgraphic.text.TextModel;
import com.github.xnscdev.jgraphic.util.DisplayManager;
import org.joml.Vector2f;
import org.joml.Vector3f;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

public class GuiText extends GuiComponent {
    private final float fontSize;
    private final float lineMaxSize;
    private final FontType font;
    private final boolean centered;
    private TextModel model;
    private String text;
    private Vector3f color = new Vector3f();
    private float width = 0.45f;
    private float edge = 0.1f;
    private Vector3f borderColor = new Vector3f();
    private Vector2f borderOffset = new Vector2f();
    private float borderWidth = 0;
    private float borderEdge = 0.1f;
    private int lineCount;

    public GuiText(String text, float fontSize, FontType font, Vector2f position, float lineMaxSize, boolean centered) {
        super(position);
        this.text = text;
        this.fontSize = fontSize;
        this.font = font;
        this.lineMaxSize = lineMaxSize / DisplayManager.getWidth();
        this.centered = centered;
        model = font.loadText(this);
    }

    @Override
    public void render(Vector2f screenOffset) {
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glDisable(GL_DEPTH_TEST);
        GuiManager.TEXT_SHADER.start();
        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, font.getTextureAtlas());
        glBindVertexArray(model.getVAO());
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);
        GuiManager.TEXT_SHADER.loadTranslation(new Vector2f(screenOffset).add(screenPosition).add(1, -1));
        GuiManager.TEXT_SHADER.loadForeground(color, width, edge);
        GuiManager.TEXT_SHADER.loadBorder(borderColor, borderOffset, borderWidth, borderEdge);
        glDrawArrays(GL_TRIANGLES, 0, model.getVertexCount());
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glBindVertexArray(0);
        Shader.stop();
        glDisable(GL_BLEND);
        glEnable(GL_DEPTH_TEST);
    }

    public FontType getFont() {
        return font;
    }

    public TextModel getModel() {
        return model;
    }

    public Vector2f getTextSize() {
        return model.getTextSize();
    }

    public Vector3f getColor() {
        return color;
    }

    public void setColor(Vector3f color) {
        this.color = color;
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public float getEdge() {
        return edge;
    }

    public void setEdge(float edge) {
        this.edge = edge;
    }

    public Vector3f getBorderColor() {
        return borderColor;
    }

    public void setBorderColor(Vector3f borderColor) {
        this.borderColor = borderColor;
    }

    public Vector2f getBorderOffset() {
        return borderOffset;
    }

    public void setBorderOffset(Vector2f borderOffset) {
        this.borderOffset = borderOffset;
    }

    public float getBorderWidth() {
        return borderWidth;
    }

    public void setBorderWidth(float borderWidth) {
        this.borderWidth = borderWidth;
    }

    public float getBorderEdge() {
        return borderEdge;
    }

    public void setBorderEdge(float borderEdge) {
        this.borderEdge = borderEdge;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        if (!this.text.equals(text)) {
            this.text = text;
            model = font.loadText(this);
        }
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
