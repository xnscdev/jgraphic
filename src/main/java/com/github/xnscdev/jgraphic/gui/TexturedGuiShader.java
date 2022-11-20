package com.github.xnscdev.jgraphic.gui;

import com.github.xnscdev.jgraphic.render.Shader;
import org.joml.Matrix4f;

public class TexturedGuiShader extends Shader {
    private int locTransformMatrix;
    private int locTexturePos;
    private int locTextureSize;

    public TexturedGuiShader() {
        super("textured_gui");
    }

    @Override
    protected void getUniforms() {
        locTransformMatrix = getUniform("transform_matrix");
        locTexturePos = getUniform("texture_pos");
        locTextureSize = getUniform("texture_size");
    }

    public void loadTransformMatrix(Matrix4f value) {
        loadMatrix4f(locTransformMatrix, value);
    }

    public void loadView(GuiView view) {
        loadVector2f(locTexturePos, view.texturePos());
        loadVector2f(locTextureSize, view.textureSize());
    }
}
