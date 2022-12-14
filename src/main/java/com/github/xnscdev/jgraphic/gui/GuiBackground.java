package com.github.xnscdev.jgraphic.gui;

import com.github.xnscdev.jgraphic.render.Shader;
import com.github.xnscdev.jgraphic.util.MathUtils;
import com.github.xnscdev.jgraphic.util.ObjectManager;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector4f;

import java.util.HashMap;
import java.util.Map;

import static org.lwjgl.opengl.GL30.*;

public class GuiBackground {
    private static final Map<String, Integer> TEXTURES = new HashMap<>();
    private GuiModel model;
    private Vector4f backgroundColor;
    private int texture;
    private Type type;

    public GuiBackground(GuiModel model) {
        this.model = model;
    }

    public void setSolidColor(Vector4f color) {
        type = Type.SOLID;
        backgroundColor = color;
    }

    public GuiModel getModel() {
        return model;
    }

    public void setModel(GuiModel model) {
        this.model = model;
    }

    public int getTexture() {
        return texture;
    }

    public void setTexture(String texture) {
        type = Type.TEXTURED;
        this.texture = TEXTURES.computeIfAbsent(texture, ObjectManager::createTexture);
    }

    public GuiView render(Gui gui, GuiView view, Vector2f screenPos) {
        GuiView newView = view.getVisibleArea(gui.getPosition(), gui.getSize(), screenPos);
        if (newView == null)
            return null;
        switch (type) {
            case SOLID -> renderSolid(newView);
            case TEXTURED -> renderTextured(newView);
        }
        ;
        return newView;
    }

    private void renderSolid(GuiView view) {
        GuiManager.SOLID_SHADER.start();
        bindModel();
        Matrix4f transformMatrix = MathUtils.transformMatrix(view.screenPos(), view.screenSize());
        GuiManager.SOLID_SHADER.loadTransformMatrix(transformMatrix);
        GuiManager.SOLID_SHADER.setColor(backgroundColor);
        glDrawArrays(GL_TRIANGLES, 0, model.getVertexCount());
        unbindModel();
        Shader.stop();
    }

    private void renderTextured(GuiView view) {
        GuiManager.TEXTURED_SHADER.start();
        bindModel();
        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, texture);
        Matrix4f transformMatrix = MathUtils.transformMatrix(view.screenPos(), view.screenSize());
        GuiManager.TEXTURED_SHADER.loadTransformMatrix(transformMatrix);
        GuiManager.TEXTURED_SHADER.loadView(view);
        glDrawArrays(GL_TRIANGLES, 0, model.getVertexCount());
        unbindModel();
        Shader.stop();
    }

    private void bindModel() {
        glBindVertexArray(model.getVAO());
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
