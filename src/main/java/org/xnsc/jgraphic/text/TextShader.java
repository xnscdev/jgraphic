package org.xnsc.jgraphic.text;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.xnsc.jgraphic.render.Shader;

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

    protected void loadTranslation(Vector2f translation) {
        loadVector2f(locTranslation, translation);
    }

    protected void loadColor(Vector3f color) {
        loadVector3f(locColor, color);
    }
}
