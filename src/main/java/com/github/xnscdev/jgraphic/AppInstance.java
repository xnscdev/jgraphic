package com.github.xnscdev.jgraphic;

import com.github.xnscdev.jgraphic.text.Fonts;
import com.github.xnscdev.jgraphic.util.DisplayManager;
import com.github.xnscdev.jgraphic.util.ObjectManager;
import com.github.xnscdev.jgraphic.gui.GuiManager;
import com.github.xnscdev.jgraphic.world.World;

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
