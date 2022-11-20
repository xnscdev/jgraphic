package com.github.xnscdev.jgraphic.terrain;

import com.github.xnscdev.jgraphic.model.ModelData;
import com.github.xnscdev.jgraphic.util.MathUtils;
import com.github.xnscdev.jgraphic.world.World;
import de.articdive.jnoise.generators.noise_parameters.fade_functions.FadeFunction;
import de.articdive.jnoise.generators.noise_parameters.interpolation.Interpolation;
import de.articdive.jnoise.pipeline.JNoise;
import org.joml.Vector2f;
import org.joml.Vector3f;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;
import java.util.Random;

/**
 * Represents a single instance of terrain. This terrain implementation supports height maps and texture blend maps.
 * @author XNSC
 */
public class TerrainPiece {
    private static final float MAX_PIXEL_COLOR = 256 * 256 * 256;
    private static final String UNUSED_TEXTURE = "black";
    private final float x;
    private final float z;
    private final float size;
    private final int vertexCount;
    private final String background;
    private String red;
    private String green;
    private String blue;
    private String blendMap;
    private float maxHeight;
    private int hx;
    private int hz;
    private BufferedImage heightMap;
    private float[][] heights;
    private TerrainMesh model;

    /**
     * Creates a new square-shaped, textured, flat terrain piece.
     * @param x center X coordinate in world space
     * @param z center Z coordinate in world space
     * @param size side length of terrain
     * @param vertexCount number of points along each axis to use for height maps
     * @param background name of terrain texture
     */
    public TerrainPiece(int x, int z, float size, int vertexCount, String background) {
        this.x = x - size / 2;
        this.z = z - size / 2;
        this.size = size;
        this.vertexCount = vertexCount;
        this.background = background;
        this.red = this.green = this.blue = this.blendMap = UNUSED_TEXTURE;
    }

    public TerrainMesh getModel() {
        return model;
    }

    public float getX() {
        return x;
    }

    public float getZ() {
        return z;
    }

    public float getSize() {
        return size;
    }

    public int getVertexCount() {
        return vertexCount;
    }

    /**
     * Calculates the height of the terrain at the specified world coordinates. The terrain piece must contain
     * the specified coordinates.
     * @param worldX X coordinate in world space
     * @param worldZ Z coordinate in world space
     * @return the terrain height
     */
    public float getTerrainHeight(float worldX, float worldZ) {
        float terrainX = worldX - x;
        float terrainZ = worldZ - z;
        float tileSize = size / ((float) heights.length - 1);
        int gridX = (int) (terrainX / tileSize);
        int gridZ = (int) (terrainZ / tileSize);
        if (gridX < 0 || gridX >= heights.length - 1 || gridZ < 0 || gridZ >= heights.length - 1)
            return World.VOID;
        float rx = terrainX % tileSize / tileSize;
        float rz = terrainX % tileSize / tileSize;
        if (rx <= 1 - rz)
            return MathUtils.barycentric(new Vector3f(0, heights[gridX][gridZ], 0), new Vector3f(1, heights[gridX + 1][gridZ], 0), new Vector3f(0, heights[gridX][gridZ + 1], 1), new Vector2f(rx, rz));
        else
            return MathUtils.barycentric(new Vector3f(1, heights[gridX + 1][gridZ], 0), new Vector3f(1, heights[gridX + 1][gridZ + 1], 1), new Vector3f(0, heights[gridX][gridZ + 1], 1), new Vector2f(rx, rz));
    }

