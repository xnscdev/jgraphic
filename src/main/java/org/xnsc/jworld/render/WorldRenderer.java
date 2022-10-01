package org.xnsc.jworld.render;

import org.xnsc.jworld.render.util.MatrixUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.lwjgl.opengl.GL11.*;

public class WorldRenderer {
    private static final float FOV = 70;
    private static final float NEAR_PLANE = 0.1f;
    private static final float FAR_PLANE = 1000f;
    private final EntityShader entityShader = new EntityShader();
    private final EntityRenderer entityRenderer = new EntityRenderer(entityShader);
    private final Map<RawModel, List<Entity>> entities = new HashMap<>();

    public WorldRenderer() {
        glClearColor(0, 0, 0, 1);
        glEnable(GL_DEPTH_TEST);
        glCullFace(GL_BACK);
        glEnable(GL_CULL_FACE);
        entityShader.start();
        entityShader.loadProjectionMatrix(MatrixUtils.projectionMatrix(FOV, NEAR_PLANE, FAR_PLANE));
        Shader.stop();
    }

    public void clean() {
        entityShader.clean();
    }

    public void render(Camera camera, LightSource light) {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        entityShader.start();
        entityShader.loadLightSource(light);
        entityShader.loadViewMatrix(camera);
        entityRenderer.render(entities);
        Shader.stop();
        entities.clear();
    }

    public void processEntity(Entity entity) {
        RawModel model = entity.getModel();
        List<Entity> batch = entities.get(model);
        if (batch != null)
            batch.add(entity);
        else {
            batch = new ArrayList<>();
            batch.add(entity);
            entities.put(model, batch);
        }
    }
}
