package org.xnsc.jworld.render;

import org.lwjgl.BufferUtils;
import org.lwjgl.stb.STBImage;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.lwjgl.opengl.GL30.*;

public class ObjectManager {
    private static final List<Integer> vaos = new ArrayList<>();
    private static final List<Integer> vbos = new ArrayList<>();
    private static final List<Integer> textures = new ArrayList<>();

    public static int createVAO() {
        int vao = glGenVertexArrays();
        glBindVertexArray(vao);
        vaos.add(vao);
        return vao;
    }

    public static void unbindVAO() {
        glBindVertexArray(0);
    }

    public static int createTexture(String path) {
        try (MemoryStack stack = MemoryStack.stackPush()) {
            IntBuffer width = stack.mallocInt(1);
            IntBuffer height = stack.mallocInt(1);
            IntBuffer channels = stack.mallocInt(1);
            ByteBuffer raw = storeByteBuffer(path);
            if (raw == null)
                throw new IllegalArgumentException("Failed to load buffer for texture " + path);
            ByteBuffer buffer = STBImage.stbi_load_from_memory(raw, width, height, channels, STBImage.STBI_rgb_alpha);
            if (buffer == null)
                throw new RuntimeException("Failed to load image data for texture " + path);

            int texture = glGenTextures();
            glBindTexture(GL_TEXTURE_2D, texture);
            glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width.get(), height.get(), 0, GL_RGBA, GL_UNSIGNED_BYTE, buffer);
            glGenerateMipmap(GL_TEXTURE_2D);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_LINEAR);
            glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_LOD_BIAS, 0);
            glBindTexture(GL_TEXTURE_2D, 0);
            STBImage.stbi_image_free(buffer);
            textures.add(texture);
            return texture;
        }
    }

    public static void storeAttribute(int attr, int size, float[] data) {
        int vbo = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        FloatBuffer buffer = storeFloatBuffer(data);
        glBufferData(GL_ARRAY_BUFFER, buffer, GL_STATIC_DRAW);
        glVertexAttribPointer(attr, size, GL_FLOAT, false, 0, 0);
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
        for (int texture : textures)
            glDeleteTextures(texture);
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

    private static ByteBuffer resizeByteBuffer(ByteBuffer buffer, int capacity) {
        ByteBuffer newBuffer = BufferUtils.createByteBuffer(capacity);
        buffer.flip();
        newBuffer.put(buffer);
        return newBuffer;
    }

    private static ByteBuffer storeByteBuffer(String path) {
        ByteBuffer buffer;
        try (ReadableByteChannel channel = Channels.newChannel(Objects.requireNonNull(ObjectManager.class.getResourceAsStream(path)))) {
            buffer = BufferUtils.createByteBuffer(1024);
            while (true) {
                int bytes = channel.read(buffer);
                if (bytes == -1)
                    break;
                if (buffer.remaining() == 0)
                    buffer = resizeByteBuffer(buffer, buffer.capacity() * 3 / 2);
            }
        }
        catch (IOException e) {
            return null;
        }
        buffer.flip();
        return MemoryUtil.memSlice(buffer);
    }
}
