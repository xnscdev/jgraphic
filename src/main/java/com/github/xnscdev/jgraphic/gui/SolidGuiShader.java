package com.github.xnscdev.jgraphic.gui;

import com.github.xnscdev.jgraphic.render.Shader;
import org.joml.Matrix4f;
import org.joml.Vector4f;

public class SolidGuiShader extends Shader {
    private int locTransformMatrix;
    private int locColor;

    public SolidGuiShader() {
        super("solid_gui");
    }

    @Override
    protected void getUniforms() {
        locTransformMatrix = getUniform("transform_matrix");
        locColor = getUniform("color");
    }

    public void loadTransformMatrix(Matrix4f value) {
        loadMatrix4f(locTransformMatrix, value);
    }

    public void setColor(Vector4f color) {
        loadVector4f(locColor, color);
    }
}
