package org.xnsc.jgraphic.world;

import org.joml.Vector2f;
import org.xnsc.jgraphic.entity.Player;
import org.xnsc.jgraphic.util.DisplayManager;

import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_RIGHT;

public class Camera3P extends Camera {
    private static final float MAX_DISTANCE = 300;
    private static final float MIN_PITCH = 10;
    private final Player player;
    private float playerDistance = 50;
    private float playerYaw;

    public Camera3P(Player player) {
        this.player = player;
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
        if (pitch > 180 - MIN_PITCH)
            pitch = 180 - MIN_PITCH;

        float horizontal = playerDistance * (float) Math.cos(Math.toRadians(pitch));
        float vertical = playerDistance * (float) Math.sin(Math.toRadians(pitch));
        float theta = player.getRy() + playerYaw;
        float offsetX = horizontal * (float) Math.sin(Math.toRadians(theta));
        float offsetZ = horizontal * (float) Math.cos(Math.toRadians(theta));
        position.x = player.getPosition().x + offsetX;
        position.y = player.getPosition().y + vertical;
        position.z = player.getPosition().z + offsetZ;
        yaw = -theta;
    }
}
