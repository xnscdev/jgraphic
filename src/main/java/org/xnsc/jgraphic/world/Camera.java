package org.xnsc.jgraphic.world;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.xnsc.jgraphic.util.MatrixUtils;

public class Camera {
    private Vector3f position = new Vector3f();
    private float pitch;
    private float yaw;
    private float roll;

    public Vector3f getPosition() {
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
        return MatrixUtils.viewMatrix(position, pitch, yaw, roll);
    }
}
