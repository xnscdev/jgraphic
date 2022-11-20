package com.github.xnscdev.jgraphic.util;

import com.github.xnscdev.jgraphic.render.TextureData;
import org.apache.commons.lang3.SystemUtils;
import org.lwjgl.BufferUtils;
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

/**
 * Manages OpenGL objects and loading assets from JAR resources.
 * @author XNSC
 */
public class ObjectManager {
    private static final List<Integer> vaos = new ArrayList<>();
    private static final List<Integer> vbos = new ArrayList<>();
    private static final List<Integer> textures = new ArrayList<>();

    static {
        try {
            NativeUtils.loadLibraryFromJar(getNativeJARName());
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Loads a string of text into a texture to be rendered as a GUI.
     * @param text the text to render
     * @param size font size
     * @param font font for rendering the text
     * @return a texture containing the string
     * @see com.github.xnscdev.jgraphic.gui.GuiText
     */
    public static native TextureData loadText(String text, int size, String font);

    /**
     * Creates a new OpenGL vertex array object.
     * @return the VAO
     */
    public static int createVAO() {
        int vao = glGenVertexArrays();
        glBindVertexArray(vao);
        vaos.add(vao);
        return vao;
    }

    /**
     * Unbinds a bound VAO.
     */
    public static void unbindVAO() {
        glBindVertexArray(0);
    }

    /**
     * Loads a texture from a JAR resource. The texture should be in the appropriate directory.
     * @param name name of the texture
     * @return the OpenGL texture ID
     */
    public static int createTexture(String name) {
        return loadTexture("/textures/" + name + ".png");
    }

    /**
     * Stores floating-point data into an OpenGL buffer and assigns it to an attribute of the currently bound VAO.
     * @param attr attribute number
     * @param size number of floats per vertex
     * @param data the data to store
     */
    public static void storeAttribute(int attr, int size, float[] data) {
        int vbo = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        FloatBuffer buffer = storeFloatBuffer(data);
        glBufferData(GL_ARRAY_BUFFER, buffer, GL_STATIC_DRAW);
        glVertexAttribPointer(attr, size, GL_FLOAT, false, 0, 0);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        vbos.add(vbo);
    }

    /**
     * Stores indices data into an OpenGL buffer and assigns it to an attribute of the currently bound VAO.
     * @param indices the data to store
     */
    public static void storeIndices(int[] indices) {
        int vbo = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, vbo);
        IntBuffer buffer = storeIntBuffer(indices);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, buffer, GL_STATIC_DRAW);
        vbos.add(vbo);
    }

    /**
     * Reads a file from a JAR resource into a {@link ByteBuffer} object. The entire contents of the file are read
     * into memory.
     * @param path JAR resource path to the file, starting with {@code '/'}
     * @return the byte buffer
     * @throws RuntimeException an I/O error occurred while reading the file
     */
    public static ByteBuffer storeByteBuffer(String path) {
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
            throw new RuntimeException(e);
        }
        buffer.flip();
        return MemoryUtil.memSlice(buffer);
    }

    /**
     * Deletes all allocated OpenGL VAOs, buffers, and textures.
     */
    public static void clean() {
        for (int vao : vaos)
            glDeleteVertexArrays(vao);
        for (int vbo : vbos)
            glDeleteBuffers(vbo);
        for (int texture : textures)
            glDeleteTextures(texture);
    }

    private static int loadTexture(String path) {
        TextureData data = new TextureData(path);
        textures.add(data.getTexture());
        return data.getTexture();
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

    private static String getNativeJARName() {
        if (SystemUtils.IS_OS_WINDOWS)
            return "/jtext.dll";
        else if (SystemUtils.IS_OS_MAC_OSX)
            return "/libjtext.dylib";
        else if (SystemUtils.IS_OS_LINUX)
            return "/libjtext.so";
        else
            throw new RuntimeException("Could not determine native platform");
    }
}
