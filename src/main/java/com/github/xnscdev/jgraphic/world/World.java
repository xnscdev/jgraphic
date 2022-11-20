package com.github.xnscdev.jgraphic.world;

import com.github.xnscdev.jgraphic.entity.Entity;
import com.github.xnscdev.jgraphic.entity.EntityModel;
import com.github.xnscdev.jgraphic.entity.EntityRenderer;
import com.github.xnscdev.jgraphic.gui.GuiManager;
import com.github.xnscdev.jgraphic.terrain.TerrainPiece;
import com.github.xnscdev.jgraphic.terrain.TerrainRenderer;
import com.github.xnscdev.jgraphic.util.DisplayManager;
import com.github.xnscdev.jgraphic.util.MathUtils;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import java.util.*;

import static org.lwjgl.opengl.GL11.*;

/**
 * The world where all three-dimensional objects are rendered. Worlds also support ambient effects including
 * light sources, gravity, and fog.
 * @author XNSC
 */
public class World {
    private static final float FOV = 70;
    private static final float NEAR_PLANE = 0.1f;
    private static final float FAR_PLANE = 1000f;
    private static final Vector3f DEFAULT_SKY_COLOR = new Vector3f(0.5f, 1, 1);

    /**
     * Default projection matrix for a world.
     */
    public static final Matrix4f PROJECTION = MathUtils.projectionMatrix(FOV, NEAR_PLANE, FAR_PLANE);

    /**
     * Entities with a vertical position below this limit will be removed from the world.
     */
    public static final float VOID = -2000;
    private final EntityRenderer entityRenderer = new EntityRenderer();
    private final TerrainRenderer terrainRenderer = new TerrainRenderer();
    private final Map<EntityModel, List<Entity>> entitiesMap = new HashMap<>();
    private final List<Entity> entities = new ArrayList<>();
    private final List<TerrainPiece> terrains = new ArrayList<>();
    private final List<LightSource> lights = new ArrayList<>();
    private IMouseEventCallback mousePressedCallback;
    private IMouseEventCallback mouseReleasedCallback;
    private Camera camera;
    private MousePicker picker;
    private IWorldTick tickCallback;
    private Vector3f skyColor;
    private float fogDensity;
    private float fogGradient = 1;
    private float ambientThreshold;
    private float gravityAccel;
    private boolean active = true;

    public World() {
        entityRenderer.enableCulling();
        setSkyColor(DEFAULT_SKY_COLOR);
        glEnable(GL_DEPTH_TEST);
    }

    public void tick() {
        double delta = DisplayManager.deltaTime();
        ListIterator<Entity> iter = entities.listIterator();
        while (iter.hasNext()) {
            Entity entity = iter.next();
            if (active)
                entity.tick(getTerrainForEntity(entity), delta);
            if (entity.getPosition().y <= VOID)
                iter.remove();
            else
                loadEntity(entity);
        }
        camera.tick();
        picker.tick(this);
        GuiManager.tick(delta);
        render(camera, lights);
        if (active) {
            if (tickCallback != null)
                tickCallback.tick(this, delta);
        }
    }

    /**
     * Frees resources allocated to this world.
     */
    public void clean() {
        entityRenderer.clean();
        terrainRenderer.clean();
    }

    public void render(Camera camera, List<LightSource> lights) {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        WorldState state = new WorldState(camera, lights, skyColor, fogDensity, fogGradient, ambientThreshold);
        entityRenderer.render(state, entitiesMap);
        terrainRenderer.render(state, terrains);
        GuiManager.render();
        entitiesMap.clear();
    }

    public void loadEntity(Entity entity) {
        EntityModel model = entity.getModel();
        List<Entity> batch = entitiesMap.get(model);
        if (batch != null)
            batch.add(entity);
        else {
            batch = new ArrayList<>();
            batch.add(entity);
            entitiesMap.put(model, batch);
        }
    }

    /**
     * Adds an entity to be rendered in this world.
     * @param entity the entity
     */
    public void addEntity(Entity entity) {
        entity.addAcceleration(new Vector3f(0, -gravityAccel, 0));
        entities.add(entity);
    }

