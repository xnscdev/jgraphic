package org.xnsc.jworld.render.shader;

import org.joml.Matrix4f;
import org.xnsc.jworld.render.Camera;

public class StaticShader extends Shader {
    private int locTransformMatrix;
    private int locProjectionMatrix;
    private int locViewMatrix;

    public StaticShader() {
        super("static");
    }

    @Override
    protected void getUniforms() {
        locTransformMatrix = getUniform("transform_matrix");
        locProjectionMatrix = getUniform("projection_matrix");
        locViewMatrix = getUniform("view_matrix");
    }

    public void loadTransformMatrix(Matrix4f value) {
        loadMatrix4f(locTransformMatrix, value);
    }

    public void loadProjectionMatrix(Matrix4f value) {
        loadMatrix4f(locProjectionMatrix, value);
    }

    public void loadViewMatrix(Camera camera) {
        loadMatrix4f(locViewMatrix, camera.viewMatrix());
    }
}
