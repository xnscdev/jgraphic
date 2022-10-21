package com.github.xnscdev.jgraphic.entity;

import com.github.xnscdev.jgraphic.model.RawModel;
import org.joml.Matrix4f;
import com.github.xnscdev.jgraphic.model.TexturedModel;
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

    public void render(WorldState state, Map<TexturedModel, List<Entity>> entities) {
        shader.start();
        state.loadToShader(shader);
        for (TexturedModel model : entities.keySet()) {
            bindModel(model);
            List<Entity> batch = entities.get(model);
            for (Entity entity : batch) {
                bindEntity(entity);
                glDrawElements(GL_TRIANGLES, model.getVertexCount(), GL_UNSIGNED_INT, 0);
            }
            unbindModel(model);
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

    private void bindModel(TexturedModel model) {
        glBindVertexArray(model.getVAO());
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);
        glEnableVertexAttribArray(2);
        shader.setTextureRows(model.getRows());
        if (model.isTransparent())
            disableCulling();
        shader.loadFakeLighting(model.isFakeLighting());
        shader.loadSpecularLighting(model.getReflectivity(), model.getShineDamper());
        model.preRender();
    }

    private void unbindModel(RawModel model) {
        model.postRender();
        enableCulling();
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glDisableVertexAttribArray(2);
        glBindVertexArray(0);
    }

    private void bindEntity(Entity entity) {
        Matrix4f transformMatrix = MathUtils.transformMatrix(entity.getPosition(), entity.getRx(), entity.getRy(), entity.getRz(), entity.getScale());
        shader.loadTransformMatrix(transformMatrix);
        shader.setTextureOffset(entity.getTextureX(), entity.getTextureY());
    }
}
