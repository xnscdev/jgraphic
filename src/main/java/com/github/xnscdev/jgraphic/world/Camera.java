package com.github.xnscdev.jgraphic.world;

import com.github.xnscdev.jgraphic.util.MathUtils;
import org.joml.Matrix4f;
import org.joml.Vector3f;

/**
 * A camera in a world. All worlds must have a camera to render from.
 * @author XNSC
 */
public class Camera {
    protected Vector3f position = new Vector3f();
    protected float pitch;
    protected float yaw;
    protected float roll;

    public Vector3f getPosition() {
        return position;
    }

    /**
     * The position to report to the debug GUI. This position is normally just the position of the camera itself,
     * however third-person cameras may report the position of the tracked player instead.
     * @return the position in world space
     * @see com.github.xnscdev.jgraphic.gui.DebugGui
     */
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

    /**
     * Calculates the view matrix for this camera.
     * @return the view matrix
     */
    public Matrix4f viewMatrix() {
        return MathUtils.viewMatrix(position, pitch, yaw, roll);
    }
}
