package org.xnsc.jworld;

import org.joml.Vector3f;
import org.xnsc.jworld.render.*;
import org.xnsc.jworld.render.shader.Shader;
import org.xnsc.jworld.render.shader.StaticShader;

public class Main {
    public static void main(String[] args) {
        DisplayManager.createDisplay();
        Renderer renderer = new Renderer();

        float[] vertices = {
                -0.5f, 0.5f, 0,
                -0.5f, -0.5f, 0,
                0.5f, -0.5f, 0,
                0.5f, 0.5f, 0
        };
        float[] textures = {
                0, 0,
                0, 1,
                1, 1,
                1, 0
        };
        int[] indices = {
                0, 1, 3,
                3, 1, 2
        };
        TexturedModel model = new TexturedModel(vertices, textures, indices, "texture.png");
        Entity entity = new Entity(model, new Vector3f(0, 0, -2), 0, 0, 0, 1);

        while (!DisplayManager.closeRequested()) {
            renderer.tick();
            entity.move(0, 0, -0.01f);
            renderer.refresh();
            renderer.render(entity);
            DisplayManager.updateDisplay();
        }

        renderer.clean();
        ObjectManager.clean();
        DisplayManager.closeDisplay();
    }
}