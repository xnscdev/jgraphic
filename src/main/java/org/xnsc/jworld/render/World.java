package org.xnsc.jworld.render;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.xnsc.jworld.render.entity.Entity;
import org.xnsc.jworld.render.entity.EntityRenderer;
import org.xnsc.jworld.render.model.RawModel;
import org.xnsc.jworld.render.terrain.TerrainPiece;
import org.xnsc.jworld.render.terrain.TerrainRenderer;
import org.xnsc.jworld.render.util.MatrixUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.lwjgl.opengl.GL11.*;

public class World {
    private static final float FOV = 70;
    private static final float NEAR_PLANE = 0.1f;
    private static final float FAR_PLANE = 1000f;
    public static final Vector3f SKY_COLOR = new Vector3f(0.5f, 1, 1);
    public static final float FOG_DENSITY = 0.003f;
    public static final float FOG_GRADIENT = 1.5f;
    public static final Matrix4f PROJECTION = MatrixUtils.projectionMatrix(FOV, NEAR_PLANE, FAR_PLANE);
    private final EntityRenderer entityRenderer = new EntityRenderer();
    private final TerrainRenderer terrainRenderer = new TerrainRenderer();
    private final Map<RawModel, List<Entity>> entitiesMap = new HashMap<>();
    private final List<TerrainPiece> terrains = new ArrayList<>();
    private final List<Entity> entities = new ArrayList<>();
    private final LightSource light = new LightSource(new Vector3f(2000, 3000, 2000), new Vector3f(1, 1, 1));
    private final Camera camera = new Camera();

    public World() {
        entityRenderer.enableCulling();
        glClearColor(SKY_COLOR.x, SKY_COLOR.y, SKY_COLOR.z, 1);
        glEnable(GL_DEPTH_TEST);
    }

    public void tick() {
        camera.tick();
        for (Entity entity : entities) {
            loadEntity(entity);
        }
        render(camera, light);
    }

    public void clean() {
        entityRenderer.clean();
        terrainRenderer.clean();
    }

    public void render(Camera camera, LightSource light) {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        entityRenderer.render(camera, light, entitiesMap);
        terrainRenderer.render(camera, light, terrains);
        entitiesMap.clear();
    }

    public void loadEntity(Entity entity) {
        RawModel model = entity.getModel();
        List<Entity> batch = entitiesMap.get(model);
        if (batch != null)
            batch.add(entity);
        else {
            batch = new ArrayList<>();
            batch.add(entity);
            entitiesMap.put(model, batch);
        }
    }

    public void addEntity(Entity entity) {
        entities.add(entity);
    }

    public void addTerrainPiece(TerrainPiece terrain) {
        terrains.add(terrain);
    }

    public LightSource getLightSource() {
        return light;
    }

    public Camera getCamera() {
        return camera;
    }
}
