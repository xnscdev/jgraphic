package org.xnsc.jworld.render;

import static org.lwjgl.opengl.GL30.*;

public class Renderer {
    public void init() {
        glClearColor(1, 1, 1, 1);
    }

    public void refresh() {
        glClear(GL_COLOR_BUFFER_BIT);
    }

    public void render(RawModel model) {
        glBindVertexArray(model.getVAO());
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);
        model.preRender();
        glDrawElements(GL_TRIANGLES, model.getVertexCount(), GL_UNSIGNED_INT, 0);
        model.postRender();
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glBindVertexArray(0);
    }
}
