package org.xnsc.jgraphic.world;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.xnsc.jgraphic.util.DisplayManager;
import org.xnsc.jgraphic.util.MatrixUtils;

import static org.lwjgl.glfw.GLFW.*;

public class Camera {
    private static final float MOVE_SPEED = 0.4f;
    private static final int TURN_SPEED = 1;
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
        if (DisplayManager.keyDown(GLFW_KEY_A)) {
            position.x -= MOVE_SPEED * Math.cos(Math.toRadians(yaw));
            position.z -= MOVE_SPEED * Math.sin(Math.toRadians(yaw));
        }
        if (DisplayManager.keyDown(GLFW_KEY_D)) {
            position.x += MOVE_SPEED * Math.cos(Math.toRadians(yaw));
            position.z += MOVE_SPEED * Math.sin(Math.toRadians(yaw));
        }
        if (DisplayManager.keyDown(GLFW_KEY_W)) {
            position.z -= MOVE_SPEED * Math.cos(Math.toRadians(yaw));
            position.x += MOVE_SPEED * Math.sin(Math.toRadians(yaw));
        }
        if (DisplayManager.keyDown(GLFW_KEY_S)) {
            position.z += MOVE_SPEED * Math.cos(Math.toRadians(yaw));
            position.x -= MOVE_SPEED * Math.sin(Math.toRadians(yaw));
        }
        if (DisplayManager.keyDown(GLFW_KEY_LEFT))
            yaw -= TURN_SPEED;
        if (DisplayManager.keyDown(GLFW_KEY_RIGHT))
            yaw += TURN_SPEED;
    }

    public Matrix4f viewMatrix() {
        return MatrixUtils.viewMatrix(position, pitch, yaw, roll);
    }
}
