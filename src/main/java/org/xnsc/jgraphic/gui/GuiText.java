package org.xnsc.jgraphic.gui;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.xnsc.jgraphic.render.Shader;
import org.xnsc.jgraphic.text.FontType;
import org.xnsc.jgraphic.text.TextModel;
import org.xnsc.jgraphic.util.DisplayManager;

import java.util.Map;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

public class GuiText implements GuiComponent {
    private final float fontSize;
    private final Vector2f position;
    private final float lineMaxSize;
    private final FontType font;
    private final boolean centered;
    private TextModel model;
    private String text;
    private Vector3f color = new Vector3f();
    private int lineCount;

    public GuiText(String text, float fontSize, FontType font, Vector2f position, float lineMaxSize, boolean centered) {
        this.text = text;
        this.fontSize = fontSize;
        this.font = font;
        this.position = new Vector2f(position.x / DisplayManager.getWidth() * 2, position.y / DisplayManager.getHeight() * -2);
        this.lineMaxSize = lineMaxSize / DisplayManager.getWidth();
        this.centered = centered;
        model = font.loadText(this);
    }

    @Override
    public void render(Vector2f translation) {
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glDisable(GL_DEPTH_TEST);
        GuiManager.TEXT_SHADER.start();
        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, font.getTextureAtlas());
        glBindVertexArray(model.getVAO());
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);
        GuiManager.TEXT_SHADER.loadTranslation(new Vector2f(translation).add(position));
        GuiManager.TEXT_SHADER.loadColor(color);
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

    public void setText(String text) {
        this.text = text;
        model = font.loadText(this);
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
