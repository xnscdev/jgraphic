package com.github.xnscdev.jgraphic.render;

import com.github.xnscdev.jgraphic.util.ObjectManager;
import org.lwjgl.stb.STBImage;
import org.lwjgl.system.MemoryStack;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL30.*;

/**
 * Stores information about a loaded OpenGL texture.
 * @author XNSC
 */
public class TextureData {
    private final int texture;
    private final int width;
    private final int height;

    public TextureData(int texture, int width, int height) {
        this.texture = texture;
        this.width = width;
        this.height = height;
    }

    public TextureData(String path) {
        try (MemoryStack stack = MemoryStack.stackPush()) {
            IntBuffer width = stack.mallocInt(1);
            IntBuffer height = stack.mallocInt(1);
            IntBuffer channels = stack.mallocInt(1);
            ByteBuffer raw = ObjectManager.storeByteBuffer(path);
            ByteBuffer buffer = STBImage.stbi_load_from_memory(raw, width, height, channels, STBImage.STBI_rgb_alpha);
            if (buffer == null)
                throw new RuntimeException("Failed to load image data for texture " + path);
            this.width = width.get();
            this.height = height.get();
            this.texture = glGenTextures();
            glBindTexture(GL_TEXTURE_2D, texture);
            glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, this.width, this.height, 0, GL_RGBA, GL_UNSIGNED_BYTE, buffer);
            glGenerateMipmap(GL_TEXTURE_2D);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_LINEAR);
            glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_LOD_BIAS, 0);
            glBindTexture(GL_TEXTURE_2D, 0);
            STBImage.stbi_image_free(buffer);
        }
    }

    public int getTexture() {
        return texture;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
