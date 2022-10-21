package org.xnsc.jgraphic.gui;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.xnsc.jgraphic.render.Shader;
import org.xnsc.jgraphic.util.MathUtils;
import org.xnsc.jgraphic.util.ObjectManager;

import static org.lwjgl.opengl.GL30.*;

public class GuiBackground {
    private Vector3f backgroundColor;
    private int texture;
    private Type type;

    public void setSolidColor(Vector3f color) {
        type = Type.SOLID;
        backgroundColor = color;
    }

    public void setTexture(String texture) {
        type = Type.TEXTURED;
        this.texture = ObjectManager.createTexture(texture);
    }

    public void render(Vector2f pos, Vector2f size) {
        if (type == null)
            return;
        switch (type) {
        case SOLID -> renderSolid(pos, size);
        case TEXTURED -> renderTextured(pos, size);
        }
    }

    private void renderSolid(Vector2f pos, Vector2f size) {
        GuiManager.SOLID_SHADER.start();
        bindModel();
        Matrix4f transformMatrix = MathUtils.transformMatrix(pos, size);
        GuiManager.SOLID_SHADER.loadTransformMatrix(transformMatrix);
        GuiManager.SOLID_SHADER.setColor(backgroundColor);
        glDrawArrays(GL_TRIANGLE_STRIP, 0, GuiManager.GUI_MODEL.getVertexCount());
        unbindModel();
        Shader.stop();
    }

    private void renderTextured(Vector2f pos, Vector2f size) {
        GuiManager.TEXTURED_SHADER.start();
        bindModel();
        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, texture);
        Matrix4f transformMatrix = MathUtils.transformMatrix(pos, size);
        GuiManager.TEXTURED_SHADER.loadTransformMatrix(transformMatrix);
        glDrawArrays(GL_TRIANGLE_STRIP, 0, GuiManager.GUI_MODEL.getVertexCount());
        unbindModel();
        Shader.stop();
    }

    private void bindModel() {
        glBindVertexArray(GuiManager.GUI_MODEL.getVAO());
        glEnableVertexAttribArray(0);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glDisable(GL_DEPTH_TEST);
    }

    private void unbindModel() {
        glEnable(GL_DEPTH_TEST);
        glDisable(GL_BLEND);
        glDisableVertexAttribArray(0);
        glBindVertexArray(0);
    }

    private enum Type {
        SOLID,
        TEXTURED
    }
}