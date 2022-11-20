package com.github.xnscdev.jgraphic.world;

import com.github.xnscdev.jgraphic.entity.Player;
import com.github.xnscdev.jgraphic.util.DisplayManager;
import org.joml.Vector2f;
import org.joml.Vector3f;

import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_RIGHT;

/**
 * A third-person camera that always faces a player. This camera has a variable yaw with respect to the player,
 * independent of the player's actual rotation. Changing the camera's yaw will cause the equivalent change
 * to be reflected in the player's yaw but the two values may not be equal.
 * @author XNSC
 */
public class Camera3P extends Camera {
    private static final float MAX_DISTANCE = 300;
    private static final float MIN_PITCH = 10;
    protected final Player player;
    protected float playerYaw;
    private float playerDistance = 50;

    public Camera3P(Player player) {
        this.player = player;
    }

    @Override
    public Vector3f getPrimaryPosition() {
        return player.getPosition();
    }

    @Override
    public void tick() {
        float zoom = DisplayManager.getScrollOffset();
        playerDistance -= zoom;
        if (playerDistance > MAX_DISTANCE)
            playerDistance = MAX_DISTANCE;
        if (playerDistance < 0)
            playerDistance = 0;
        if (DisplayManager.mouseDown(GLFW_MOUSE_BUTTON_RIGHT)) {
            Vector2f mouseOffset = DisplayManager.getMouseOffset();
            pitch -= mouseOffset.y * 0.3f;
            playerYaw -= mouseOffset.x * 0.3f;
        }
        if (pitch < MIN_PITCH)
            pitch = MIN_PITCH;
        if (pitch > 90)
            pitch = 90;

        float horizontal = playerDistance * (float) Math.cos(Math.toRadians(pitch));
        float vertical = playerDistance * (float) Math.sin(Math.toRadians(pitch));
        float theta = getCameraAngle();
        float offsetX = horizontal * (float) Math.sin(Math.toRadians(theta));
        float offsetZ = horizontal * (float) Math.cos(Math.toRadians(theta));
        position.x = player.getPosition().x + offsetX;
        position.y = player.getPosition().y + vertical;
        position.z = player.getPosition().z + offsetZ;
        yaw = -theta;
    }

    public float getCameraAngle() {
        return player.getRy() + playerYaw;
    }
}
