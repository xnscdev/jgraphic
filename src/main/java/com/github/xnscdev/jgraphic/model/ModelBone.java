package com.github.xnscdev.jgraphic.model;

import org.joml.Matrix4f;

public record ModelBone(int id, String name, Matrix4f offset) {
}
