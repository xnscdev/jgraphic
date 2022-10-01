package org.xnsc.jworld.render;

import org.joml.Vector3f;
import org.xnsc.jworld.render.util.OBJLoader;

public class World {
    private final WorldRenderer renderer = new WorldRenderer();
    private final RawModel model = OBJLoader.loadModel("dragon");
    private final Entity entity = new Entity(model, new Vector3f(0, -5, -20), 0, 0, 0, 1);
    private final LightSource light = new LightSource(new Vector3f(10, 5, 0), new Vector3f(1, 1, 1));
    private final Camera camera = new Camera();

    public void tick() {
        camera.tick();
        entity.rotate(0, 1, 0);
        renderer.refresh();
        renderer.render(entity, camera, light);
    }

    public void clean() {
        renderer.clean();
    }
}
