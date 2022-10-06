package org.xnsc.jgraphic.world;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.xnsc.jgraphic.terrain.TerrainPiece;
import org.xnsc.jgraphic.util.DisplayManager;

public class MousePicker {
    private static final int RECURSION_COUNT = 200;
    private static final float RAY_RANGE = 600;
    private final Matrix4f projectionMatrix;
    private final Camera camera;
    private Matrix4f viewMatrix;
    private Vector3f ray;
    private Vector3f terrainPoint;

    public MousePicker(Camera camera) {
        this.camera = camera;
        this.projectionMatrix = World.PROJECTION;
        this.viewMatrix = camera.viewMatrix();
    }

    public Vector3f getRay() {
        return ray;
    }

    public Vector3f getTerrainPoint() {
        return terrainPoint;
    }

    public void tick(World world) {
        updateRay();
        if (intersectionInRange(0, RAY_RANGE, ray, world))
            terrainPoint = binarySearch(0, 0, RAY_RANGE, ray, world);
        else
            terrainPoint = null;
    }

    private void updateRay() {
        viewMatrix = camera.viewMatrix();
        Vector2f pos = DisplayManager.getMousePos();
        pos.x = 2f * pos.x / DisplayManager.getWidth() - 1;
        pos.y = -2f * pos.y / DisplayManager.getHeight() + 1;
        Vector4f clip = new Vector4f(pos, -1, 1);

        Matrix4f invertedProjection = new Matrix4f();
        projectionMatrix.invert(invertedProjection);
        invertedProjection.transform(clip);
        Vector4f eye = new Vector4f(clip.x, clip.y, -1, 0);

        Matrix4f invertedView = new Matrix4f();
        viewMatrix.invert(invertedView);
        invertedView.transform(eye);
        ray = new Vector3f(eye.x, eye.y, eye.z);
        ray.normalize();
    }

    private Vector3f getRayPoint(Vector3f ray, float distance) {
        Vector3f scaledRay = new Vector3f(ray.x * distance, ray.y * distance, ray.z * distance);
        return scaledRay.add(camera.getPosition());
    }

    private Vector3f binarySearch(int count, float start, float finish, Vector3f ray, World world) {
        float mid = start + (finish - start) / 2f;
        if (count >= RECURSION_COUNT) {
            Vector3f end = getRayPoint(ray, mid);
            TerrainPiece terrain = world.getTerrain(end.x, end.z);
            if (terrain != null)
                return end;
            else
                return null;
        }
        if (intersectionInRange(start, mid, ray, world))
            return binarySearch(count + 1, start, mid, ray, world);
        else
            return binarySearch(count + 1, mid, finish, ray, world);
    }

    private boolean intersectionInRange(float start, float finish, Vector3f ray, World world) {
        Vector3f startPoint = getRayPoint(ray, start);
        Vector3f finishPoint = getRayPoint(ray, finish);
        return !isUnderground(startPoint, world) && isUnderground(finishPoint, world);
    }

    private boolean isUnderground(Vector3f pos, World world) {
        TerrainPiece terrain = world.getTerrain(pos.x, pos.z);
        float height = terrain == null ? 0 : terrain.getTerrainHeight(pos.x, pos.z);
        return pos.y < height;
    }
}
