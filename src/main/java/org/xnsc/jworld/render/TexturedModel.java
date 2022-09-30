package org.xnsc.jworld.render;

public class TexturedModel extends RawModel {
    private final int texture;

    public TexturedModel(float[] vertices, float[] textures, int[] indices, String texture) {
        super(vertices, textures, indices);
        this.texture = ObjectManager.createTexture("/textures/" + texture);
    }

    public int getTexture() {
        return texture;
    }
}
