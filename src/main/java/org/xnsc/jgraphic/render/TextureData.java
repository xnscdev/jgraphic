package org.xnsc.jgraphic.render;

import org.lwjgl.stb.STBImage;
import org.lwjgl.system.MemoryStack;
import org.xnsc.jgraphic.util.ObjectManager;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

public class TextureData {
    private final ByteBuffer buffer;
    private final int width;
    private final int height;

    public TextureData(String path) {
        try (MemoryStack stack = MemoryStack.stackPush()) {
            IntBuffer width = stack.mallocInt(1);
            IntBuffer height = stack.mallocInt(1);
            IntBuffer channels = stack.mallocInt(1);
            ByteBuffer raw = ObjectManager.storeByteBuffer(path);
            if (raw == null)
                throw new IllegalArgumentException("Failed to load buffer for texture " + path);
            ByteBuffer buffer = STBImage.stbi_load_from_memory(raw, width, height, channels, STBImage.STBI_rgb_alpha);
            if (buffer == null)
                throw new RuntimeException("Failed to load image data for texture " + path);
            this.buffer = buffer;
            this.width = width.get();
            this.height = height.get();
        }
    }

    public ByteBuffer getBuffer() {
        return buffer;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public void clean() {
        STBImage.stbi_image_free(buffer);
    }
}
