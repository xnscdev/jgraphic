package com.github.xnscdev.jgraphic.text;

import java.util.ArrayList;
import java.util.List;

public class TextLine {
    private final List<TextWord> words = new ArrayList<>();
    private final double maxLength;
    private final double spaceSize;
    private double length = 0;

    protected TextLine(double spaceWidth, double fontSize, double maxLength) {
        this.spaceSize = spaceWidth * fontSize;
        this.maxLength = maxLength;
    }

    protected boolean addWord(TextWord word) {
        double additional = word.getWidth();
        additional += words.isEmpty() ? 0 : spaceSize;
        if (length + additional <= maxLength) {
            words.add(word);
            length += additional;
            return true;
        }
        else
            return false;
    }

    protected double getMaxLength() {
        return maxLength;
    }

    protected double getLength() {
        return length;
    }

    protected List<TextWord> getWords() {
        return words;
    }
}
