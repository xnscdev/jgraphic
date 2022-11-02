package com.github.xnscdev.jgraphic.text;

import com.github.xnscdev.jgraphic.render.Shader;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class TextShader extends Shader {
    private int locTranslation;
    private int locColor;
    private int locWidth;
    private int locEdge;
    private int locBorderColor;
    private int locBorderOffset;
    private int locBorderWidth;
    private int locBorderEdge;

    public TextShader() {
        super("text");
    }

    @Override
    protected void getUniforms() {
        locTranslation = getUniform("translation");
        locColor = getUniform("color");
        locWidth = getUniform("width");
        locEdge = getUniform("edge");
        locBorderColor = getUniform("border_color");
        locBorderOffset = getUniform("border_offset");
        locBorderWidth = getUniform("border_width");
        locBorderEdge = getUniform("border_edge");
    }

    public void loadTranslation(Vector2f translation) {
        loadVector2f(locTranslation, translation);
    }

    public void loadForeground(Vector3f color, float width, float edge) {
        loadVector3f(locColor, color);
        loadFloat(locWidth, width);
        loadFloat(locEdge, edge);
    }

    public void loadBorder(Vector3f color, Vector2f offset, float width, float edge) {
        loadVector3f(locBorderColor, color);
        loadVector2f(locBorderOffset, offset);
        loadFloat(locBorderWidth, width);
        loadFloat(locBorderEdge, edge);
    }
}