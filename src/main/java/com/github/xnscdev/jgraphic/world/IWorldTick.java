package com.github.xnscdev.jgraphic.world;

/**
 * Callback interface for world tick events.
 * @author XNSC
 */
public interface IWorldTick {
    /**
     * This method is called every world tick. A world tick occurs every time the world is redrawn and is equivalent
     * to the frame rate of the application.
     * @param world the current world
     * @param delta number of seconds since the last world tick
     */
    void tick(World world, double delta);
}
