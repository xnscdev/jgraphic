package com.github.xnscdev.jgraphic;

import com.github.xnscdev.jgraphic.gui.GuiManager;
import com.github.xnscdev.jgraphic.util.AssetLoader;
import com.github.xnscdev.jgraphic.util.DisplayManager;
import com.github.xnscdev.jgraphic.util.ObjectManager;
import com.github.xnscdev.jgraphic.world.World;

/**
 * Manages the application instance.
 * @author XNSC
 */
public class AppInstance {
    public static World WORLD;

    /**
     * Initializes a JGraphic application by creating a window and initializing the GUI manager.
     * @param title window title
     * @param width width in pixels
     * @param height height in pixels
     */
    public static void init(String title, int width, int height) {
        DisplayManager.createDisplay(title, width, height);
        GuiManager.init();
    }

    /**
     * Begins rendering a world on the display and deallocates all resources when the user requests the
     * window to be closed.
     * @param world the world to render
     * @throws IllegalStateException the world does not have a camera
     */
    public static void launch(World world) {
        if (world.getCamera() == null)
            throw new IllegalStateException("Attempted to render a world without a camera");
        WORLD = world;
        while (!DisplayManager.closeRequested()) {
            world.tick();
            DisplayManager.updateDisplay();
        }
        world.clean();
        GuiManager.clean();
        ObjectManager.clean();
        AssetLoader.clean();
        DisplayManager.closeDisplay();
    }
}
