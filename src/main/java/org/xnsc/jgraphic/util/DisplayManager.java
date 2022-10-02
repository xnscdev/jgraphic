package org.xnsc.jgraphic.util;

import org.lwjgl.opengl.GL;

import static org.lwjgl.glfw.GLFW.*;

public class DisplayManager {
    private static String title;
    private static int width;
    private static int height;
    private static long window;

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

        glfwMakeContextCurrent(window);
        GL.createCapabilities();
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

    public static String getTitle() {
        return title;
    }

    public static int getWidth() {
        return width;
    }

    public static int getHeight() {
        return height;
    }
}
