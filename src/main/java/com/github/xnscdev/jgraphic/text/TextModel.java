package com.github.xnscdev.jgraphic.text;

import com.github.xnscdev.jgraphic.model.ModelMesh;
import org.joml.Vector2f;

public class TextModel extends ModelMesh {
    private final Vector2f textSize;

    public TextModel(float[] vertices, float[] textures, Vector2f textSize) {
        super(vertices, textures, 2);
        this.textSize = textSize;
    }

    public Vector2f getTextSize() {
        return textSize;
    }
}
