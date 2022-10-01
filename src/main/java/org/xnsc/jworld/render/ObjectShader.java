package org.xnsc.jworld.render;

import org.joml.Matrix4f;

public class ObjectShader extends Shader {
    private int locTransformMatrix;
    private int locProjectionMatrix;
    private int locViewMatrix;
    private int locLightPos;
    private int locLightColor;
    private int locReflectivity;
    private int locShineDamper;
    private int locFogDensity;
    private int locFogGradient;
    private int locSkyColor;

    public ObjectShader(String name) {
        super(name);
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
        locFogDensity = getUniform("fog_density");
        locFogGradient = getUniform("fog_gradient");
        locSkyColor = getUniform("sky_color");
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

    public void loadSkyFog() {
        loadFloat(locFogDensity, World.FOG_DENSITY);
        loadFloat(locFogGradient, World.FOG_GRADIENT);
        loadVector3f(locSkyColor, World.SKY_COLOR);
    }
}
