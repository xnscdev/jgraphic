package com.github.xnscdev.jgraphic.entity;

import com.github.xnscdev.jgraphic.model.AssetModel;
import com.github.xnscdev.jgraphic.model.ModelData;
import com.github.xnscdev.jgraphic.model.ModelMaterial;
import com.github.xnscdev.jgraphic.util.AssetLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * All entities are rendered with an entity model, which is a collection of meshes and materials forming a single
 * object in the world. Multiple entities may use the same entity model instance.
 * @author XNSC
 */
public class EntityModel {
    private final List<EntityMesh> meshes = new ArrayList<>();

    /**
     * Loads an entity model from the class resources. All materials for the model are expected to be defined in
     * the model files. The model files should be in the appropriate directory.
     * @param model name of the model
     * @see AssetLoader#loadAsset(String)
     */
    public EntityModel(String model) {
        AssetModel asset = AssetLoader.loadAsset(model);
        for (ModelData data : asset.getMeshes()) {
            meshes.add(new EntityMesh(data));
        }
    }

    /**
     * Loads an entity model from the class resources with a fixed texture. The texture path supplied will be
     * applied to all meshes within the model. The model and texture files should be in the appropriate directory.
     * @param model name of the model
     * @param texture name of the texture
     * @see AssetLoader#loadAsset(String)
     */
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
