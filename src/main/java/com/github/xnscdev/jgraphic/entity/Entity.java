package com.github.xnscdev.jgraphic.entity;

import com.github.xnscdev.jgraphic.terrain.TerrainPiece;
import com.github.xnscdev.jgraphic.world.World;
import org.joml.Vector3f;

/**
 * An entity is a three-dimensional object that can be rendered in a world. Entities have an individual
 * position, rotation, and scale. Multiple entities may share the same underlying model.
 * @author XNSC
 */
public class Entity {
    private final EntityModel model;
    protected Vector3f position;
    protected float rx;
    protected float ry;
    protected float rz;
    protected float scale;
    private Vector3f acceleration = new Vector3f();
    private Vector3f velocity = new Vector3f();

    /**
     * Creates a new entity. The entity will not be rendered until added to a world.
     * @param model entity model to render
     * @param position position in the world
     * @param rx roll
     * @param ry yaw
     * @param rz pitch
     * @param scale scale factor relative to model size
     */
    public Entity(EntityModel model, Vector3f position, float rx, float ry, float rz, float scale) {
        this.model = model;
        this.position = position;
        this.rx = rx;
        this.ry = ry;
        this.rz = rz;
        this.scale = scale;
    }

    /**
     * Creates a new entity with default rotation and scale. The entity will not be rendered until added to a world.
     * @param model entity model to render
     * @param position position in the world
     */
    public Entity(EntityModel model, Vector3f position) {
        this(model, position, 0, 0, 0, 1);
    }

    /**
     * This method is run every frame for an entity in a world. This method may be overridden but should not
     * be called directly. Overridden implementations of this method should call {@code super.tick(terrain, delta);}
     * for proper physics handling.
     * @param terrain the terrain piece currently underneath the entity, if one exists
     * @param delta number of seconds since the last world tick
     */
    public void tick(TerrainPiece terrain, double delta) {
        Vector3f dp = new Vector3f(velocity).mul((float) delta);
        position.add(dp);
        Vector3f dv = new Vector3f(acceleration).mul((float) delta);
        velocity.add(dv);
        float terrainHeight = terrain == null ? World.VOID : terrain.getTerrainHeight(position.x, position.z);
        if (position.y < terrainHeight) {
            position.y = terrainHeight;
            velocity.y = 0;
        }
    }

    public EntityModel getModel() {
        return model;
    }

    public Vector3f getPosition() {
        return position;
    }

    public void setPosition(Vector3f position) {
        this.position = position;
    }

    public float getRx() {
        return rx;
    }

    public void setRx(float rx) {
        this.rx = rx;
    }

    public float getRy() {
        return ry;
    }

    public void setRy(float ry) {
        this.ry = ry;
    }

    public float getRz() {
        return rz;
    }

    public void setRz(float rz) {
        this.rz = rz;
    }

    public float getScale() {
        return scale;
    }

    public void setScale(float scale) {
        this.scale = scale;
    }

    public void addAcceleration(Vector3f acceleration) {
        this.acceleration.add(acceleration);
    }

    public void addVelocity(Vector3f velocity) {
        this.velocity.add(velocity);
    }

    public void move(float dx, float dy, float dz) {
        position.x += dx;
        position.y += dy;
        position.z += dz;
    }

    public void rotate(float dx, float dy, float dz) {
        rx += dx;
        ry += dy;
        rz += dz;
    }
}
