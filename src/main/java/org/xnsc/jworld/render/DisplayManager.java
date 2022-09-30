package org.xnsc.jworld.render;

import org.lwjgl.opengl.GL;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

public class DisplayManager {
    private static final String TITLE = "JWorld";
    private static final int WIDTH = 1280;
    private static final int HEIGHT = 720;
    private static long window;

    public static void createDisplay() {
        if (!glfwInit())
            throw new IllegalStateException("Failed to initialize GLFW");
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
        glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GLFW_TRUE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE);

        window = glfwCreateWindow(WIDTH, HEIGHT, TITLE, 0, 0);
        if (window == 0) {
            glfwTerminate();
            throw new RuntimeException("Failed to create window");
        }

        glfwMakeContextCurrent(window);
        GL.createCapabilities();
        glViewport(0, 0, WIDTH, HEIGHT);
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
}
