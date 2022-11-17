package com.github.xnscdev.jgraphic.text;

import com.github.xnscdev.jgraphic.render.Shader;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class TextShader extends Shader {
    private int locTransformMatrix;
    private int locColor;

    public TextShader() {
        super("text");
    }

    @Override
    protected void getUniforms() {
        locTransformMatrix = getUniform("transform_matrix");
        locColor = getUniform("color");
    }

    public void loadTransformMatrix(Matrix4f transformMatrix) {
        loadMatrix4f(locTransformMatrix, transformMatrix);
    }

    public void loadForeground(Vector3f color) {
        loadVector3f(locColor, color);
    }
}