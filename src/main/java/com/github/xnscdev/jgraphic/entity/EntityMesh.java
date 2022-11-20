package com.github.xnscdev.jgraphic.entity;

import com.github.xnscdev.jgraphic.model.ModelData;
import com.github.xnscdev.jgraphic.model.ModelMaterial;
import com.github.xnscdev.jgraphic.model.ModelMesh;
import com.github.xnscdev.jgraphic.util.ObjectManager;

import static org.lwjgl.opengl.GL13.*;

/**
 * Represents a single mesh in an entity model with its material. Entity models may have multiple meshes and materials.
 * @author XNSC
 */
public class EntityMesh extends ModelMesh {
    private static final int NO_TEXTURE = -1;
    private ModelMaterial material;
    private int texture = NO_TEXTURE;

    public EntityMesh(ModelData data) {
        super(data);
        if (data.material() != null)
            setMaterial(data.material());
    }

    public ModelMaterial getMaterial() {
        return material;
    }

    public void setMaterial(ModelMaterial material) {
        this.material = material;
        if (material.getTexturePath() != null)
            texture = ObjectManager.createTexture(material.getTexturePath());
    }

    public int getTexture() {
        return texture;
    }

    public boolean hasTexture() {
        return texture != NO_TEXTURE;
    }

    @Override
    public void preRender() {
        if (texture != NO_TEXTURE) {
            glActiveTexture(GL_TEXTURE0);
            glBindTexture(GL_TEXTURE_2D, texture);
        }
    }
}
