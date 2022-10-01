package org.xnsc.jworld.render.terrain;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.xnsc.jworld.render.Camera;
import org.xnsc.jworld.render.LightSource;
import org.xnsc.jworld.render.Shader;
import org.xnsc.jworld.render.World;
import org.xnsc.jworld.render.model.RawModel;
import org.xnsc.jworld.render.util.MatrixUtils;

import java.util.List;

import static org.lwjgl.opengl.GL30.*;

public class TerrainRenderer {
    private final TerrainShader shader = new TerrainShader();

    public TerrainRenderer() {
        shader.start();
        shader.loadProjectionMatrix(World.PROJECTION);
        Shader.stop();
    }

    public void clean() {
        shader.clean();
    }

    public void render(Camera camera, LightSource light, List<TerrainPiece> terrains) {
        shader.start();
        shader.loadSkyFog();
        shader.loadLightSource(light);
        shader.loadViewMatrix(camera);
        for (TerrainPiece terrain : terrains) {
            bindModel(terrain.getModel());
            bindTerrainPiece(terrain);
            glDrawElements(GL_TRIANGLES, terrain.getModel().getVertexCount(), GL_UNSIGNED_INT, 0);
            unbindModel(terrain.getModel());
        }
        Shader.stop();
    }

    private void bindModel(RawModel model) {
        glBindVertexArray(model.getVAO());
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);
        glEnableVertexAttribArray(2);
        shader.loadSpecularLighting(model.getReflectivity(), model.getShineDamper());
        model.preRender();
    }

    private void unbindModel(RawModel model) {
        model.postRender();
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glDisableVertexAttribArray(2);
        glBindVertexArray(0);
    }

    private void bindTerrainPiece(TerrainPiece terrain) {
        Matrix4f transformMatrix = MatrixUtils.transformMatrix(new Vector3f(terrain.getX(), 0, terrain.getZ()), 0, 0, 0, 1);
        shader.loadTransformMatrix(transformMatrix);
    }
}
