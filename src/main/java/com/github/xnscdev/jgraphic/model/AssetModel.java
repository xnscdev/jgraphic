package com.github.xnscdev.jgraphic.model;

import org.lwjgl.PointerBuffer;
import org.lwjgl.assimp.*;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.assimp.Assimp.aiReleaseImport;

public class AssetModel {
    private final AIScene scene;
    private final List<ModelData> meshes = new ArrayList<>();
    private final List<ModelMaterial> materials = new ArrayList<>();

    public AssetModel(AIScene scene) {
        this.scene = scene;
        int meshCount = scene.mNumMeshes();
        PointerBuffer meshesBuffer = scene.mMeshes();
        for (int i = 0; i < meshCount; i++) {
            meshes.add(getModelData(AIMesh.create(meshesBuffer.get(i))));
        }
        int materialCount = scene.mNumMaterials();
        PointerBuffer materialsBuffer = scene.mMaterials();
        for (int i = 0; i < materialCount; i++) {
            materials.add(new ModelMaterial(AIMaterial.create(materialsBuffer.get(i))));
        }
    }

    public void clean() {
        aiReleaseImport(scene);
        meshes.clear();
        materials.clear();
    }

    public AIScene getScene() {
        return scene;
    }

    public List<ModelData> getMeshes() {
        return meshes;
    }

    public List<ModelMaterial> getMaterials() {
        return materials;
    }

    private ModelData getModelData(AIMesh mesh) {
        int vertexCount = mesh.mNumVertices();
        AIVector3D.Buffer vertices = mesh.mVertices();
        float[] verticesArray = new float[vertexCount * 3];
        int index = 0;
        for (AIVector3D vertex : vertices.stream().toList()) {
            verticesArray[index++] = vertex.x();
            verticesArray[index++] = vertex.y();
            verticesArray[index++] = vertex.z();
        }

        AIVector3D.Buffer textures = mesh.mTextureCoords(0);
        if (textures == null)
            throw new IllegalArgumentException("Only meshes with texture coordinates are supported");
        float[] texturesArray = new float[vertexCount * 2];
        index = 0;
        for (AIVector3D texture : textures.stream().toList()) {
            texturesArray[index++] = texture.x();
            texturesArray[index++] = 1 - texture.y();
        }
        
        AIVector3D.Buffer normals = mesh.mNormals();
        if (normals == null)
            throw new IllegalArgumentException("Only meshes with normals are supported");
        float[] normalsArray = new float[vertexCount * 3];
        index = 0;
        for (AIVector3D normal : normals.stream().toList()) {
            normalsArray[index++] = normal.x();
            normalsArray[index++] = normal.y();
            normalsArray[index++] = normal.z();
        }

        int faceCount = mesh.mNumFaces();
        int[] indicesArray = new int[faceCount * 3];
        AIFace.Buffer facesBuffer = mesh.mFaces();
        for (int i = 0; i < faceCount; i++) {
            AIFace face = facesBuffer.get(i);
            if (face.mNumIndices() != 3)
                throw new IllegalStateException("Face does not have three sides");
            face.mIndices().get(indicesArray, i * 3, 3);
        }
        return new ModelData(verticesArray, texturesArray, normalsArray, indicesArray);
    }
}
