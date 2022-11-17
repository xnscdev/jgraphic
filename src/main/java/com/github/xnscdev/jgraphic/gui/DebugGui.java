package com.github.xnscdev.jgraphic.gui;

import com.github.xnscdev.jgraphic.util.DisplayManager;
import com.github.xnscdev.jgraphic.world.World;
import org.joml.Vector2f;
import org.joml.Vector4f;

import java.text.NumberFormat;

public class DebugGui extends Gui {
    private final NumberFormat floatFormatter = NumberFormat.getNumberInstance();
    private final GuiText fpsText;
    private final GuiText cameraText;
    private final World world;
    private long lastUpdate;

    public DebugGui(World world) {
        super(GuiManager.RECT_MODEL, new Vector2f(), new Vector2f(DisplayManager.getWidth(), 50), new Vector4f(0.6f, 0.6f, 0.6f, 1));
        this.world = world;
        fpsText = new GuiText("FPS: 0", 24, "Sans", new Vector2f(12, 12), 100, false);
        cameraText = new GuiText("Position: (0.00 0.00 0.00)", 24, "Sans", new Vector2f(112, 12), 9999, false);
        addChild(fpsText);
        addChild(cameraText);
        floatFormatter.setMinimumFractionDigits(2);
        floatFormatter.setMaximumFractionDigits(2);
    }

    @Override
    public void tick(double delta) {
        long time = System.currentTimeMillis();
        if (time - lastUpdate >= 500) {
            fpsText.setText("FPS: " + (int) (1 / delta));
            lastUpdate = time;
        }
        cameraText.setText("Position: " + world.getCamera().getPrimaryPosition().toString(floatFormatter));
    }
}
