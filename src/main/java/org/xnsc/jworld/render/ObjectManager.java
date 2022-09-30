package org.xnsc.jworld.render;

import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL30.*;

public class ObjectManager {
    private static final List<Integer> vaos = new ArrayList<>();
    private static final List<Integer> vbos = new ArrayList<>();

    public static int createVAO() {
        int vao = glGenVertexArrays();
        glBindVertexArray(vao);
        vaos.add(vao);
        return vao;
    }

    public static void unbindVAO() {
        glBindVertexArray(0);
    }

    public static void storeVertices(int attr, float[] data) {
        int vbo = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        FloatBuffer buffer = storeFloatBuffer(data);
        glBufferData(GL_ARRAY_BUFFER, buffer, GL_STATIC_DRAW);
        glVertexAttribPointer(attr, 3, GL_FLOAT, false, 0, 0);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        vbos.add(vbo);
    }

    public static void storeIndices(int[] indices) {
        int vbo = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, vbo);
        IntBuffer buffer = storeIntBuffer(indices);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, buffer, GL_STATIC_DRAW);
        vbos.add(vbo);
    }

    public static void clean() {
        for (int vao : vaos)
            glDeleteVertexArrays(vao);
        for (int vbo : vbos)
            glDeleteBuffers(vbo);
    }

    private static FloatBuffer storeFloatBuffer(float[] data) {
        FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
        buffer.put(data);
        buffer.flip();
        return buffer;
    }

    private static IntBuffer storeIntBuffer(int[] data) {
        IntBuffer buffer = BufferUtils.createIntBuffer(data.length);
        buffer.put(data);
        buffer.flip();
        return buffer;
    }
}
