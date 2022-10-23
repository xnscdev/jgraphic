package com.github.xnscdev.jgraphic.util;

import com.github.xnscdev.jgraphic.model.AssetModel;
import com.github.xnscdev.jgraphic.model.ModelData;
import org.lwjgl.assimp.AIFile;
import org.lwjgl.assimp.AIFileIO;
import org.lwjgl.assimp.AIScene;

import java.nio.ByteBuffer;

import static org.lwjgl.assimp.Assimp.*;
import static org.lwjgl.system.MemoryUtil.*;

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

    public static void clean() {
        fileIO.OpenProc().free();
        fileIO.CloseProc().free();
    }

    public static AssetModel loadAsset(String name) {
        AIScene scene = aiImportFileEx("/models/" + name, aiProcess_JoinIdenticalVertices | aiProcess_Triangulate, fileIO);
        if (scene == null)
            throw new RuntimeException(aiGetErrorString());
        return new AssetModel(scene);
    }
}
