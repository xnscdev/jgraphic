package org.xnsc.jworld;

import org.xnsc.jgraphic.AppInstance;
import org.xnsc.jgraphic.world.World;

public class Main {
    public static void main(String[] args) {
        AppInstance app = new AppInstance("JWorld", 1280, 760);
        World world = new JWorld();
        app.setWorld(world);
        app.mainLoop();
        app.clean();
    }
}