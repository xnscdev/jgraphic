package org.xnsc.jgraphic.entity;

import org.joml.Vector3f;
import org.xnsc.jgraphic.model.RawModel;

public class Entity {
    private final RawModel model;
    private Vector3f position;
    private float rx;
    private float ry;
    private float rz;
    private float scale;
    private float mass;
    private Vector3f accel;
    private Vector3f velocity = new Vector3f();

    public Entity(RawModel model, Vector3f position, float rx, float ry, float rz, float scale, float mass, Vector3f accel) {
        this.model = model;
        this.position = position;
        this.rx = rx;
        this.ry = ry;
        this.rz = rz;
        this.scale = scale;
        this.mass = mass;
        this.accel = accel;
    }

    public Entity(RawModel model, Vector3f position, float mass, Vector3f accel) {
        this(model, position, 0, 0, 0, 1, mass, accel);
    }

    public void tick(double delta) {
        Vector3f dp = new Vector3f(velocity).mul((float) delta);
        position.add(dp);
        Vector3f dv = new Vector3f(accel).mul((float) delta);
        velocity.add(dv);
    }

    public RawModel getModel() {
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
