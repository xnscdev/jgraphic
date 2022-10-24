package com.github.xnscdev.jgraphic.entity;

import com.github.xnscdev.jgraphic.model.*;
import com.github.xnscdev.jgraphic.util.AssetLoader;

import java.util.ArrayList;
import java.util.List;

public class EntityModel {
    private final List<EntityMesh> meshes = new ArrayList<>();

    public EntityModel(String model) {
        AssetModel asset = AssetLoader.loadAsset(model);
        for (ModelData data : asset.getMeshes()) {
            meshes.add(new EntityMesh(data));
        }
    }

    public List<EntityMesh> getMeshes() {
        return meshes;
    }
}
