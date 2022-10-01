package org.xnsc.jworld.render;

import org.joml.Vector3f;
import org.xnsc.jworld.render.util.OBJLoader;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class World {
    private final WorldRenderer renderer = new WorldRenderer();
    private final LightSource light = new LightSource(new Vector3f(10, 5, 0), new Vector3f(1, 1, 1));
    private final Camera camera = new Camera();
    private final List<Entity> entities = new ArrayList<>();

    public World() {
        Random random = new Random();
        RawModel model = OBJLoader.loadModel("model");
        for (int i = 0; i < 200; i++) {
            float x = random.nextFloat() * 100 - 50;
            float y = random.nextFloat() * 100 - 50;
            float z = random.nextFloat() * -300;
            entities.add(new Entity(model, new Vector3f(x, y, z), random.nextFloat() * 180, random.nextFloat() * 180, 0, 1));
        }
    }

    public void tick() {
        camera.tick();
        for (Entity entity : entities) {
            renderer.processEntity(entity);
        }
        renderer.render(camera, light);
    }

    public void clean() {
        renderer.clean();
    }
}
