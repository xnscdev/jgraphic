package com.github.xnscdev.jgraphic.world;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import com.github.xnscdev.jgraphic.util.MathUtils;

public class Camera {
    protected Vector3f position = new Vector3f();
    protected float pitch;
    protected float yaw;
    protected float roll;

    public Vector3f getPosition() {
        return position;
    }

    public Vector3f getPrimaryPosition() {
        return position;
    }

    public void setPosition(Vector3f position) {
        this.position = position;
    }

    public float getPitch() {
        return pitch;
    }

    public void setPitch(float pitch) {
        this.pitch = pitch;
    }

    public float getYaw() {
        return yaw;
    }

    public void setYaw(float yaw) {
        this.yaw = yaw;
    }

    public float getRoll() {
        return roll;
    }

    public void setRoll(float roll) {
        this.roll = roll;
    }

    public void tick() {
    }

    public Matrix4f viewMatrix() {
        return MathUtils.viewMatrix(position, pitch, yaw, roll);
    }
}
