package org.xnsc.jgraphic.world;

import org.joml.Vector3f;

public record WorldState(Camera camera, LightSource light, Vector3f skyColor, float fogDensity, float fogGradient, float ambientThreshold) {
    public void loadToShader(ObjectShader shader) {
        shader.loadSkyFog(skyColor, fogDensity, fogGradient);
        shader.loadLightSource(light);
        shader.loadViewMatrix(camera);
    }
}
