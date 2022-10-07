package org.xnsc.jgraphic.text;

import org.xnsc.jgraphic.util.DisplayManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class FontMetaFile {
    private static final int PAD_TOP = 0;
    private static final int PAD_LEFT = 1;
    private static final int PAD_BOTTOM = 2;
    private static final int PAD_RIGHT = 3;
    private static final int DESIRED_PADDING = 3;
    private static final String SPLITTER = " ";
    private static final String NUMBER_SEPARATOR = ",";
    private final Map<Integer, TextChar> metadata = new HashMap<>();
    private final Map<String, String> values = new HashMap<>();
    private final BufferedReader reader;
    private final double aspectRatio;
    private double verticalPerPixelSize;
    private double horizontalPerPixelSize;
    private double spaceWidth;
    private int[] padding;
    private int paddingWidth;
    private int paddingHeight;

    protected FontMetaFile(String name) {
        aspectRatio = (double) DisplayManager.getWidth() / (double) DisplayManager.getHeight();
        reader = new BufferedReader(new InputStreamReader(Objects.requireNonNull(this.getClass().getResourceAsStream("/fonts/" + name + ".fnt"))));
        loadPaddingData();
        loadLineSizes();
        int width = getVariableValue("scaleW");
        loadCharacterData(width);
        close();
    }

    protected double getSpaceWidth() {
        return spaceWidth;
    }

    protected TextChar getCharacter(int id) {
        return metadata.get(id);
    }

    private void loadPaddingData() {
        nextLine();
        padding = getVariableValues("padding");
        paddingWidth = padding[PAD_LEFT] + padding[PAD_RIGHT];
        paddingHeight = padding[PAD_TOP] + padding[PAD_BOTTOM];
    }

    private void loadLineSizes() {
        nextLine();
        int pixels = getVariableValue("lineHeight") - paddingHeight;
        verticalPerPixelSize = TextMeshCreator.LINE_HEIGHT / (double) pixels;
        horizontalPerPixelSize = verticalPerPixelSize / aspectRatio;
    }

    private void loadCharacterData(int width) {
        nextLine();
        nextLine();
        while (nextLine()) {
            TextChar c = loadCharacter(width);
            if (c != null)
                metadata.put(c.getID(), c);
        }
    }

    private TextChar loadCharacter(int size) {
        int id = getVariableValue("id");
        if (id == TextMeshCreator.SPACE_ASCII || id == TextMeshCreator.NEWLINE_ASCII) {
            spaceWidth = (getVariableValue("xadvance") - paddingWidth) * horizontalPerPixelSize;
            return null;
        }
        double x = ((double) getVariableValue("x") + padding[PAD_LEFT] - DESIRED_PADDING) / size;
        double y = ((double) getVariableValue("y") + padding[PAD_TOP] - DESIRED_PADDING) / size;
        int width = getVariableValue("width") - paddingWidth + 2 * DESIRED_PADDING;
        int height = getVariableValue("height") - paddingHeight + 2 * DESIRED_PADDING;
        double quadWidth = width * horizontalPerPixelSize;
        double quadHeight = height * verticalPerPixelSize;
        double xTexture = (double) width / size;
        double yTexture = (double) height / size;
        double xOffset = (getVariableValue("xoffset") + padding[PAD_LEFT] - DESIRED_PADDING) * horizontalPerPixelSize;
        double yOffset = (getVariableValue("yoffset") + padding[PAD_TOP] - DESIRED_PADDING) * verticalPerPixelSize;
        double advance = (getVariableValue("xadvance") - paddingWidth) * horizontalPerPixelSize;
        return new TextChar(id, x, y, xTexture, yTexture, xOffset, yOffset, quadWidth, quadHeight, advance);
    }

    private boolean nextLine() {
        values.clear();
        String line;
        try {
            line = reader.readLine();
        }
        catch (IOException e) {
            return false;
        }
        if (line == null || line.startsWith("kerning"))
            return false;
        for (String part : line.split(SPLITTER)) {
            String[] pairs = part.split("=");
            if (pairs.length == 2)
                values.put(pairs[0], pairs[1]);
        }
        return true;
    }

    private int getVariableValue(String var) {
        return Integer.parseInt(values.get(var));
    }

    private int[] getVariableValues(String var) {
        String[] numbers = values.get(var).split(NUMBER_SEPARATOR);
        int[] values = new int[numbers.length];
        for (int i = 0; i < values.length; i++) {
            values[i] = Integer.parseInt(numbers[i]);
        }
        return values;
    }

    private void close() {
        try {
            reader.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
