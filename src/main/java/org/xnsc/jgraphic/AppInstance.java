package org.xnsc.jgraphic;

import org.xnsc.jgraphic.util.DisplayManager;
import org.xnsc.jgraphic.util.ObjectManager;
import org.xnsc.jgraphic.render.World;

public class AppInstance {
    private static World world;

    public static void init(String title, int width, int height) {
        DisplayManager.createDisplay(title, width, height);
    }

    public static void launch(World world) {
        AppInstance.world = world;
        if (world.getCamera() == null)
            throw new IllegalStateException("Attempted to render a world without a camera");
        while (!DisplayManager.closeRequested()) {
            world.tick();
            DisplayManager.updateDisplay();
        }
        world.clean();
        ObjectManager.clean();
        DisplayManager.closeDisplay();
    }
}
