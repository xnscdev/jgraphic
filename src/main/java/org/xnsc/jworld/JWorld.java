package org.xnsc.jworld;

import org.joml.Vector3f;
import org.xnsc.jworld.render.World;
import org.xnsc.jworld.render.entity.Entity;
import org.xnsc.jworld.render.model.ModelData;
import org.xnsc.jworld.render.model.TexturedModel;
import org.xnsc.jworld.render.util.OBJLoader;

import java.util.Random;

public class JWorld extends World {
    public JWorld() {
        super();
        getCamera().setPosition(new Vector3f(0, 50, 100));
        ModelData data = OBJLoader.loadModel("model");
        TexturedModel model = new TexturedModel(data, "model");
        Random random = new Random();
        for (int i = 0; i < 200; i++) {
            float x = random.nextFloat() * 100;
            float y = random.nextFloat() * 100;
            float z = random.nextFloat() * 100;
            addEntity(new Entity(model, new Vector3f(x, y, z), random.nextFloat() * 180, random.nextFloat() * 180, 0, 1));
        }
    }
}
