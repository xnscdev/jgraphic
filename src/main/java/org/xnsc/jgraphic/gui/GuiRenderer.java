package org.xnsc.jgraphic.gui;

import org.joml.Matrix4f;
import org.xnsc.jgraphic.util.MatrixUtils;
import org.xnsc.jgraphic.world.Shader;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL30.*;

public class GuiRenderer {
    private final SolidGuiShader solidShader = new SolidGuiShader();
    private final TexturedGuiShader texturedShader = new TexturedGuiShader();
    private final List<SolidGui> solidGuis = new ArrayList<>();
    private final List<TexturedGui> texturedGuis = new ArrayList<>();
    private final GuiModel model;

    public GuiRenderer() {
        float[] positions = {0, 0, 0, -1, 1, 0, 1, -1};
        model = new GuiModel(positions);
    }

    public void clean() {
        solidShader.clean();
        texturedShader.clean();
    }

    public void render() {
        solidShader.start();
        glBindVertexArray(model.getVAO());
        glEnableVertexAttribArray(0);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glDisable(GL_DEPTH_TEST);
        for (SolidGui gui : solidGuis) {
            Matrix4f transformMatrix = MatrixUtils.transformMatrix(gui.getPosition(), gui.getSize());
            solidShader.loadTransformMatrix(transformMatrix);
            solidShader.setColor(gui.getBackgroundColor());
            glDrawArrays(GL_TRIANGLE_STRIP, 0, model.getVertexCount());
        }
        texturedShader.start();
        for (TexturedGui gui : texturedGuis) {
            gui.loadTexture();
            Matrix4f transformMatrix = MatrixUtils.transformMatrix(gui.getPosition(), gui.getSize());
            texturedShader.loadTransformMatrix(transformMatrix);
            glDrawArrays(GL_TRIANGLE_STRIP, 0, model.getVertexCount());
        }
        glEnable(GL_DEPTH_TEST);
        glDisable(GL_BLEND);
        glDisableVertexAttribArray(0);
        glBindVertexArray(0);
        Shader.stop();
    }

    public void addGui(Gui gui) {
        if (gui instanceof SolidGui solidGui)
            solidGuis.add(solidGui);
        else if (gui instanceof TexturedGui texturedGui)
            texturedGuis.add(texturedGui);
        else
            throw new IllegalStateException("Attempted to render invalid GUI type");
    }

    public void addDebugGui() {
        solidGuis.add(new DebugGui());
    }
}
