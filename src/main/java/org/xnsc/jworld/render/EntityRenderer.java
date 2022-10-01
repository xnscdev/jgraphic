package org.xnsc.jworld.render;

import org.joml.Matrix4f;
import org.xnsc.jworld.render.util.MatrixUtils;

import java.util.List;
import java.util.Map;

import static org.lwjgl.opengl.GL30.*;

public class EntityRenderer {
    private final EntityShader shader;

    public EntityRenderer(EntityShader shader) {
        this.shader = shader;
    }

    public void render(Map<RawModel, List<Entity>> entities) {
        for (RawModel model : entities.keySet()) {
            bindModel(model);
            List<Entity> batch = entities.get(model);
            for (Entity entity : batch) {
                bindEntity(entity);
                glDrawElements(GL_TRIANGLES, model.getVertexCount(), GL_UNSIGNED_INT, 0);
            }
            unbindModel(model);
        }
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

    private void bindEntity(Entity entity) {
        Matrix4f transformMatrix = MatrixUtils.transformMatrix(entity.getPosition(), entity.getRx(), entity.getRy(), entity.getRz(), entity.getScale());
        shader.loadTransformMatrix(transformMatrix);
    }
}
