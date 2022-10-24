package com.github.xnscdev.jgraphic.gui;

import org.joml.Vector2f;
import com.github.xnscdev.jgraphic.text.TextShader;

import java.util.ArrayList;
import java.util.List;

public class GuiManager {
    private static final RootComponent ROOT_COMPONENT = new RootComponent();
    private static final List<GuiComponent> guis = new ArrayList<>();
    public static GuiModel RECT_MODEL;
    public static GuiModel STAR_MODEL;
    protected static SolidGuiShader SOLID_SHADER;
    protected static TexturedGuiShader TEXTURED_SHADER;
    protected static TextShader TEXT_SHADER;

    public static void init() {
        SOLID_SHADER = new SolidGuiShader();
        TEXTURED_SHADER = new TexturedGuiShader();
        TEXT_SHADER = new TextShader();
        RECT_MODEL = new GuiModel(new float[]{0, 0, 0, -1, 1, 0, 1, -1});
        STAR_MODEL = new GuiModel(new float[]{
                0, 1,
                -0.322f, 0.443f,
                0.322f, 0.443f,
                0.322f, 0.443f,
                -0.322f, 0.443f,
                0.521f, -0.169f,
                0.951f, 0.309f,
                0.322f, 0.443f,
                0.521f, -0.169f,
                -0.322f, 0.443f,
                -0.951f, 0.309f,
                -0.521f, -0.169f,
                -0.322f, 0.443f,
                -0.521f, -0.169f,
                0, -0.547f,
                -0.322f, 0.443f,
                0, -0.547f,
                0.521f, -0.169f,
                -0.521f, -0.169f,
                -0.588f, -0.809f,
                0, -0.547f,
                0.521f, -0.169f,
                0, -0.547f,
                0.588f, -0.809f
        });
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
        gui.setParent(ROOT_COMPONENT);
    }

    public static void removeGui(GuiComponent gui) {
        if (guis.remove(gui))
            gui.setParent(null);
    }

    public static void render() {
        for (GuiComponent gui : guis)
            gui.render();
    }
}
