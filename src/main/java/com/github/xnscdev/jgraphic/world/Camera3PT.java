package com.github.xnscdev.jgraphic.world;

import com.github.xnscdev.jgraphic.entity.Player;

/**
 * A third-person camera that always faces the player. This camera always faces the player from behind. The player's
 * yaw will always be set to the camera's yaw.
 * @author XNSC
 */
public class Camera3PT extends Camera3P {
    public Camera3PT(Player player) {
        super(player);
    }

    @Override
    public void tick() {
        super.tick();
        player.setRy(playerYaw);
    }

    @Override
    public float getCameraAngle() {
        return playerYaw;
    }
}
