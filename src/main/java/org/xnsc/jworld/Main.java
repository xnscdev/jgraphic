package org.xnsc.jworld;

import org.joml.Vector3f;
import org.xnsc.jgraphic.AppInstance;
import org.xnsc.jgraphic.entity.Entity;
import org.xnsc.jgraphic.model.TexturedModel;
import org.xnsc.jgraphic.world.World;

import java.util.Random;

public class Main {
    public static void main(String[] args) {
        AppInstance app = new AppInstance("JWorld", 1280, 760);
        World world = new World();
        world.getCamera().setPosition(new Vector3f(0, 50, 100));
        world.setAmbientThreshold(0.2f);
        TexturedModel model = new TexturedModel("model");
        Random random = new Random();
        for (int i = 0; i < 200; i++) {
            float x = random.nextFloat() * 100;
            float y = random.nextFloat() * 100;
            float z = random.nextFloat() * 100;
            world.addEntity(new Entity(model, new Vector3f(x, y, z), random.nextFloat() * 180, random.nextFloat() * 180, 0, 1, 20, new Vector3f(0, world.getGravityAccel(), 0)));
        }
        app.setWorld(world);
        app.mainLoop();
        app.clean();
    }
}