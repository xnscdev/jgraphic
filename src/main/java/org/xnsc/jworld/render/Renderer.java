package org.xnsc.jworld.render;

import static org.lwjgl.opengl.GL30.*;

public class Renderer {
    public void init() {
        glClearColor(1, 0, 0, 1);
    }

    public void refresh() {
        glClear(GL_COLOR_BUFFER_BIT);
    }

    public void render(RawModel model) {
        glBindVertexArray(model.getVAO());
        glEnableVertexAttribArray(0);
        glDrawElements(GL_TRIANGLES, model.getVertexCount(), GL_UNSIGNED_INT, 0);
        glDisableVertexAttribArray(0);
        glBindVertexArray(0);
    }
}
