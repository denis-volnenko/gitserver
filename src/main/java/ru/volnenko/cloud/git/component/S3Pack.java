package ru.volnenko.cloud.git.component;

import lombok.Getter;
import lombok.NonNull;
import org.eclipse.jgit.internal.storage.dfs.DfsObjDatabase;
import org.eclipse.jgit.internal.storage.dfs.DfsPackDescription;
import org.eclipse.jgit.internal.storage.dfs.DfsRepositoryDescription;
import org.eclipse.jgit.internal.storage.pack.PackExt;

public final class S3Pack extends DfsPackDescription {

    @Getter
    @NonNull
    private final byte[][] fileMap = new byte[PackExt.values().length][];

    public S3Pack(
            @NonNull final String name,
            @NonNull final DfsRepositoryDescription repoDesc,
            @NonNull DfsObjDatabase.PackSource packSource
    ) {
        super(repoDesc, name, packSource);
    }

    public void put(@NonNull final PackExt ext, @NonNull final byte[] data) {
        this.fileMap[ext.getPosition()] = data;
    }

    @NonNull
    public byte[] get(@NonNull final PackExt ext) {
        return this.fileMap[ext.getPosition()];
    }

}
