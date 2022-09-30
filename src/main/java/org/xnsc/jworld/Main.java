package org.xnsc.jworld;

import org.xnsc.jworld.render.DisplayManager;
import org.xnsc.jworld.render.ObjectManager;
import org.xnsc.jworld.render.RawModel;
import org.xnsc.jworld.render.Renderer;

public class Main {
    public static void main(String[] args) {
        DisplayManager.createDisplay();
        Renderer renderer = new Renderer();
        renderer.init();

        float[] vertices = {
                -0.5f, 0.5f, 0f,
                -0.5f, -0.5f, 0f,
                0.5f, -0.5f, 0f,
                0.5f, -0.5f, 0f,
                0.5f, 0.5f, 0f,
                -0.5f, 0.5f, 0f
        };
        RawModel model = new RawModel(vertices);

        while (!DisplayManager.closeRequested()) {
            renderer.refresh();
            renderer.render(model);
            DisplayManager.updateDisplay();
        }

        ObjectManager.clean();
        DisplayManager.closeDisplay();
    }
}