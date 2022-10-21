package com.github.xnscdev.jgraphic.entity;

import com.github.xnscdev.jgraphic.terrain.TerrainPiece;
import com.github.xnscdev.jgraphic.world.World;
import org.joml.Vector3f;
import com.github.xnscdev.jgraphic.model.TexturedModel;

public class Entity {
    private final TexturedModel model;
    protected Vector3f position;
    protected float rx;
    protected float ry;
    protected float rz;
    protected float scale;
    private int textureIndex;
    private Vector3f acceleration = new Vector3f();
    private Vector3f velocity = new Vector3f();

    public Entity(TexturedModel model, Vector3f position, float rx, float ry, float rz, float scale, int textureIndex) {
        this.model = model;
        this.position = position;
        this.rx = rx;
        this.ry = ry;
        this.rz = rz;
        this.scale = scale;
        this.textureIndex = textureIndex;
    }

    public Entity(TexturedModel model, Vector3f position, float rx, float ry, float rz, float scale) {
        this(model, position, rx, ry, rz, scale, 0);
    }

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

    public TexturedModel getModel() {
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

    public float getTextureX() {
        int column = textureIndex % model.getRows();
        return (float) column / (float) model.getRows();
    }

    public float getTextureY() {
        int row = textureIndex / model.getRows();
        return (float) row / (float) model.getRows();
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
