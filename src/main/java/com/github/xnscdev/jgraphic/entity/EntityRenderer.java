package com.github.xnscdev.jgraphic.entity;

import com.github.xnscdev.jgraphic.model.ModelMesh;
import org.joml.Matrix4f;
import com.github.xnscdev.jgraphic.util.MathUtils;
import com.github.xnscdev.jgraphic.render.Shader;
import com.github.xnscdev.jgraphic.world.World;
import com.github.xnscdev.jgraphic.world.WorldState;

import java.util.List;
import java.util.Map;

import static org.lwjgl.opengl.GL30.*;

public class EntityRenderer {
    private final EntityShader shader = new EntityShader();

    public EntityRenderer() {
        shader.start();
        shader.loadProjectionMatrix(World.PROJECTION);
        Shader.stop();
    }

    public void clean() {
        shader.clean();
    }

    public void render(WorldState state, Map<EntityModel, List<Entity>> entities) {
        shader.start();
        state.loadToShader(shader);
        for (EntityModel model : entities.keySet()) {
            for (EntityMesh mesh : model.getMeshes()) {
                bindMesh(mesh);
                List<Entity> batch = entities.get(model);
                for (Entity entity : batch) {
                    bindEntity(entity);
                    glDrawElements(GL_TRIANGLES, mesh.getVertexCount(), GL_UNSIGNED_INT, 0);
                }
                unbindMesh();
            }
        }
        Shader.stop();
    }

    public void enableCulling() {
        glCullFace(GL_BACK);
        glEnable(GL_CULL_FACE);
    }

    public void disableCulling() {
        glDisable(GL_CULL_FACE);
    }

    private void bindMesh(EntityMesh mesh) {
        glBindVertexArray(mesh.getVAO());
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);
        glEnableVertexAttribArray(2);
        if (mesh.isTransparent())
            disableCulling();
        shader.loadFakeLighting(mesh.isFakeLighting());
        shader.loadSpecularLighting(mesh.getReflectivity(), mesh.getShineDamper());
        shader.loadMaterial(mesh.getMaterial(), mesh.hasTexture());
        mesh.preRender();
    }

    private void unbindMesh() {
        enableCulling();
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glDisableVertexAttribArray(2);
        glBindVertexArray(0);
    }

    private void bindEntity(Entity entity) {
        Matrix4f transformMatrix = MathUtils.transformMatrix(entity.getPosition(), entity.getRx(), entity.getRy(), entity.getRz(), entity.getScale());
        shader.loadTransformMatrix(transformMatrix);
    }
}
