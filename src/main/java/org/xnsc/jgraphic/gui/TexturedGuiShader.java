package org.xnsc.jgraphic.gui;

import org.joml.Matrix4f;
import org.xnsc.jgraphic.world.Shader;

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
