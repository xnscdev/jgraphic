package org.xnsc.jgraphic.text;

import org.joml.Vector2f;
import org.xnsc.jgraphic.gui.GuiText;
import org.xnsc.jgraphic.render.Shader;

import java.util.List;
import java.util.Map;

import static org.lwjgl.opengl.GL30.*;

public class TextRenderer {
    private final TextShader shader = new TextShader();

    public void render(Map<FontType, Map<GuiText, Vector2f>> texts) {
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glDisable(GL_DEPTH_TEST);
        shader.start();
        for (FontType font : texts.keySet()) {
            glActiveTexture(GL_TEXTURE0);
            glBindTexture(GL_TEXTURE_2D, font.getTextureAtlas());
            for (Map.Entry<GuiText, Vector2f> entry : texts.get(font).entrySet()) {
                renderText(entry.getKey(), entry.getValue());
            }
        }
        Shader.stop();
        glDisable(GL_BLEND);
        glEnable(GL_DEPTH_TEST);
    }

    public void clean() {
        shader.clean();
    }

    private void renderText(GuiText text, Vector2f translation) {
        glBindVertexArray(text.getModel().getVAO());
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);
        shader.loadTranslation(new Vector2f(translation).add(text.getPosition()));
        shader.loadColor(text.getColor());
        glDrawArrays(GL_TRIANGLES, 0, text.getModel().getVertexCount());
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glBindVertexArray(0);
    }
}
