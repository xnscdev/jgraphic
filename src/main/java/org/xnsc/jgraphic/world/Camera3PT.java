package org.xnsc.jgraphic.world;

import org.xnsc.jgraphic.entity.Player;

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
