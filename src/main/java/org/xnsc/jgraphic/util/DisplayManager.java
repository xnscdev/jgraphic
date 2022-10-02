package org.xnsc.jgraphic.util;

import org.joml.Vector2f;
import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.glfw.GLFWMouseButtonCallback;
import org.lwjgl.glfw.GLFWScrollCallback;
import org.lwjgl.opengl.GL;

import static org.lwjgl.glfw.GLFW.*;

public class DisplayManager {
    private static String title;
    private static int width;
    private static int height;
    private static long window;
    private static double lastUpdate;
    private static float lastScroll;
    private static float scrollOffset;
    private static Vector2f lastMousePos = new Vector2f();
    private static Vector2f mouseOffset;

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

        glfwSetScrollCallback(window, new GLFWScrollCallback() {
            @Override
            public void invoke(long window, double xoffset, double yoffset) {
                scrollOffset = (float) yoffset;
            }
        });
        glfwSetCursorPosCallback(window, new GLFWCursorPosCallback() {
            @Override
            public void invoke(long window, double xpos, double ypos) {
                mouseOffset = new Vector2f((float) xpos, (float) ypos);
            }
        });
        glfwSetMouseButtonCallback(window, new GLFWMouseButtonCallback() {
            @Override
            public void invoke(long window, int button, int action, int mods) {
                if (button == GLFW_MOUSE_BUTTON_RIGHT && action == GLFW_PRESS)
                    lastMousePos = mouseOffset;
            }
        });

        glfwMakeContextCurrent(window);
        GL.createCapabilities();
    }

    public static double deltaTime() {
        double now = glfwGetTime();
        double delta = now - lastUpdate;
        lastUpdate = now;
        return delta;
    }

    public static void updateDisplay() {
        glfwSwapBuffers(window);
        glfwPollEvents();
    }

    public static void closeDisplay() {
        glfwTerminate();
    }

    public static boolean closeRequested() {
        return glfwWindowShouldClose(window);
    }

    public static boolean keyDown(int key) {
        return glfwGetKey(window, key) == GLFW_PRESS;
    }

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

    public static float getScrollOffset() {
        float offset = scrollOffset - lastScroll;
        scrollOffset = lastScroll;
        return offset;
    }

    public static Vector2f getMouseOffset() {
        Vector2f offset = new Vector2f(mouseOffset).sub(lastMousePos);
        lastMousePos = mouseOffset;
        return offset;
    }
}
