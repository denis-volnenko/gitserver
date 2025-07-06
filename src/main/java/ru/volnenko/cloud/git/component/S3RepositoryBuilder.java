package ru.volnenko.cloud.git.component;

import lombok.NonNull;
import org.eclipse.jgit.internal.storage.dfs.DfsRepositoryBuilder;

import java.io.IOException;

public final class S3RepositoryBuilder extends DfsRepositoryBuilder<S3RepositoryBuilder, S3Repository>  {

    @NonNull
    private static S3RepositoryBuilder INSTANCE = new S3RepositoryBuilder();

    @NonNull
    public static S3RepositoryBuilder getInstance() {
        return INSTANCE;
    }

    @Override
    public S3Repository build() throws IOException {
        return new S3Repository(this);
    }

}
