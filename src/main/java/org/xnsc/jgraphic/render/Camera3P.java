package org.xnsc.jgraphic.render;

import org.joml.Vector2f;
import org.xnsc.jgraphic.entity.Player;
import org.xnsc.jgraphic.util.DisplayManager;

import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_RIGHT;

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
