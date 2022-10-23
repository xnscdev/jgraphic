package com.github.xnscdev.jgraphic.model;

import com.github.xnscdev.jgraphic.util.ObjectManager;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.PointerBuffer;
import org.lwjgl.assimp.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.lwjgl.assimp.Assimp.aiReleaseImport;

public class AssetModel {
    private static final int MAX_WEIGHTS = 4;
    private static final int MAX_BONES = 150;
    private final AIScene scene;
    private final List<ModelData> meshes = new ArrayList<>();
    private final List<ModelMaterial> materials = new ArrayList<>();

    public AssetModel(AIScene scene) {
        this.scene = scene;
        int materialCount = scene.mNumMaterials();
        PointerBuffer materialsBuffer = scene.mMaterials();
        for (int i = 0; i < materialCount; i++) {
            materials.add(new ModelMaterial(AIMaterial.create(materialsBuffer.get(i))));
        }
        int meshCount = scene.mNumMeshes();
        PointerBuffer meshesBuffer = scene.mMeshes();
        List<ModelBone> bones = new ArrayList<>();
        for (int i = 0; i < meshCount; i++) {
            meshes.add(getModelData(AIMesh.create(meshesBuffer.get(i)), bones));
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

    private ModelData getModelData(AIMesh mesh, List<ModelBone> bones) {
        float[] vertices = processVertices(mesh);
        float[] textures = processTextures(mesh);
        float[] normals = processNormals(mesh);
        int[] indices = processIndices(mesh);
        AnimatedModelData animated = processBones(mesh, bones);
        AIAABB aabb = mesh.mAABB();
        Vector3f aabbMin = new Vector3f(aabb.mMin().x(), aabb.mMin().y(), aabb.mMin().z());
        Vector3f aabbMax = new Vector3f(aabb.mMax().x(), aabb.mMax().y(), aabb.mMax().z());
        int materialIndex = mesh.mMaterialIndex();
        ModelMaterial material = null;
        if (materialIndex >= 0 && materialIndex < materials.size())
            material = materials.get(materialIndex);
        return new ModelData(vertices, textures, normals, indices, animated, aabbMin, aabbMax, material);
    }

    private float[] processVertices(AIMesh mesh) {
        AIVector3D.Buffer vertices = mesh.mVertices();
        float[] verticesArray = new float[mesh.mNumVertices() * 3];
        int index = 0;
        for (AIVector3D vertex : vertices.stream().toList()) {
            verticesArray[index++] = vertex.x();
            verticesArray[index++] = vertex.y();
            verticesArray[index++] = vertex.z();
        }
        return verticesArray;
    }

    private float[] processTextures(AIMesh mesh) {
        AIVector3D.Buffer textures = mesh.mTextureCoords(0);
        float[] texturesArray = new float[mesh.mNumVertices() * 2];
        if (textures == null)
            return texturesArray;
        int index = 0;
        for (AIVector3D texture : textures.stream().toList()) {
            texturesArray[index++] = texture.x();
            texturesArray[index++] = 1 - texture.y();
        }
        return texturesArray;
    }

    private float[] processNormals(AIMesh mesh) {
        AIVector3D.Buffer normals = mesh.mNormals();
        if (normals == null)
            throw new IllegalArgumentException("Only meshes with normals are supported");
        float[] normalsArray = new float[mesh.mNumVertices() * 3];
        int index = 0;
        for (AIVector3D normal : normals.stream().toList()) {
            normalsArray[index++] = normal.x();
            normalsArray[index++] = normal.y();
            normalsArray[index++] = normal.z();
        }
        return normalsArray;
    }

    private int[] processIndices(AIMesh mesh) {
        int faceCount = mesh.mNumFaces();
        int[] indicesArray = new int[faceCount * 3];
        AIFace.Buffer facesBuffer = mesh.mFaces();
        for (int i = 0; i < faceCount; i++) {
            AIFace face = facesBuffer.get(i);
            if (face.mNumIndices() != 3)
                throw new IllegalStateException("Mesh has face with unsupported number of sides: " + face.mNumIndices());
            face.mIndices().get(indicesArray, i * 3, 3);
        }
        return indicesArray;
    }

    private AnimatedModelData processBones(AIMesh mesh, List<ModelBone> bones) {
        List<Integer> boneIDs = new ArrayList<>();
        List<Float> weights = new ArrayList<>();
        Map<Integer, List<ModelVertexWeight>> weightSet = new HashMap<>();
        int boneCount = mesh.mNumBones();
        PointerBuffer bonesBuffer = mesh.mBones();
        for (int i = 0; i < boneCount; i++) {
            AIBone aiBone = AIBone.create(bonesBuffer.get(i));
            int id = bones.size();
            ModelBone bone = new ModelBone(id, aiBone.mName().dataString(), convertMatrix(aiBone.mOffsetMatrix()));
            bones.add(bone);

            int weightCount = aiBone.mNumWeights();
            AIVertexWeight.Buffer weightsBuffer = aiBone.mWeights();
            for (int j = 0; j < weightCount; j++) {
                AIVertexWeight weight = weightsBuffer.get(j);
                ModelVertexWeight vw = new ModelVertexWeight(bone.id(), weight.mVertexId(), weight.mWeight());
                List<ModelVertexWeight> batch = weightSet.computeIfAbsent(vw.vertex(), k -> new ArrayList<>());
                batch.add(vw);
            }
        }

        int vertexCount = mesh.mNumVertices();
        for (int i = 0; i < vertexCount; i++) {
            List<ModelVertexWeight> batch = weightSet.get(i);
            int size = batch != null ? batch.size() : 0;
            for (int j = 0; j < MAX_WEIGHTS; j++) {
                if (j < size) {
                    ModelVertexWeight vw = batch.get(j);
                    weights.add(vw.weight());
                    boneIDs.add(vw.bone());
                }
                else {
                    weights.add(0f);
                    boneIDs.add(0);
                }
            }
        }
        int[] bonesArray = boneIDs.stream().mapToInt(Integer::intValue).toArray();
        float[] weightsArray = new float[weights.size()];
        for (int i = 0; i < weights.size(); i++) {
            weightsArray[i] = weights.get(i);
        }
        return new AnimatedModelData(bonesArray, weightsArray);
    }

    private static Matrix4f convertMatrix(AIMatrix4x4 matrix) {
        Matrix4f result = new Matrix4f();
        result.m00(matrix.a1());
        result.m10(matrix.a2());
        result.m20(matrix.a3());
        result.m30(matrix.a4());
        result.m01(matrix.b1());
        result.m11(matrix.b2());
        result.m21(matrix.b3());
        result.m31(matrix.b4());
        result.m02(matrix.c1());
        result.m12(matrix.c2());
        result.m22(matrix.c3());
        result.m32(matrix.c4());
        result.m03(matrix.d1());
        result.m13(matrix.d2());
        result.m23(matrix.d3());
        result.m33(matrix.d4());
        return result;
    }
}
