package org.xnsc.jworld.render;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.xnsc.jworld.render.util.MatrixUtils;

import static org.lwjgl.glfw.GLFW.*;

public class Camera {
    private final Vector3f position = new Vector3f();
    private float pitch;
    private float yaw;
    private float roll;

    public Camera() {
    }

    public Vector3f getPosition() {
        return position;
    }

    public float getPitch() {
        return pitch;
    }

    public float getYaw() {
        return yaw;
    }

    public float getRoll() {
        return roll;
    }

    public void tick() {
        if (DisplayManager.keyDown(GLFW_KEY_A))
            position.x -= 0.1f;
        if (DisplayManager.keyDown(GLFW_KEY_D))
            position.x += 0.1f;
        if (DisplayManager.keyDown(GLFW_KEY_W))
            position.z -= 0.1f;
        if (DisplayManager.keyDown(GLFW_KEY_S))
            position.z += 0.1f;
    }

    public Matrix4f viewMatrix() {
        return MatrixUtils.viewMatrix(position, pitch, yaw, roll);
    }
}
