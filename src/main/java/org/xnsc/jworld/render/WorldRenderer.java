package org.xnsc.jworld.render;

import org.joml.Matrix4f;
import org.xnsc.jworld.render.util.MatrixUtils;
import org.xnsc.jworld.render.shader.Shader;
import org.xnsc.jworld.render.shader.StaticShader;

import static org.lwjgl.opengl.GL30.*;

public class WorldRenderer {
    private static final float FOV = 70;
    private static final float NEAR_PLANE = 0.1f;
    private static final float FAR_PLANE = 1000f;
    private final StaticShader shader = new StaticShader();

    public WorldRenderer() {
        glClearColor(0, 0, 0, 1);
        glEnable(GL_DEPTH_TEST);
        shader.start();
        shader.loadProjectionMatrix(MatrixUtils.projectionMatrix(FOV, NEAR_PLANE, FAR_PLANE));
        Shader.stop();
    }

    public void refresh() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    }

    public void render(Entity entity, Camera camera, LightSource light) {
        shader.start();
        shader.loadLightSource(light);
        shader.loadViewMatrix(camera);

        RawModel model = entity.getModel();
        glBindVertexArray(model.getVAO());
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);
        glEnableVertexAttribArray(2);

        Matrix4f transformMatrix = MatrixUtils.transformMatrix(entity.getPosition(), entity.getRx(), entity.getRy(), entity.getRz(), entity.getScale());
        shader.loadTransformMatrix(transformMatrix);
        shader.loadSpecularLighting(model.getReflectivity(), model.getShineDamper());
        model.preRender();
        glDrawElements(GL_TRIANGLES, model.getVertexCount(), GL_UNSIGNED_INT, 0);
        model.postRender();

        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glDisableVertexAttribArray(2);
        glBindVertexArray(0);

        Shader.stop();
    }

    public void clean() {
        shader.clean();
    }
}
