package com.github.xnscdev.jgraphic.gui;

import com.github.xnscdev.jgraphic.render.Shader;
import org.joml.Matrix4f;

public class TexturedGuiShader extends Shader {
    private int locTransformMatrix;

    public TexturedGuiShader() {
        super("textured_gui");
    }

    @Override
    protected void getUniforms() {
        locTransformMatrix = getUniform("transform_matrix");
    }

    public void loadTransformMatrix(Matrix4f value) {
        loadMatrix4f(locTransformMatrix, value);
    }
}
