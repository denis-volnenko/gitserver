package ru.volnenko.cloud.git.component;

import io.minio.MinioClient;
import lombok.NonNull;
import org.eclipse.jgit.internal.storage.dfs.*;
import org.eclipse.jgit.internal.storage.pack.PackExt;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collection;
import java.util.List;

public final class S3ObjectDatabase extends DfsObjDatabase {

    @NonNull
    private final MinioClient minioClient;

    public S3ObjectDatabase(
            @NonNull final DfsRepository repository,
            @NonNull final DfsReaderOptions options,
            @NonNull final MinioClient minioClient
    ) {
        super(repository, options);
        this.minioClient = minioClient;
    }

    @Override
    protected DfsPackDescription newPack(PackSource source) throws IOException {
        return null;
    }

    @Override
    protected void commitPackImpl(Collection<DfsPackDescription> desc, Collection<DfsPackDescription> replaces) throws IOException {

    }

    @Override
    protected void rollbackPack(Collection<DfsPackDescription> desc) {

    }

    @Override
    protected List<DfsPackDescription> listPacks() throws IOException {
        return null;
    }

    @Override
    protected ReadableChannel openFile(DfsPackDescription desc, PackExt ext) throws FileNotFoundException, IOException {
        return null;
    }

    @Override
    protected DfsOutputStream writeFile(DfsPackDescription desc, PackExt ext) throws IOException {
        return null;
    }

    @Override
    public long getApproximateObjectCount() {
        return 0;
    }

}
