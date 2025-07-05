package ru.volnenko.cloud.git.component;

import lombok.NonNull;
import org.eclipse.jgit.internal.storage.dfs.*;

public final class RepositoryS3 extends DfsRepository {

    @NonNull
    private static final RepositoryS3Builder BUILDER = new RepositoryS3Builder();

    @NonNull
    private final DfsReaderOptions options = new DfsReaderOptions();

    @NonNull
    private final S3ObjectDatabase objectDatabase = new S3ObjectDatabase(this, options);

    @NonNull
    private final S3RefDatabase refDatabase = new S3RefDatabase(this);

    public RepositoryS3(@NonNull final DfsRepositoryDescription desc) {
        super(BUILDER);
        BUILDER.setRepositoryDescription(desc);
    }

    public RepositoryS3(@NonNull final DfsRepositoryBuilder builder) {
        super(builder);
    }

    @Override
    public S3ObjectDatabase getObjectDatabase() {
        return objectDatabase;
    }

    @Override
    public S3RefDatabase getRefDatabase() {
        return refDatabase;
    }

}
