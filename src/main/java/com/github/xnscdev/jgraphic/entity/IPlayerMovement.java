package com.github.xnscdev.jgraphic.entity;

import com.github.xnscdev.jgraphic.terrain.TerrainPiece;

/**
 * This interface handles player movement.
 * @author XNSC
 */
public interface IPlayerMovement {
    /**
     * This method is called every world tick and is used to update the player's position. The method should
     * be overridden by checking the appropriate keyboard or mouse inputs and moving the player accordingly.
     * @param player the player
     * @param terrain the terrain piece the player is currently above
     * @param delta number of seconds since the last world tick
     */
    void move(Player player, TerrainPiece terrain, double delta);
}
