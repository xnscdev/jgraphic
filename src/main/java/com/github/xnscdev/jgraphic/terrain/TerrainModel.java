package com.github.xnscdev.jgraphic.terrain;

import com.github.xnscdev.jgraphic.model.ModelData;
import com.github.xnscdev.jgraphic.model.RawModel;
import com.github.xnscdev.jgraphic.util.ObjectManager;

import static org.lwjgl.opengl.GL13.*;

public class TerrainModel extends RawModel {
    private final int backgroundTexture;
    private final int redTexture;
    private final int greenTexture;
    private final int blueTexture;
    private final int blendMap;

    public TerrainModel(ModelData data, String backgroundTexture, String redTexture, String greenTexture, String blueTexture, String blendMap) {
        super(data);
        this.backgroundTexture = ObjectManager.createTexture(backgroundTexture);
        this.redTexture = ObjectManager.createTexture(redTexture);
        this.greenTexture = ObjectManager.createTexture(greenTexture);
        this.blueTexture = ObjectManager.createTexture(blueTexture);
        this.blendMap = ObjectManager.createTexture(blendMap);
    }

    @Override
    public void preRender() {
        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, backgroundTexture);
        glActiveTexture(GL_TEXTURE1);
        glBindTexture(GL_TEXTURE_2D, redTexture);
        glActiveTexture(GL_TEXTURE2);
        glBindTexture(GL_TEXTURE_2D, greenTexture);
        glActiveTexture(GL_TEXTURE3);
        glBindTexture(GL_TEXTURE_2D, blueTexture);
        glActiveTexture(GL_TEXTURE4);
        glBindTexture(GL_TEXTURE_2D, blendMap);
    }
}
