package org.xnsc.jgraphic.terrain;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.xnsc.jgraphic.model.RawModel;
import org.xnsc.jgraphic.util.MatrixUtils;
import org.xnsc.jgraphic.world.Shader;
import org.xnsc.jgraphic.world.World;
import org.xnsc.jgraphic.world.WorldState;

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

    public void render(WorldState state, List<TerrainPiece> terrains) {
        shader.start();
        state.loadToShader(shader);
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
        shader.setTextureScale(terrain.getSize() / 10);
    }
}
