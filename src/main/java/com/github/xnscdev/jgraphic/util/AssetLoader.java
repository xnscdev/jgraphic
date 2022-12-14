package com.github.xnscdev.jgraphic.util;

import com.github.xnscdev.jgraphic.model.AssetModel;
import org.lwjgl.assimp.AIFile;
import org.lwjgl.assimp.AIFileIO;
import org.lwjgl.assimp.AIScene;

import java.nio.ByteBuffer;

import static org.lwjgl.assimp.Assimp.*;
import static org.lwjgl.system.MemoryUtil.*;

/**
 * Helper class for loading assets within JAR resources using Assimp.
 * @author XNSC
 */
public class AssetLoader {
    private static final AIFileIO fileIO = AIFileIO.create()
            .OpenProc((pFileIO, fileName, openMode) -> {
                String name = memUTF8(fileName);
                ByteBuffer data = ObjectManager.storeByteBuffer(name);
                return AIFile.create()
                        .ReadProc((pFile, pBuffer, size, count) -> {
                            long max = Math.min(data.remaining(), size * count);
                            memCopy(memAddress(data) + data.position(), pBuffer, max);
                            return max;
                        })
                        .SeekProc((pFile, offset, origin) -> {
                            if (origin == aiOrigin_CUR)
                                data.position(data.position() + (int) offset);
                            else if (origin == aiOrigin_SET)
                                data.position((int) offset);
                            else if (origin == aiOrigin_END)
                                data.position(data.limit() + (int) offset);
                            return 0;
                        })
                        .FileSizeProc(pFile -> data.limit())
                        .address();
            })
            .CloseProc((pFileIO, pFile) -> {
                AIFile file = AIFile.create(pFile);
                file.ReadProc().free();
                file.SeekProc().free();
                file.FileSizeProc().free();
            });

    /**
     * Frees memory used by the Assimp IO structures.
     */
    public static void clean() {
        fileIO.OpenProc().free();
        fileIO.CloseProc().free();
    }

    /**
     * Loads a model using Assimp. The model files should be in the appropriate directory.
     * @param name name of the model
     * @return the loaded model
     * @throws RuntimeException Assimp encountered an error importing the model
     */
    public static AssetModel loadAsset(String name) {
        AIScene scene = aiImportFileEx("/models/" + name, aiProcess_JoinIdenticalVertices | aiProcess_Triangulate, fileIO);
        if (scene == null)
            throw new RuntimeException(aiGetErrorString());
        return new AssetModel(scene);
    }
}
