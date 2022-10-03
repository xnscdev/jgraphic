package org.xnsc.jgraphic.gui;

import org.xnsc.jgraphic.util.ObjectManager;

public class GuiModel {
    private final int vao;
    private final int vertexCount;

    public GuiModel(float[] vertices) {
        vao = ObjectManager.createVAO();
        vertexCount = vertices.length / 2;
        ObjectManager.storeAttribute(0, 2, vertices);
        ObjectManager.unbindVAO();
    }

    public int getVAO() {
        return vao;
    }

    public int getVertexCount() {
        return vertexCount;
    }
}
