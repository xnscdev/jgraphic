package org.xnsc.jgraphic;

import org.xnsc.jgraphic.util.DisplayManager;
import org.xnsc.jgraphic.util.ObjectManager;
import org.xnsc.jgraphic.world.World;

public class AppInstance {
    private World world;

    public AppInstance(String title, int width, int height) {
        DisplayManager.createDisplay(title, width, height);
    }

    public void setWorld(World world) {
        this.world = world;
    }

    public void mainLoop() {
        if (world.getCamera() == null)
            throw new IllegalStateException("Attempted to render a world without a camera");
        while (!DisplayManager.closeRequested()) {
            world.tick();
            DisplayManager.updateDisplay();
        }
    }

    public void clean() {
        world.clean();
        ObjectManager.clean();
        DisplayManager.closeDisplay();
    }
}
