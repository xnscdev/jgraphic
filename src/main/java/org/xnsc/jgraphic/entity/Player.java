package org.xnsc.jgraphic.entity;

import org.joml.Vector3f;
import org.xnsc.jgraphic.model.RawModel;
import org.xnsc.jgraphic.util.DisplayManager;

import static org.lwjgl.glfw.GLFW.*;

public class Player extends Entity {
    public static final IPlayerMovement MOVEMENT_WASD = (player, delta) -> {
        if (DisplayManager.keyDown(GLFW_KEY_A)) {
            player.position.x -= player.moveSpeed * delta * Math.cos(Math.toRadians(player.yaw));
            player.position.z -= player.moveSpeed * delta * Math.sin(Math.toRadians(player.yaw));
        }
        if (DisplayManager.keyDown(GLFW_KEY_D)) {
            player.position.x += player.moveSpeed * delta * Math.cos(Math.toRadians(player.yaw));
            player.position.z += player.moveSpeed * delta * Math.sin(Math.toRadians(player.yaw));
        }
        if (DisplayManager.keyDown(GLFW_KEY_W)) {
            player.position.z -= player.moveSpeed * delta * Math.cos(Math.toRadians(player.yaw));
            player.position.x += player.moveSpeed * delta * Math.sin(Math.toRadians(player.yaw));
        }
        if (DisplayManager.keyDown(GLFW_KEY_S)) {
            player.position.z += player.moveSpeed * delta * Math.cos(Math.toRadians(player.yaw));
            player.position.x -= player.moveSpeed * delta * Math.sin(Math.toRadians(player.yaw));
        }
        if (DisplayManager.keyDown(GLFW_KEY_SPACE) && player.position.y == TERRAIN_HEIGHT)
            player.addVelocity(new Vector3f(0, player.jumpSpeed, 0));
    };
    public static final IPlayerMovement MOVEMENT_WASD_TLR = (player, delta) -> {
        MOVEMENT_WASD.move(player, delta);
        if (DisplayManager.keyDown(GLFW_KEY_LEFT)) {
            player.ry += player.turnSpeed * delta;
            player.yaw -= player.turnSpeed * delta;
        }
        if (DisplayManager.keyDown(GLFW_KEY_RIGHT)) {
            player.ry -= player.turnSpeed * delta;
            player.yaw += player.turnSpeed * delta;
        }
    };
    private final IPlayerMovement movement;
    private float moveSpeed = 20;
    private float turnSpeed = 160;
    private float jumpSpeed = 15;
    private float yaw;

    public Player(RawModel model, Vector3f position, IPlayerMovement movement) {
        super(model, position);
        this.movement = movement;
    }

    public Player(RawModel model, Vector3f position) {
        this(model, position, null);
    }

    @Override
    public void tick(double delta) {
        if (movement != null)
            movement.move(this, delta);
        super.tick(delta);
    }

    @Override
    public void setRy(float ry) {
        super.setRy(ry);
        yaw = -ry;
    }

    public float getMoveSpeed() {
        return moveSpeed;
    }

    public void setMoveSpeed(float moveSpeed) {
        this.moveSpeed = moveSpeed;
    }

    public float getTurnSpeed() {
        return turnSpeed;
    }

    public void setTurnSpeed(float turnSpeed) {
        this.turnSpeed = turnSpeed;
    }

    public float getJumpSpeed() {
        return jumpSpeed;
    }

    public void setJumpSpeed(float jumpSpeed) {
        this.jumpSpeed = jumpSpeed;
    }
}
