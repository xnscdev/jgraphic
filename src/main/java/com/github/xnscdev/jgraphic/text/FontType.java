package com.github.xnscdev.jgraphic.text;

import com.github.xnscdev.jgraphic.gui.GuiText;
import com.github.xnscdev.jgraphic.util.ObjectManager;

public class FontType {
    private final int textureAtlas;
    private final TextMeshCreator loader;

    public FontType(String name) {
        this.textureAtlas = ObjectManager.createFontAtlas(name);
        this.loader = new TextMeshCreator(name);
    }

    public int getTextureAtlas() {
        return textureAtlas;
    }

    public TextModel loadText(GuiText text) {
        return loader.createModel(text);
    }
}
