package com.github.xnscdev.jgraphic.gui;

import com.github.xnscdev.jgraphic.render.Shader;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class TextShader extends Shader {
    private int locTransformMatrix;
    private int locTexturePos;
    private int locTextureSize;
    private int locColor;

    public TextShader() {
        super("text");
    }

    @Override
    protected void getUniforms() {
        locTransformMatrix = getUniform("transform_matrix");
        locTexturePos = getUniform("texture_pos");
        locTextureSize = getUniform("texture_size");
        locColor = getUniform("color");
    }

    public void loadTransformMatrix(Matrix4f transformMatrix) {
        loadMatrix4f(locTransformMatrix, transformMatrix);
    }

    public void loadView(GuiView view) {
        loadVector2f(locTexturePos, view.texturePos());
        loadVector2f(locTextureSize, view.textureSize());
    }

    public void loadForegroundColor(Vector3f color) {
        loadVector3f(locColor, color);
    }
}