package org.xnsc.jgraphic.text;

import java.util.ArrayList;
import java.util.List;

public class TextWord {
    private final List<TextChar> characters = new ArrayList<>();
    private final double fontSize;
    private double width = 0;

    protected TextWord(double fontSize) {
        this.fontSize = fontSize;
    }

    protected void addCharacter(TextChar character) {
        characters.add(character);
        width += character.getAdvance() * fontSize;
    }

    protected List<TextChar> getCharacters() {
        return characters;
    }

    protected double getWidth() {
        return width;
    }
}
