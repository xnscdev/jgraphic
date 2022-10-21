package com.github.xnscdev.jgraphic.entity;

import com.github.xnscdev.jgraphic.terrain.TerrainPiece;

public interface IPlayerMovement {
    void move(Player player, TerrainPiece terrain, double delta);
}
