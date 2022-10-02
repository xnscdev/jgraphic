package org.xnsc.jgraphic.world;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.xnsc.jgraphic.entity.Entity;
import org.xnsc.jgraphic.entity.EntityRenderer;
import org.xnsc.jgraphic.model.RawModel;
import org.xnsc.jgraphic.terrain.TerrainPiece;
import org.xnsc.jgraphic.terrain.TerrainRenderer;
import org.xnsc.jgraphic.util.DisplayManager;
import org.xnsc.jgraphic.util.MatrixUtils;

import java.util.*;

import static org.lwjgl.opengl.GL11.*;

public class World {
    private static final float FOV = 70;
    private static final float NEAR_PLANE = 0.1f;
    private static final float FAR_PLANE = 1000f;
    private static final Vector3f DEFAULT_SKY_COLOR = new Vector3f(0.5f, 1, 1);
    public static final Matrix4f PROJECTION = MatrixUtils.projectionMatrix(FOV, NEAR_PLANE, FAR_PLANE);
    private final EntityRenderer entityRenderer = new EntityRenderer();
    private final TerrainRenderer terrainRenderer = new TerrainRenderer();
    private final Map<RawModel, List<Entity>> entitiesMap = new HashMap<>();
    private final List<TerrainPiece> terrains = new ArrayList<>();
    private final List<Entity> entities = new ArrayList<>();
    private LightSource light = new LightSource(new Vector3f(0, 40000, 0), new Vector3f(1, 1, 1));
    private Camera camera;
    private Vector3f skyColor;
    private float fogDensity;
    private float fogGradient = 1;
    private float ambientThreshold;
    private float gravityAccel;

    public World() {
        entityRenderer.enableCulling();
        setSkyColor(DEFAULT_SKY_COLOR);
        glEnable(GL_DEPTH_TEST);
    }

    public void tick() {
        double delta = DisplayManager.deltaTime();
        camera.tick();
        ListIterator<Entity> iter = entities.listIterator();
        while (iter.hasNext()) {
            Entity entity = iter.next();
            entity.tick(delta);
            if (entity.getPosition().y < -2000)
                iter.remove();
            else
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
        WorldState state = new WorldState(camera, light, skyColor, fogDensity, fogGradient, ambientThreshold);
        entityRenderer.render(state, entitiesMap);
        terrainRenderer.render(state, terrains);
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
        entity.addAcceleration(new Vector3f(0, -gravityAccel, 0));
        entities.add(entity);
    }

    public void addTerrain(String texture) {
        terrains.add(new TerrainPiece(0, 0, texture));
        terrains.add(new TerrainPiece(0, -1, texture));
        terrains.add(new TerrainPiece(-1, 0, texture));
        terrains.add(new TerrainPiece(-1, -1, texture));
    }

    public LightSource getLightSource() {
        return light;
    }

    public Camera getCamera() {
        return camera;
    }

    public void setCamera(Camera camera) {
        this.camera = camera;
    }

    public float getAmbientThreshold() {
        return ambientThreshold;
    }

    public void setAmbientThreshold(float ambientThreshold) {
        this.ambientThreshold = ambientThreshold;
    }

    public float getGravityAccel() {
        return gravityAccel;
    }

    public void setGravityAccel(float gravityAccel) {
        this.gravityAccel = gravityAccel;
    }

    public void setSkyColor(Vector3f skyColor) {
        this.skyColor = skyColor;
        glClearColor(skyColor.x, skyColor.y, skyColor.z, 1);
    }

    public void setFog(float density, float gradient) {
        fogDensity = density;
        fogGradient = gradient;
    }
}
