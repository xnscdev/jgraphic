package com.github.xnscdev.jgraphic.gui;

import org.joml.Vector2f;
import com.github.xnscdev.jgraphic.text.TextShader;

import java.util.ArrayList;
import java.util.List;

public class GuiManager {
    protected static SolidGuiShader SOLID_SHADER;
    protected static TexturedGuiShader TEXTURED_SHADER;
    protected static TextShader TEXT_SHADER;
    protected static GuiModel GUI_MODEL;
    private static final List<GuiComponent> guis = new ArrayList<>();

    public static void init() {
        SOLID_SHADER = new SolidGuiShader();
        TEXTURED_SHADER = new TexturedGuiShader();
        TEXT_SHADER = new TextShader();
        float[] positions = {0, 0, 0, -1, 1, 0, 1, -1};
        GUI_MODEL = new GuiModel(positions);
    }


    public static void clean() {
        SOLID_SHADER.clean();
        TEXTURED_SHADER.clean();
        TEXT_SHADER.clean();
    }

    public static void tick(double delta) {
        for (GuiComponent gui : guis)
            gui.tick(delta);
    }

    public static void addGui(GuiComponent gui) {
        guis.add(gui);
    }

    public static void render() {
        for (GuiComponent gui : guis)
            gui.render(new Vector2f());
    }
}
