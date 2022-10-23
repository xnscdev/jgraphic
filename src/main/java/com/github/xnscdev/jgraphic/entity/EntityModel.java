package com.github.xnscdev.jgraphic.entity;

import com.github.xnscdev.jgraphic.model.*;
import com.github.xnscdev.jgraphic.util.AssetLoader;

import java.util.ArrayList;
import java.util.List;

public class EntityModel {
    private final List<EntityMesh> meshes = new ArrayList<>();
    private final List<ModelMaterial> materials;

    public EntityModel(String model) {
        AssetModel asset = AssetLoader.loadAsset(model);
        materials = asset.getMaterials();
        for (ModelData data : asset.getMeshes()) {
            meshes.add(new EntityMesh(data));
        }
    }

    public List<EntityMesh> getMeshes() {
        return meshes;
    }

    public List<ModelMaterial> getMaterials() {
        return materials;
    }
}
