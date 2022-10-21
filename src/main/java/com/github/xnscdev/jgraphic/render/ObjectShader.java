package com.github.xnscdev.jgraphic.render;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import com.github.xnscdev.jgraphic.world.Camera;
import com.github.xnscdev.jgraphic.world.LightSource;

import java.util.List;

public class ObjectShader extends Shader {
    private static final int MAX_LIGHTS = 4;
    private int locTransformMatrix;
    private int locProjectionMatrix;
    private int locViewMatrix;
    private int[] locLightPos;
    private int[] locLightColor;
    private int[] locAttenuation;
    private int locReflectivity;
    private int locShineDamper;
    private int locFogDensity;
    private int locFogGradient;
    private int locSkyColor;
    private int locAmbientThreshold;

    public ObjectShader(String name) {
        super(name);
    }

    @Override
    protected void getUniforms() {
        locTransformMatrix = getUniform("transform_matrix");
        locProjectionMatrix = getUniform("projection_matrix");
        locViewMatrix = getUniform("view_matrix");
        locReflectivity = getUniform("reflectivity");
        locShineDamper = getUniform("shine_damper");
        locFogDensity = getUniform("fog_density");
        locFogGradient = getUniform("fog_gradient");
        locSkyColor = getUniform("sky_color");
        locAmbientThreshold = getUniform("ambient_threshold");

        locLightPos = new int[MAX_LIGHTS];
        locLightColor = new int[MAX_LIGHTS];
        locAttenuation = new int[MAX_LIGHTS];
        for (int i = 0; i < MAX_LIGHTS; i++) {
            locLightPos[i] = getUniform("light_pos[" + i + "]");
            locLightColor[i] = getUniform("light_color[" + i + "]");
            locAttenuation[i] = getUniform("attenuation[" + i + "]");
        }
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

    public void loadLightSources(List<LightSource> lights) {
        for (int i = 0; i < MAX_LIGHTS; i++) {
            if (i < lights.size()) {
                loadVector3f(locLightPos[i], lights.get(i).getPosition());
                loadVector3f(locLightColor[i], lights.get(i).getColor());
                loadVector3f(locAttenuation[i], lights.get(i).getAttenuation());
            }
            else {
                loadVector3f(locLightPos[i], new Vector3f());
                loadVector3f(locLightColor[i], new Vector3f());
                loadVector3f(locAttenuation[i], new Vector3f(1, 0, 0));
            }
        }
    }

    public void loadSpecularLighting(float reflectivity, float shineDamper) {
        loadFloat(locReflectivity, reflectivity);
        loadFloat(locShineDamper, shineDamper);
    }

    public void loadSkyFog(Vector3f skyColor, float fogDensity, float fogGradient) {
        loadFloat(locFogDensity, fogDensity);
        loadFloat(locFogGradient, fogGradient);
        loadVector3f(locSkyColor, skyColor);
    }

    public void setAmbientThreshold(float threshold) {
        loadFloat(locAmbientThreshold, threshold);
    }
}
