package org.xnsc.jworld;

import org.xnsc.jworld.render.*;
import org.xnsc.jworld.render.util.DisplayManager;
import org.xnsc.jworld.render.util.ObjectManager;

public class Main {
    public static void main(String[] args) {
        DisplayManager.createDisplay();
        World world = new World();

        while (!DisplayManager.closeRequested()) {
            world.tick();
            DisplayManager.updateDisplay();
        }

        world.clean();
        ObjectManager.clean();
        DisplayManager.closeDisplay();
    }
}