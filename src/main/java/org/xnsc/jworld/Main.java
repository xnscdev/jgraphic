package org.xnsc.jworld;

import org.joml.Vector3f;
import org.xnsc.jgraphic.AppInstance;
import org.xnsc.jgraphic.entity.Player;
import org.xnsc.jgraphic.model.TexturedModel;
import org.xnsc.jgraphic.world.Camera;
import org.xnsc.jgraphic.world.World;

public class Main {
    public static void main(String[] args) {
        AppInstance app = new AppInstance("JWorld", 1280, 760);
        World world = new World();
        world.setCamera(new Camera());
        world.getCamera().setPosition(new Vector3f(0, 5, 40));
        world.setAmbientThreshold(0.2f);
        TexturedModel playerModel = new TexturedModel("human");
        Player player = new Player(playerModel, new Vector3f(0, 0, 0));
        world.addEntity(player);
        world.addTerrain("terrain");
        app.setWorld(world);
        app.mainLoop();
        app.clean();
    }
}