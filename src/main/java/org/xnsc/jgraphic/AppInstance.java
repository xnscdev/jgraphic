package org.xnsc.jgraphic;

import org.xnsc.jgraphic.gui.GuiManager;
import org.xnsc.jgraphic.text.Fonts;
import org.xnsc.jgraphic.util.DisplayManager;
import org.xnsc.jgraphic.util.ObjectManager;
import org.xnsc.jgraphic.world.World;

public class AppInstance {
    public static void init(String title, int width, int height) {
        DisplayManager.createDisplay(title, width, height);
        Fonts.init();
        GuiManager.init();
    }

    public static void launch(World world) {
        if (world.getCamera() == null)
            throw new IllegalStateException("Attempted to render a world without a camera");
        while (!DisplayManager.closeRequested()) {
            world.tick();
            DisplayManager.updateDisplay();
        }
        world.clean();
        GuiManager.clean();
        ObjectManager.clean();
        DisplayManager.closeDisplay();
    }
}
