package com.github.xnscdev.jgraphic.text;

import com.github.xnscdev.jgraphic.render.Shader;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class TextShader extends Shader {
    private int locTranslation;
    private int locColor;

    public TextShader() {
        super("text");
    }

    @Override
    protected void getUniforms() {
        locTranslation = getUniform("translation");
        locColor = getUniform("color");
    }

    public void loadTranslation(Vector2f translation) {
        loadVector2f(locTranslation, translation);
    }

    public void loadColor(Vector3f color) {
        loadVector3f(locColor, color);
    }
}
