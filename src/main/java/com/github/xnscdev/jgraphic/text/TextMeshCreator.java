package com.github.xnscdev.jgraphic.text;

import com.github.xnscdev.jgraphic.gui.GuiText;
import com.github.xnscdev.jgraphic.util.DisplayManager;
import org.joml.Vector2f;

import java.util.ArrayList;
import java.util.List;

public class TextMeshCreator {
    protected static final int SPACE_ASCII = ' ';
    protected static final int NEWLINE_ASCII = '\n';
    private final FontMetaFile metadata;

    protected TextMeshCreator(String name) {
        metadata = new FontMetaFile(name);
    }

    protected TextModel createModel(GuiText text) {
        List<TextLine> lines = createStructure(text);
        return createVertices(text, lines);
    }

    private List<TextLine> createStructure(GuiText text) {
        char[] chars = text.getText().toCharArray();
        List<TextLine> lines = new ArrayList<>();
        TextLine line = new TextLine(metadata.getSpaceWidth(), text.getFontSize(), text.getLineMaxSize());
        TextWord word = new TextWord(text.getFontSize());
        for (char c : chars) {
            if ((int) c == SPACE_ASCII) {
                boolean added = line.addWord(word);
                if (!added) {
                    lines.add(line);
                    line = new TextLine(metadata.getSpaceWidth(), text.getFontSize(), text.getLineMaxSize());
                    line.addWord(word);
                }
                word = new TextWord(text.getFontSize());
                continue;
            }
            TextChar character = metadata.getCharacter(c);
            if (character != null)
                word.addCharacter(character);
        }
        boolean added = line.addWord(word);
        if (!added) {
            lines.add(line);
            line = new TextLine(metadata.getSpaceWidth(), text.getFontSize(), text.getLineMaxSize());
            line.addWord(word);
        }
        lines.add(line);
        return lines;
    }

    private TextModel createVertices(GuiText text, List<TextLine> lines) {
        text.setLineCount(lines.size());
        double x = 0;
        double y = 0;
        double maxX = 0;
        List<Float> vertices = new ArrayList<>();
        List<Float> textures = new ArrayList<>();
        for (TextLine line : lines) {
            if (text.isCentered())
                x = (line.getMaxLength() - line.getLength()) / 2;
            for (TextWord word : line.getWords()) {
                for (TextChar c : word.getCharacters()) {
                    addCharVertices(x, y, c, text.getFontSize(), vertices);
                    addTextures(textures, c.getX(), c.getY(), c.getXFinal(), c.getYFinal());
                    x += c.getAdvance() * text.getFontSize();
                }
                x += metadata.getSpaceWidth() * text.getFontSize();
            }
            if (x > maxX)
                maxX = x;
            x = 0;
            y += text.getFontSize() / DisplayManager.getHeight();
        }
        return new TextModel(toArray(vertices), toArray(textures), new Vector2f((float) maxX * DisplayManager.getWidth(), (float) y * DisplayManager.getHeight()));
    }

    private void addCharVertices(double x, double y, TextChar c, double fontSize, List<Float> vertices) {
        x += c.getXOffset() * fontSize;
        y += c.getYOffset() * fontSize;
        double maxX = x + c.getWidth() * fontSize;
        double maxY = y + c.getHeight() * fontSize;
        double realX = 2 * x - 1;
        double realY = -2 * y + 1;
        double realMaxX = 2 * maxX - 1;
        double realMaxY = -2 * maxY + 1;
        addVertices(vertices, realX, realY, realMaxX, realMaxY);
    }

    private void addVertices(List<Float> vertices, double x, double y, double maxX, double maxY) {
        addQuadData(vertices, (float) x, (float) y, (float) maxX, (float) maxY);
    }

    private void addTextures(List<Float> textures, double x, double y, double maxX, double maxY) {
        addQuadData(textures, (float) x, (float) y, (float) maxX, (float) maxY);
    }

    private void addQuadData(List<Float> array, float x, float y, float maxX, float maxY) {
        array.add(x);
        array.add(y);
        array.add(x);
        array.add(maxY);
        array.add(maxX);
        array.add(maxY);
        array.add(maxX);
        array.add(maxY);
        array.add(maxX);
        array.add(y);
        array.add(x);
        array.add(y);
    }

    private float[] toArray(List<Float> array) {
        float[] result = new float[array.size()];
        for (int i = 0; i < result.length; i++) {
            result[i] = array.get(i);
        }
        return result;
    }
}
