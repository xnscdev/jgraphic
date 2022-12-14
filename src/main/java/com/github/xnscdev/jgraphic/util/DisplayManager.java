package com.github.xnscdev.jgraphic.util;

import com.github.xnscdev.jgraphic.AppInstance;
import com.github.xnscdev.jgraphic.gui.GuiManager;
import org.joml.Vector2f;
import org.lwjgl.glfw.GLFWCharCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWMouseButtonCallback;
import org.lwjgl.glfw.GLFWScrollCallback;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryStack;

import java.nio.DoubleBuffer;

import static org.lwjgl.glfw.GLFW.*;

/**
 * Utility class that handles the display, mouse, and keyboard.
 * @author XNSC
 */
public class DisplayManager {
    private static String title;
    private static int width;
    private static int height;
    private static long window;
    private static double lastUpdate;
    private static float scrollOffset;
    private static Vector2f lastMousePos = new Vector2f();

    /**
     * Creates a new window for displaying content.
     * @param title title of the window
     * @param width window width in pixels
     * @param height window height in pixels
     */
    public static void createDisplay(String title, int width, int height) {
        DisplayManager.title = title;
        DisplayManager.width = width;
        DisplayManager.height = height;
        if (!glfwInit())
            throw new IllegalStateException("Failed to initialize GLFW");
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 4);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 0);
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
        glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GLFW_TRUE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE);

        window = glfwCreateWindow(width, height, title, 0, 0);
        if (window == 0) {
            glfwTerminate();
            throw new RuntimeException("Failed to create window");
        }

        glfwSetKeyCallback(window, new GLFWKeyCallback() {
            @Override
            public void invoke(long window, int key, int scancode, int action, int mods) {
                if (GuiManager.getFocusedComponent() != null)
                    GuiManager.getFocusedComponent().receiveKey(key, action, mods);
            }
        });
        glfwSetCharCallback(window, new GLFWCharCallback() {
            @Override
            public void invoke(long window, int codepoint) {
                if (GuiManager.getFocusedComponent() != null)
                    GuiManager.getFocusedComponent().receiveChar(codepoint);
            }
        });
        glfwSetScrollCallback(window, new GLFWScrollCallback() {
            @Override
            public void invoke(long window, double xoffset, double yoffset) {
                scrollOffset = (float) yoffset;
            }
        });
        glfwSetMouseButtonCallback(window, new GLFWMouseButtonCallback() {
            @Override
            public void invoke(long window, int button, int action, int mods) {
                if (button == GLFW_MOUSE_BUTTON_RIGHT && action == GLFW_PRESS)
                    lastMousePos = getMousePos();
                if (button == GLFW_MOUSE_BUTTON_LEFT) {
                    Vector2f pos = getMousePos();
                    if (action == GLFW_PRESS)
                        AppInstance.WORLD.mousePressed(pos.x, pos.y);
                    else if (action == GLFW_RELEASE)
                        AppInstance.WORLD.mouseReleased(pos.x, pos.y);
                }
            }
        });

        glfwMakeContextCurrent(window);
        GL.createCapabilities();
    }

    /**
     * Calculates the number of seconds since this function was last called.
     * @return time delta
     */
    public static double deltaTime() {
        double now = glfwGetTime();
        double delta = now - lastUpdate;
        lastUpdate = now;
        return delta;
    }

    /**
     * Redraws the contents of the display.
     */
    public static void updateDisplay() {
        glfwSwapBuffers(window);
        glfwPollEvents();
    }

    /**
     * Closes the display.
     */
    public static void closeDisplay() {
        glfwTerminate();
    }

    /**
     * Checks whether the window should close.
     * @return {@code true} if the window should close
     */
    public static boolean closeRequested() {
        return glfwWindowShouldClose(window);
    }

    /**
     * Checks if a keyboard key is currently pressed.
     * @param key constant representing the key to check
     * @return {@code true} if the key is pressed
     */
    public static boolean keyDown(int key) {
        return glfwGetKey(window, key) == GLFW_PRESS;
    }

    /**
     * Checks if a mouse button is currently pressed.
     * @param button constant representing the button to check
     * @return {@code true} if the button is pressed
     */
    public static boolean mouseDown(int button) {
        return glfwGetMouseButton(window, button) == GLFW_PRESS;
    }

    public static String getTitle() {
        return title;
    }

    public static int getWidth() {
        return width;
    }

    public static int getHeight() {
        return height;
    }

    /**
     * Gets the distance the mouse was scrolled since the last time this function was called or the scroll wheel
     * was released.
     * @return the scroll distance
     */
    public static float getScrollOffset() {
        float offset = scrollOffset;
        scrollOffset = 0;
        return offset;
    }

    /**
     * Gets the offset of the current mouse position from the last time this function was called.
     * @return the mouse offset
     */
    public static Vector2f getMouseOffset() {
        Vector2f pos = getMousePos();
        Vector2f offset = new Vector2f(pos).sub(lastMousePos);
        lastMousePos = pos;
        return offset;
    }

    /**
     * Gets the current mouse position in device coordinates.
     * @return the position vector
     */
    public static Vector2f getMousePos() {
        try (MemoryStack stack = MemoryStack.stackPush()) {
            DoubleBuffer x = stack.mallocDouble(1);
            DoubleBuffer y = stack.mallocDouble(1);
            glfwGetCursorPos(window, x, y);
            return new Vector2f((float) x.get(), (float) y.get());
        }
    }
}
