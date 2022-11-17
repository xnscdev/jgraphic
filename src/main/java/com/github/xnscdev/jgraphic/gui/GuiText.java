package com.github.xnscdev.jgraphic.gui;

import com.github.xnscdev.jgraphic.render.Shader;
import com.github.xnscdev.jgraphic.render.TextureData;
import com.github.xnscdev.jgraphic.util.DisplayManager;
import com.github.xnscdev.jgraphic.util.MathUtils;
import com.github.xnscdev.jgraphic.util.ObjectManager;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

import static org.lwjgl.opengl.GL30.*;

public class GuiText extends GuiComponent {
    private final int fontSize;
    private final float lineMaxSize;
    private final String font;
    private final boolean centered;
    private TextureData textureData;
    private String text;
    private Vector3f color = new Vector3f();
    private int lineCount;

    public GuiText(String text, int fontSize, String font, Vector2f position, float lineMaxSize, boolean centered) {
        super(position);
        this.text = text;
        this.fontSize = fontSize;
        this.font = font;
        this.lineMaxSize = lineMaxSize / DisplayManager.getWidth();
        this.centered = centered;
        textureData = ObjectManager.loadText(text, fontSize, font);
        setSize(new Vector2f(textureData.getWidth(), textureData.getHeight()));
    }

    @Override
    public void render(Vector2f screenOffset) {
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glDisable(GL_DEPTH_TEST);
        GuiManager.TEXT_SHADER.start();
        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, textureData.getTexture());
        glBindVertexArray(GuiManager.RECT_MODEL.getVAO());
        glEnableVertexAttribArray(0);
        Vector2f pos = new Vector2f(screenOffset).add(screenPosition);
        if (centered) {
            float padding = lineMaxSize - screenSize.x / 2;
            if (padding > 0)
                pos.x += padding;
        }
        Matrix4f transformMatrix = MathUtils.transformMatrix(pos, screenSize);
        GuiManager.TEXT_SHADER.loadTransformMatrix(transformMatrix);
        GuiManager.TEXT_SHADER.loadForeground(color);
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

    public int getLineCount() {
        return lineCount;
    }

    public void setLineCount(int lineCount) {
        this.lineCount = lineCount;
    }
}
