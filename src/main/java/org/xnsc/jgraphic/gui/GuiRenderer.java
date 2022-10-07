package org.xnsc.jgraphic.gui;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.xnsc.jgraphic.text.FontType;
import org.xnsc.jgraphic.text.TextRenderer;
import org.xnsc.jgraphic.util.MathUtils;
import org.xnsc.jgraphic.render.Shader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.lwjgl.opengl.GL30.*;

public class GuiRenderer {
    private static final Map<FontType, Map<GuiText, Vector2f>> texts = new HashMap<>();
    private final TextRenderer textRenderer = new TextRenderer();
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
        textRenderer.clean();
    }

    public void tick(double delta) {
        for (Gui gui : solidGuis) {
            gui.tick(delta);
        }
        for (Gui gui : texturedGuis) {
            gui.tick(delta);
        }
    }

    public void render() {
        solidShader.start();
        glBindVertexArray(model.getVAO());
        glEnableVertexAttribArray(0);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glDisable(GL_DEPTH_TEST);
        for (SolidGui gui : solidGuis) {
            Matrix4f transformMatrix = MathUtils.transformMatrix(gui.getScreenPosition(), gui.getScreenSize());
            solidShader.loadTransformMatrix(transformMatrix);
            solidShader.setColor(gui.getBackgroundColor());
            glDrawArrays(GL_TRIANGLE_STRIP, 0, model.getVertexCount());
        }
        texturedShader.start();
        for (TexturedGui gui : texturedGuis) {
            gui.loadTexture();
            Matrix4f transformMatrix = MathUtils.transformMatrix(gui.getScreenPosition(), gui.getScreenSize());
            texturedShader.loadTransformMatrix(transformMatrix);
            glDrawArrays(GL_TRIANGLE_STRIP, 0, model.getVertexCount());
        }
        glEnable(GL_DEPTH_TEST);
        glDisable(GL_BLEND);
        glDisableVertexAttribArray(0);
        glBindVertexArray(0);
        Shader.stop();
        renderTexts();
    }

    private void renderTexts() {
        for (Gui gui : solidGuis) {
            renderText(gui);
        }
        for (Gui gui : texturedGuis) {
            renderText(gui);
        }
        textRenderer.render(texts);
        texts.clear();
    }

    private void renderText(Gui gui) {
        for (GuiText text : gui.getTexts()) {
            FontType font = text.getFont();
            Map<GuiText, Vector2f> batch = texts.computeIfAbsent(font, k -> new HashMap<>());
            batch.put(text, gui.getTextPosition());
        }
    }

    public void addGui(Gui gui) {
        if (gui instanceof SolidGui solidGui)
            solidGuis.add(solidGui);
        else if (gui instanceof TexturedGui texturedGui)
            texturedGuis.add(texturedGui);
        else
            throw new IllegalStateException("Attempted to render invalid GUI type");
    }
}
