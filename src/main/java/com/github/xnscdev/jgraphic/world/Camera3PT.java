package com.github.xnscdev.jgraphic.world;

import com.github.xnscdev.jgraphic.entity.Player;

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
