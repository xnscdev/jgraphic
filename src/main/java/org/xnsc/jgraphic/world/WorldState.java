package org.xnsc.jgraphic.world;

import org.joml.Vector3f;
import org.xnsc.jgraphic.render.ObjectShader;

import java.util.List;

public record WorldState(Camera camera, List<LightSource> lights, Vector3f skyColor, float fogDensity, float fogGradient, float ambientThreshold) {
    public void loadToShader(ObjectShader shader) {
        shader.loadSkyFog(skyColor, fogDensity, fogGradient);
        shader.loadLightSources(lights);
        shader.loadViewMatrix(camera);
        shader.setAmbientThreshold(ambientThreshold);
    }
}