    /**
     * Sets a height map for the terrain from a subsection of a height map image. The image file should be in
     * the appropriate directory.
     * @param hx first X coordinate of image
     * @param hz first Z coordinate of image
     * @param maxHeight maximum height in world space
     * @param path name of height map file
     */
    public void setHeightMap(int hx, int hz, int maxHeight, String path) {
        this.hx = hx;
        this.hz = hz;
        this.maxHeight = maxHeight;
        try {
            heightMap = ImageIO.read(Objects.requireNonNull(this.getClass().getResourceAsStream("/heightmaps/" + path + ".png")));
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Generates a random height map with a random seed using Perlin noise.
     * @param maxHeight maximum height in world space
     */
    public void setRandomHeightMap(int maxHeight) {
        setRandomHeightMap(maxHeight, new Random().nextLong());
    }

    /**
     * Generates a random height map with a set seed using Perlin noise.
     * @param maxHeight maximum height in world space
     * @param seed seed to use for the random number generator
     */
    public void setRandomHeightMap(int maxHeight, long seed) {
        this.maxHeight = maxHeight;
        heightMap = new BufferedImage(vertexCount, vertexCount, BufferedImage.TYPE_INT_ARGB);
        JNoise gen = JNoise.newBuilder().perlin(seed, Interpolation.LINEAR, FadeFunction.IMPROVED_PERLIN_NOISE)
                .scale(32.0 / vertexCount)
                .addModifier(v -> (v + 1) / 2)
                .clamp(0, 1)
                .build();
        for (int x = 0; x < vertexCount; x++) {
            for (int y = 0; y < vertexCount; y++) {
                double noise = gen.evaluateNoise(x, y);
                int rgb = (int) (noise * 255) * 0x010101;
                heightMap.setRGB(x, y, rgb);
            }
        }
    }

    /**
     * Use a blend map when rendering this terrain.
     * @param red name of the texture to use for red in the blend map
     * @param green name of the texture to use for green in the blend map
     * @param blue name of the texture to use for blue in the blend map
     * @param blendMap name of the blend map texture
     */
    public void useBlendMap(String red, String green, String blue, String blendMap) {
        this.red = red;
        this.green = green;
        this.blue = blue;
        this.blendMap = blendMap;
    }

    /**
     * Builds the terrain mesh. This method should be called before terrain is added to the world and after
     * any height maps are added.
     * @see TerrainPiece#setHeightMap(int, int, int, String)
     * @see TerrainPiece#setRandomHeightMap(int)
     * @see TerrainPiece#setRandomHeightMap(int, long)
     */
    public void build() {
        int count = vertexCount * vertexCount;
        heights = new float[vertexCount][vertexCount];
        float[] vertices = new float[count * 3];
        float[] textures = new float[count * 2];
        float[] normals = new float[count * 3];
        int[] indices = new int[6 * (vertexCount - 1) * (vertexCount - 1)];
        int index = 0;
        for (int i = 0; i < vertexCount; i++) {
            for (int j = 0; j < vertexCount; j++) {
                vertices[index * 3] = (float) j / ((float) vertexCount - 1) * size;
                float height = getHeight(j + hz, i + hx, heightMap);
                vertices[index * 3 + 1] = height;
                heights[j][i] = height;
                vertices[index * 3 + 2] = (float) i / ((float) vertexCount - 1) * size;
                textures[index * 2] = (float) j / ((float) vertexCount - 1);
                textures[index * 2 + 1] = (float) i / ((float) vertexCount - 1);
                Vector3f normal = getNormal(j + hz, i + hx, heightMap);
                normals[index * 3] = normal.x;
                normals[index * 3 + 1] = normal.y;
                normals[index * 3 + 2] = normal.z;
                index++;
            }
        }
        index = 0;
        for (int gz = 0; gz < vertexCount - 1; gz++) {
            for (int gx = 0; gx < vertexCount - 1; gx++) {
                int topLeft = gz * vertexCount + gx;
                int topRight = topLeft + 1;
                int bottomLeft = (gz + 1) * vertexCount + gx;
                int bottomRight = bottomLeft + 1;
                indices[index++] = topLeft;
                indices[index++] = bottomLeft;
                indices[index++] = topRight;
                indices[index++] = topRight;
                indices[index++] = bottomLeft;
                indices[index++] = bottomRight;
            }
        }
        ModelData data = new ModelData(vertices, textures, normals, indices);
        model = new TerrainMesh(data, background, red, green, blue, blendMap);
        heightMap = null;
    }

    private float getHeight(int x, int z, BufferedImage image) {
        if (image == null || x < 0 || x >= image.getHeight() || z < 0 || z >= image.getHeight())
            return 0;
        float height = image.getRGB(x, z);
        height += MAX_PIXEL_COLOR / 2f;
        height /= MAX_PIXEL_COLOR / 2f;
        height *= maxHeight;
        return height;
    }

    private Vector3f getNormal(int x, int z, BufferedImage image) {
        float left = getHeight(x - 1, z, image);
        float right = getHeight(x + 1, z, image);
        float down = getHeight(x, z - 1, image);
        float up = getHeight(x, z + 1, image);
        Vector3f normal = new Vector3f(left - right, 2, down - up);
        return normal.normalize();
    }
}
