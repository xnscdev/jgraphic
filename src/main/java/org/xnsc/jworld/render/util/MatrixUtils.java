package org.xnsc.jworld.render.util;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.xnsc.jworld.render.DisplayManager;

public final class MatrixUtils {
    private MatrixUtils() {}

    public static Matrix4f transformMatrix(Vector3f translation, float rx, float ry, float rz, float scale) {
        return new Matrix4f()
                .translate(translation)
                .rotate((float) Math.toRadians(rx), new Vector3f(1, 0, 0))
                .rotate((float) Math.toRadians(ry), new Vector3f(0, 1, 0))
                .rotate((float) Math.toRadians(rz), new Vector3f(0, 0, 1))
                .scale(scale);
    }

    public static Matrix4f transformMatrix(Vector2f translation, Vector2f scale) {
        return new Matrix4f()
                .translate(translation.x, translation.y, 0)
                .scale(scale.x, scale.y, 1);
    }

    public static Matrix4f projectionMatrix(float fov, float nearPlane, float farPlane) {
        float aspectRatio = (float) DisplayManager.WIDTH / (float) DisplayManager.HEIGHT;
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

    public static Matrix4f viewMatrix(Vector3f pos, float pitch, float yaw, float roll) {
        Vector3f translation = new Vector3f();
        return new Matrix4f()
                .rotate((float) Math.toRadians(pitch), new Vector3f(1, 0, 0))
                .rotate((float) Math.toRadians(yaw), new Vector3f(0, 1, 0))
                .rotate((float) Math.toRadians(roll), new Vector3f(0, 0, 1))
                .translate(pos.negate(translation));
    }
}
