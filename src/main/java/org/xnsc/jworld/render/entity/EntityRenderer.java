package org.xnsc.jworld.render.entity;

import org.joml.Matrix4f;
import org.xnsc.jworld.render.Camera;
import org.xnsc.jworld.render.LightSource;
import org.xnsc.jworld.render.Shader;
import org.xnsc.jworld.render.World;
import org.xnsc.jworld.render.model.RawModel;
import org.xnsc.jworld.render.util.MatrixUtils;

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

    public void render(Camera camera, LightSource light, Map<RawModel, List<Entity>> entities) {
        shader.start();
        shader.loadSkyFog();
        shader.loadLightSource(light);
        shader.loadViewMatrix(camera);
        for (RawModel model : entities.keySet()) {
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

    private void bindModel(RawModel model) {
        glBindVertexArray(model.getVAO());
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);
        glEnableVertexAttribArray(2);
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
        Matrix4f transformMatrix = MatrixUtils.transformMatrix(entity.getPosition(), entity.getRx(), entity.getRy(), entity.getRz(), entity.getScale());
        shader.loadTransformMatrix(transformMatrix);
    }
}
