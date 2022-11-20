package com.github.xnscdev.jgraphic.world;

/**
 * Callback interface for mouse events.
 * @author XNSC
 */
public interface IMouseEventCallback {
    /**
     * This method is called when a mouse event is generated anywhere on the screen.
     * @param world the current world
     * @param x X coordinate of the mouse event in device coordinates
     * @param y Y coordinate of the mouse event in device coordinates
     */
    void invoke(World world, float x, float y);
}
