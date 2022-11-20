package com.github.xnscdev.jgraphic.gui;

import com.github.xnscdev.jgraphic.util.DisplayManager;
import org.joml.Vector2f;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

/**
 * Manages general GUI-related functions.
 * @author XNSC
 */
public class GuiManager {
    private static final RootComponent ROOT_COMPONENT = new RootComponent();
    private static final List<GuiComponent> guis = new ArrayList<>();

    /**
     * A rectangular model suitable as a GUI background. The GUI's position will be the top left corner
     * of the rectangle.
     */
    public static GuiModel RECT_MODEL;

    /**
     * A five-pointed star model suitable as a GUI background. The GUI's position is the center of the star
     * and its size is the distance from the center to a tip.
     */
    public static GuiModel STAR_MODEL;
    protected static SolidGuiShader SOLID_SHADER;
    protected static TexturedGuiShader TEXTURED_SHADER;
    protected static TextShader TEXT_SHADER;
    protected static GuiComponent focusedComponent;

    /**
     * Initializes the JGraphic GUI engine. This function usually does not need to be explicitly called since it is
     * called automatically when a JGraphic application is initialized.
     * @see com.github.xnscdev.jgraphic.AppInstance#init(String, int, int)
     */
    public static void init() {
        SOLID_SHADER = new SolidGuiShader();
        TEXTURED_SHADER = new TexturedGuiShader();
        TEXT_SHADER = new TextShader();
        RECT_MODEL = new GuiModel(new float[]{
                0, 0,
                0, -1,
                1, 0,
                1, 0,
                0, -1,
                1, -1
        });
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

    /**
     * Frees resources allocated by the JGraphic GUI engine.
     */
    public static void clean() {
        SOLID_SHADER.clean();
        TEXTURED_SHADER.clean();
        TEXT_SHADER.clean();
    }

    /**
     * Sends a tick event to all top-level GUIs. This function is called every world tick.
     * @param delta number of seconds since the last world tick
     */
    public static void tick(double delta) {
        for (GuiComponent gui : guis)
            gui.tick(delta);
    }

    /**
     * Adds a top-level GUI to be rendered on-screen. The GUI should not have an existing parent.
     * @param gui the GUI to add
     */
    public static void addGui(GuiComponent gui) {
        guis.add(gui);
        gui.setParent(ROOT_COMPONENT);
    }

    /**
     * Removes a top-level GUI so it is no longer rendered. Its parent will be reset to {@code null}.
     * @param gui the GUI to remove
     */
    public static void removeGui(GuiComponent gui) {
        if (guis.remove(gui))
            gui.setParent(null);
    }

    public static void render() {
        Vector2f pos = new Vector2f();
        GuiView view = new GuiView(pos, new Vector2f(DisplayManager.getWidth(), DisplayManager.getHeight()), null, null);
        for (GuiComponent gui : guis)
            gui.render(view, pos);
    }

    public static boolean mousePressed(float x, float y) {
        ListIterator<GuiComponent> iter = guis.listIterator(guis.size());
        while (iter.hasPrevious()) {
            GuiComponent gui = iter.previous();
            if (gui.containsAbsolutePoint(x, y) && gui.mousePressed(x, y))
                return true;
        }
        if (focusedComponent != null)
            focusedComponent.unfocused();
        focusedComponent = null;
        return false;
    }

    public static boolean mouseReleased(float x, float y) {
        ListIterator<GuiComponent> iter = guis.listIterator(guis.size());
        while (iter.hasPrevious()) {
            GuiComponent gui = iter.previous();
            if (gui.containsAbsolutePoint(x, y) && gui.mouseReleased(x, y))
                return true;
        }
        resetFocusedComponent();
        return false;
    }

    /**
     * Gets the currently focused component. A single GUI component may have focus, in which keyboard events
     * are directed to the GUI.
     * @return the currently focused component, or {@code null} if no GUI is focused
     */
    public static GuiComponent getFocusedComponent() {
        return focusedComponent;
    }

    /**
     * Removes focus from the currently focused component, causing it to call its unfocused handler. If no
     * component is currently focused, no action is performed.
     * @see GuiComponent#unfocused()
     */
    public static void resetFocusedComponent() {
        if (focusedComponent != null)
            focusedComponent.dropFocus();
    }
}
