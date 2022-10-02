package org.xnsc.jgraphic.entity;

import org.joml.Vector3f;
import org.xnsc.jgraphic.model.RawModel;
import org.xnsc.jgraphic.util.DisplayManager;

import static org.lwjgl.glfw.GLFW.*;

public class Player extends Entity {
    private static final float MOVE_SPEED = 20;
    private static final float TURN_SPEED = 160;
    private float yaw;

    public Player(RawModel model, Vector3f position) {
        super(model, position);
    }

    @Override
    public void tick(double delta) {
        if (DisplayManager.keyDown(GLFW_KEY_A)) {
            position.x -= MOVE_SPEED * delta * Math.cos(Math.toRadians(yaw));
            position.z -= MOVE_SPEED * delta * Math.sin(Math.toRadians(yaw));
        }
        if (DisplayManager.keyDown(GLFW_KEY_D)) {
            position.x += MOVE_SPEED * delta * Math.cos(Math.toRadians(yaw));
            position.z += MOVE_SPEED * delta * Math.sin(Math.toRadians(yaw));
        }
        if (DisplayManager.keyDown(GLFW_KEY_W)) {
            position.z -= MOVE_SPEED * delta * Math.cos(Math.toRadians(yaw));
            position.x += MOVE_SPEED * delta * Math.sin(Math.toRadians(yaw));
        }
        if (DisplayManager.keyDown(GLFW_KEY_S)) {
            position.z += MOVE_SPEED * delta * Math.cos(Math.toRadians(yaw));
            position.x -= MOVE_SPEED * delta * Math.sin(Math.toRadians(yaw));
        }
        if (DisplayManager.keyDown(GLFW_KEY_LEFT)) {
            ry += TURN_SPEED * delta;
            yaw -= TURN_SPEED * delta;
        }
        if (DisplayManager.keyDown(GLFW_KEY_RIGHT)) {
            ry -= TURN_SPEED * delta;
            yaw += TURN_SPEED * delta;
        }
        super.tick(delta);
    }
}
