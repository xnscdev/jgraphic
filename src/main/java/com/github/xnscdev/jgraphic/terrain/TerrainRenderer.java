package com.github.xnscdev.jgraphic.terrain;

import com.github.xnscdev.jgraphic.model.ModelMesh;
import com.github.xnscdev.jgraphic.render.Shader;
import com.github.xnscdev.jgraphic.world.WorldState;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import com.github.xnscdev.jgraphic.util.MathUtils;
import com.github.xnscdev.jgraphic.world.World;

import java.util.List;

import static org.lwjgl.opengl.GL30.*;

public class TerrainRenderer {
    private final TerrainShader shader = new TerrainShader();

    public TerrainRenderer() {
        shader.start();
        shader.loadProjectionMatrix(World.PROJECTION);
        shader.loadTextureUnits();
        Shader.stop();
    }

    public void clean() {
        shader.clean();
    }

    public void render(WorldState state, List<TerrainPiece> terrains) {
        shader.start();
        state.loadToShader(shader);
        for (TerrainPiece terrain : terrains) {
            bindMesh(terrain.getModel());
            bindTerrainPiece(terrain);
            glDrawElements(GL_TRIANGLES, terrain.getModel().getVertexCount(), GL_UNSIGNED_INT, 0);
            unbindMesh();
        }
        Shader.stop();
    }

    private void bindMesh(ModelMesh model) {
        glBindVertexArray(model.getVAO());
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);
        glEnableVertexAttribArray(2);
        shader.loadSpecularLighting(0);
        model.preRender();
    }

    private void unbindMesh() {
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glDisableVertexAttribArray(2);
        glBindVertexArray(0);
    }

    private void bindTerrainPiece(TerrainPiece terrain) {
        Matrix4f transformMatrix = MathUtils.transformMatrix(new Vector3f(terrain.getX(), 0, terrain.getZ()), 0, 0, 0, 1);
        shader.loadTransformMatrix(transformMatrix);
        shader.setTextureScale(terrain.getSize() / 10);
    }
}
