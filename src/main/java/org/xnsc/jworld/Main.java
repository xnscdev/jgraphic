package org.xnsc.jworld;

import org.xnsc.jworld.render.DisplayManager;
import org.xnsc.jworld.render.ObjectManager;
import org.xnsc.jworld.render.RawModel;
import org.xnsc.jworld.render.Renderer;
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
        int[] indices = {
                0, 1, 3,
                3, 1, 2
        };
        RawModel model = new RawModel(vertices, indices);

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