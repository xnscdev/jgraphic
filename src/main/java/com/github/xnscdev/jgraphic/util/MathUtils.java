package com.github.xnscdev.jgraphic.util;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

/**
 * Utility class for linear algebraic and geometric operations.
 * @author XNSC
 */
public final class MathUtils {
    private MathUtils() {
    }

    /**
     * Creates a transformation matrix for a three-dimensional object.
     * @param translation translation vector
     * @param rx roll
     * @param ry yaw
     * @param rz pitch
     * @param scale scale factor
     * @return the transformation matrix
     */
    public static Matrix4f transformMatrix(Vector3f translation, float rx, float ry, float rz, float scale) {
        return new Matrix4f()
                .translate(translation)
                .rotate((float) Math.toRadians(rx), new Vector3f(1, 0, 0))
                .rotate((float) Math.toRadians(ry), new Vector3f(0, 1, 0))
                .rotate((float) Math.toRadians(rz), new Vector3f(0, 0, 1))
                .scale(scale);
    }

    /**
     * Creates a transformation matrix for a two-dimensional object.
     * @param translation translation vector
     * @param scale scale vector
     * @return the transformation matrix
     */
    public static Matrix4f transformMatrix(Vector2f translation, Vector2f scale) {
        return new Matrix4f()
                .translate(translation.x, translation.y, 0)
                .scale(scale.x, scale.y, 1);
    }

    /**
     * Creates a projection matrix.
     * @param fov field of view
     * @param nearPlane near plane
     * @param farPlane far plane
     * @return the projection matrix
     */
    public static Matrix4f projectionMatrix(float fov, float nearPlane, float farPlane) {
        float aspectRatio = (float) DisplayManager.getWidth() / (float) DisplayManager.getHeight();
        float yScale = (1f / (float) Math.tan(Math.toRadians(fov / 2f))) * aspectRatio;
        float xScale = yScale / aspectRatio;
        float frustum = farPlane - nearPlane;
        return new Matrix4f()
                .m00(xScale)
                .m11(yScale)
                .m22(-(farPlane + nearPlane) / frustum)
                .m23(-1)
                .m32(-(2 * nearPlane * farPlane) / frustum)
                .m33(0);
    }

    /**
     * Creates a view matrix for a camera in the world.
     * @param pos position of the camera
     * @param pitch pitch
     * @param yaw yaw
     * @param roll roll
     * @return the view matrix
     */
    public static Matrix4f viewMatrix(Vector3f pos, float pitch, float yaw, float roll) {
        Vector3f translation = new Vector3f();
        return new Matrix4f()
                .rotate((float) Math.toRadians(pitch), new Vector3f(1, 0, 0))
                .rotate((float) Math.toRadians(yaw), new Vector3f(0, 1, 0))
                .rotate((float) Math.toRadians(roll), new Vector3f(0, 0, 1))
                .translate(pos.negate(translation));
    }

    /**
     * Calculates the height of a point within a triangle using barycentric interpolation.
     * @param p1 the first vertex of the triangle
     * @param p2 the second vertex of the triangle
     * @param p3 the third vertex of the triangle
     * @param pos the two-dimensional position of the point in world space
     * @return the interpolated height
     */
    public static float barycentric(Vector3f p1, Vector3f p2, Vector3f p3, Vector2f pos) {
        float det = (p2.z - p3.z) * (p1.x - p3.x) + (p3.x - p2.x) * (p1.z - p3.z);
        float l1 = ((p2.z - p3.z) * (pos.x - p3.x) + (p3.x - p2.x) * (pos.y - p3.z)) / det;
        float l2 = ((p3.z - p1.z) * (pos.x - p3.x) + (p1.x - p3.x) * (pos.y - p3.z)) / det;
        float l3 = 1.0f - l1 - l2;
        return l1 * p1.y + l2 * p2.y + l3 * p3.y;
    }
}
