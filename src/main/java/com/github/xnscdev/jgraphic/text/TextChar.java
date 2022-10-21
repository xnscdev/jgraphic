package com.github.xnscdev.jgraphic.text;

public class TextChar {
    private final int id;
    private final double x;
    private final double y;
    private final double xFinal;
    private final double yFinal;
    private final double xOffset;
    private final double yOffset;
    private final double width;
    private final double height;
    private final double advance;

    public TextChar(int id, double x, double y, double xTexture, double yTexture, double xOffset, double yOffset, double width, double height, double advance) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.xOffset = xOffset;
        this.yOffset = yOffset;
        this.width = width;
        this.height = height;
        this.advance = advance;
        xFinal = x + xTexture;
        yFinal = y + yTexture;
    }

    public int getID() {
        return id;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getXFinal() {
        return xFinal;
    }

    public double getYFinal() {
        return yFinal;
    }

    public double getXOffset() {
        return xOffset;
    }

    public double getYOffset() {
        return yOffset;
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }

    public double getAdvance() {
        return advance;
    }
}
