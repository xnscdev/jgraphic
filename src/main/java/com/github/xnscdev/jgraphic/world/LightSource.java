package com.github.xnscdev.jgraphic.world;

import org.joml.Vector3f;

/**
 * A point light source with attenuation and color.
 * @author XNSC
 */
public class LightSource {
    private Vector3f position;
    private Vector3f color;
    private Vector3f attenuation;

    /**
     * Creates a new light source.
     * @param position origin of the light
     * @param color light color
     * @param attenuation light attenuation vector: {@code (constant, linear, quadratic)}
     */
    public LightSource(Vector3f position, Vector3f color, Vector3f attenuation) {
        this.position = position;
        this.color = color;
        this.attenuation = attenuation;
    }

    /**
     * Creates a new light source with infinite attenuation.
     * @param position origin of the light
     * @param color light color
     */
    public LightSource(Vector3f position, Vector3f color) {
        this(position, color, new Vector3f(1, 0, 0));
    }

    public Vector3f getPosition() {
        return position;
    }

    public void setPosition(Vector3f position) {
        this.position = position;
    }

    public Vector3f getColor() {
        return color;
    }

    public void setColor(Vector3f color) {
        this.color = color;
    }

    public Vector3f getAttenuation() {
        return attenuation;
    }

    public void setAttenuation(Vector3f attenuation) {
        this.attenuation = attenuation;
    }
}
