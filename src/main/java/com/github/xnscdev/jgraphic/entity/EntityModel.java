package com.github.xnscdev.jgraphic.entity;

import com.github.xnscdev.jgraphic.model.*;
import com.github.xnscdev.jgraphic.util.AssetLoader;

import java.util.ArrayList;
import java.util.List;

public class EntityModel {
    private final List<ModelMesh> meshes = new ArrayList<>();
    private final List<ModelMaterial> materials;

    public EntityModel(String model) {
        AssetModel asset = AssetLoader.loadAsset(model);
        materials = asset.getMaterials();
        for (ModelData data : asset.getMeshes()) {
            ModelMesh mesh;
            if (data.material() != null && data.material().getTexturePath() != null)
                mesh = new TexturedMesh(data, data.material().getTexturePath());
            else
                mesh = new ModelMesh(data);
            meshes.add(mesh);
        }
    }

    public List<ModelMesh> getMeshes() {
        return meshes;
    }

    public List<ModelMaterial> getMaterials() {
        return materials;
    }
}