    /**
     * Removes an entity to be rendered in this world. If this world does not already have the entity,
     * no action is performed.
     * @param entity the entity
     */
    public void removeEntity(Entity entity) {
        entities.remove(entity);
    }

    /**
     * Adds a terrain piece to the world.
     * @param terrain the terrain
     */
    public void addTerrain(TerrainPiece terrain) {
        terrain.build();
        terrains.add(terrain);
    }

    /**
     * Removes a terrain piece from the world. If this world does not already have the terrain piece,
     * no action is performed.
     * @param terrain the terrain
     */
    public void removeTerrain(TerrainPiece terrain) {
        terrains.remove(terrain);
    }

    public List<LightSource> getLightSources() {
        return lights;
    }

    /**
     * Adds a light source to the world. Currently, a limited number of light sources are supported.
     * @param light the light source
     */
    public void addLightSource(LightSource light) {
        lights.add(light);
    }

    /**
     * Removes a light source from the world. If the light source is not present in the world,
     * no action is performed.
     * @param light the light source
     */
    public void removeLightSource(LightSource light) {
        lights.remove(light);
    }

    public Camera getCamera() {
        return camera;
    }

    /**
     * Sets the camera of the world and initializes the world mouse picker. A world requires a camera to render.
     * @param camera the camera
     */
    public void setCamera(Camera camera) {
        this.camera = camera;
        picker = new MousePicker(camera);
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

    public Vector3f getSkyColor() {
        return skyColor;
    }

    public void setSkyColor(Vector3f skyColor) {
        this.skyColor = skyColor;
        glClearColor(skyColor.x, skyColor.y, skyColor.z, 1);
    }

    public void setFog(float density, float gradient) {
        fogDensity = density;
        fogGradient = gradient;
    }

    public MousePicker getMousePicker() {
        return picker;
    }

    public void setTickCallback(IWorldTick tickCallback) {
        this.tickCallback = tickCallback;
    }

    public void mousePressed(float x, float y) {
        if (!GuiManager.mousePressed(x, y) && mousePressedCallback != null)
            mousePressedCallback.invoke(this, x, y);
    }

    public void setMousePressedCallback(IMouseEventCallback callback) {
        this.mousePressedCallback = callback;
    }

    public void mouseReleased(float x, float y) {
        if (!GuiManager.mouseReleased(x, y) && mouseReleasedCallback != null)
            mouseReleasedCallback.invoke(this, x, y);
    }

    public void setMouseReleasedCallback(IMouseEventCallback callback) {
        this.mouseReleasedCallback = callback;
    }

    public boolean isActive() {
        return active;
    }

    /**
     * Changes the active state of the world. Inactive worlds will not be refreshed and no world ticks will be emitted
     * for the world.
     * @param active the new active state
     */
    public void setActive(boolean active) {
        this.active = active;
    }

    /**
     * Gets the terrain piece containing a point.
     * @param x X coordinate of the point in world space
     * @param z Z coordinate of the point in world space
     * @return the terrain, or {@code null} if no terrain piece contains the point
     */
    public TerrainPiece getTerrain(float x, float z) {
        for (TerrainPiece terrain : terrains) {
            if (x >= terrain.getX() && x < terrain.getX() + terrain.getSize() && z >= terrain.getZ() && z < terrain.getZ() + terrain.getSize())
                return terrain;
        }
        return null;
    }

    /**
     * Calculates the height of the terrain at the specified coordinates. If multiple terrains overlap this point,
     * the result will be calculated based on the first terrain piece added.
     * @param x X coordinate in world space
     * @param z Z coordinate in world space
     * @return the terrain height, or 0 if no terrain contains this point
     */
    public float getTerrainHeight(float x, float z) {
        TerrainPiece terrain = getTerrain(x, z);
        if (terrain == null)
            return 0;
        return terrain.getTerrainHeight(x, z);
    }

    private TerrainPiece getTerrainForEntity(Entity entity) {
        return getTerrain(entity.getPosition().x, entity.getPosition().z);
    }
}
