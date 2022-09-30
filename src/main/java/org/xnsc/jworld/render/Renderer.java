package org.xnsc.jworld.render;

import org.joml.Matrix4f;
import org.xnsc.jworld.render.shader.MatrixUtils;
import org.xnsc.jworld.render.shader.Shader;
import org.xnsc.jworld.render.shader.StaticShader;

import static org.lwjgl.opengl.GL30.*;

public class Renderer {
    private static final float FOV = 70;
    private static final float NEAR_PLANE = 0.1f;
    private static final float FAR_PLANE = 1000f;
    private final StaticShader shader = new StaticShader();
    private final Camera camera = new Camera();

    public Renderer() {
        glClearColor(1, 1, 1, 1);
        shader.start();
        shader.loadProjectionMatrix(MatrixUtils.projectionMatrix(FOV, NEAR_PLANE, FAR_PLANE));
        Shader.stop();
    }

    public void refresh() {
        glClear(GL_COLOR_BUFFER_BIT);
    }

    public void render(Entity entity) {
        shader.start();
        shader.loadViewMatrix(camera);

        TexturedModel model = entity.getModel();
        glBindVertexArray(model.getVAO());
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);

        Matrix4f transformMatrix = MatrixUtils.transformMatrix(entity.getPosition(), entity.getRx(), entity.getRy(), entity.getRz(), entity.getScale());
        shader.loadTransformMatrix(transformMatrix);
        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, model.getTexture());
        glDrawElements(GL_TRIANGLES, model.getVertexCount(), GL_UNSIGNED_INT, 0);

        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glBindVertexArray(0);

        Shader.stop();
    }

    public void tick() {
        camera.tick();
    }

    public void clean() {
        shader.clean();
    }
}
