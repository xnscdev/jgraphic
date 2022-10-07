package org.xnsc.jworld;

import org.joml.Random;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.xnsc.jgraphic.AppInstance;
import org.xnsc.jgraphic.entity.Entity;
import org.xnsc.jgraphic.entity.Player;
import org.xnsc.jgraphic.gui.SolidGui;
import org.xnsc.jgraphic.text.FontType;
import org.xnsc.jgraphic.gui.GuiText;
import org.xnsc.jgraphic.model.TexturedModel;
import org.xnsc.jgraphic.terrain.TerrainPiece;
import org.xnsc.jgraphic.text.Fonts;
import org.xnsc.jgraphic.world.Camera3PT;
import org.xnsc.jgraphic.world.LightSource;
import org.xnsc.jgraphic.world.World;

public class Main {
    public static void main(String[] args) {
        AppInstance.init("JWorld", 1280, 760);
        World world = new World();
        TexturedModel playerModel = new TexturedModel("human");
        Player player = new Player(playerModel, new Vector3f(0, 0, 0), Player.MOVEMENT_WASD);
        world.setCamera(new Camera3PT(player));
        world.getCamera().setPosition(new Vector3f(0, 20, 50));
        world.getCamera().setPitch(20);
        world.addLightSource(new LightSource(new Vector3f(2000, 4000, 2000), new Vector3f(1)));
        world.setAmbientThreshold(0.3f);
        world.setGravityAccel(40);
        world.setFog(0.003f, 1.5f);
        world.addEntity(player);

        TerrainPiece terrain = new TerrainPiece(0, 0, 1000, 256, "terrain");
        terrain.useBlendMap("mud", "flowers", "path", "blend_map");
        terrain.setHeightMap(0, 0, 10, "heightmap");
        world.addTerrain(terrain);

        Random random = new Random();
        TexturedModel treeModel = new TexturedModel("tree");
        TexturedModel fernModel = new TexturedModel("fern");
        fernModel.setRows(2);
        for (int i = 0; i < 1000; i++) {
            float x = random.nextFloat() * 2000 - 1000;
            float z = random.nextFloat() * 2000 - 1000;
            float scale = random.nextFloat() + 3;
            Entity tree = new Entity(treeModel, new Vector3f(x, terrain.getTerrainHeight(x, z), z), 0, 0, 0, scale);
            world.addEntity(tree);
        }
        for (int i = 0; i < 1500; i++) {
            float x = random.nextFloat() * 2000 - 1000;
            float z = random.nextFloat() * 2000 - 1000;
            float ry = random.nextFloat() * 180;
            int textureIndex = random.nextInt(4);
            Entity fern = new Entity(fernModel, new Vector3f(x, terrain.getTerrainHeight(x, z), z), 0, ry, 0, 1, textureIndex);
            world.addEntity(fern);
        }
        world.addDebugGui();

        GuiText text = new GuiText("Sample text", 1, Fonts.PLAY, new Vector2f(20, 20), 360, false);
        SolidGui gui = new SolidGui(new Vector2f(100, 100), new Vector2f(400, 100), new Vector3f(0, 1, 0));
        gui.addText(text);
        world.addGui(gui);
        AppInstance.launch(world);
    }
}