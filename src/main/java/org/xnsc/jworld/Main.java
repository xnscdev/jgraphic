package org.xnsc.jworld;

import org.joml.Random;
import org.joml.Vector3f;
import org.xnsc.jgraphic.AppInstance;
import org.xnsc.jgraphic.entity.Entity;
import org.xnsc.jgraphic.entity.Player;
import org.xnsc.jgraphic.model.TexturedModel;
import org.xnsc.jgraphic.world.Camera;
import org.xnsc.jgraphic.world.Camera3P;
import org.xnsc.jgraphic.world.World;

public class Main {
    public static void main(String[] args) {
        AppInstance.init("JWorld", 1280, 760);
        World world = new World();
        TexturedModel playerModel = new TexturedModel("human");
        Player player = new Player(playerModel, new Vector3f(0, 0, 0));
        world.setCamera(new Camera3P(player));
        world.getCamera().setPosition(new Vector3f(0, 20, 50));
        world.getCamera().setPitch(20);
        world.setAmbientThreshold(0.2f);
        world.setGravityAccel(20);
        world.addEntity(player);
        Random random = new Random();
        TexturedModel treeModel = new TexturedModel("tree");
        for (int i = 0; i < 500; i++) {
            float x = random.nextFloat() * 1000 - 500;
            float z = random.nextFloat() * 1000 - 500;
            float scale = random.nextFloat() + 3;
            Entity tree = new Entity(treeModel, new Vector3f(x, 0, z), 0, 0, 0, scale);
            world.addEntity(tree);
        }
        world.addTerrain("terrain");
        AppInstance.launch(world);
    }
}