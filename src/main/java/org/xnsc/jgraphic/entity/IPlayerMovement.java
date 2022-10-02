package org.xnsc.jgraphic.entity;

import org.xnsc.jgraphic.terrain.TerrainPiece;

public interface IPlayerMovement {
    void move(Player player, TerrainPiece terrain, double delta);
}
