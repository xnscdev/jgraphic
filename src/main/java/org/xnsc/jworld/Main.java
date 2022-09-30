package org.xnsc.jworld;

import org.xnsc.jworld.render.*;
import org.xnsc.jworld.render.shader.Shader;
import org.xnsc.jworld.render.shader.StaticShader;

public class Main {
    public static void main(String[] args) {
        DisplayManager.createDisplay();
        Renderer renderer = new Renderer();
        renderer.init();

        StaticShader shader = new StaticShader();
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

        while (!DisplayManager.closeRequested()) {
            renderer.refresh();
            shader.start();
            renderer.render(model);
            Shader.stop();
            DisplayManager.updateDisplay();
        }

        shader.clean();
        ObjectManager.clean();
        DisplayManager.closeDisplay();
    }
}