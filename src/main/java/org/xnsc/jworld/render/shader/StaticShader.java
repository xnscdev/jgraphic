package org.xnsc.jworld.render.shader;

import org.joml.Matrix4f;
import org.xnsc.jworld.render.Camera;
import org.xnsc.jworld.render.LightSource;

public class StaticShader extends Shader {
    private int locTransformMatrix;
    private int locProjectionMatrix;
    private int locViewMatrix;
    private int locLightPos;
    private int locLightColor;
    private int locReflectivity;
    private int locShineDamper;

    public StaticShader() {
        super("static");
    }

    @Override
    protected void getUniforms() {
        locTransformMatrix = getUniform("transform_matrix");
        locProjectionMatrix = getUniform("projection_matrix");
        locViewMatrix = getUniform("view_matrix");
        locLightPos = getUniform("light_pos");
        locLightColor = getUniform("light_color");
        locReflectivity = getUniform("reflectivity");
        locShineDamper = getUniform("shine_damper");
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

    public void loadLightSource(LightSource source) {
        loadVector3f(locLightPos, source.getPosition());
        loadVector3f(locLightColor, source.getColor());
    }

    public void loadSpecularLighting(float reflectivity, float shineDamper) {
        loadFloat(locReflectivity, reflectivity);
        loadFloat(locShineDamper, shineDamper);
    }
}
