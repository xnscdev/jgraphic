package com.github.xnscdev.jgraphic.entity;

import com.github.xnscdev.jgraphic.model.AssetModel;
import com.github.xnscdev.jgraphic.model.ModelData;
import com.github.xnscdev.jgraphic.model.ModelMaterial;
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

    public EntityModel(String model, String texture) {
        AssetModel asset = AssetLoader.loadAsset(model);
        ModelMaterial material = new ModelMaterial(texture);
        for (ModelData data : asset.getMeshes()) {
            EntityMesh mesh = new EntityMesh(data);
            mesh.setMaterial(material);
            meshes.add(mesh);
        }
    }

    public List<EntityMesh> getMeshes() {
        return meshes;
    }
}
