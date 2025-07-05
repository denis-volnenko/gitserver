package ru.volnenko.cloud.git.component;

import lombok.NonNull;
import org.eclipse.jgit.internal.storage.dfs.DfsRepositoryBuilder;

import java.io.IOException;

public final class RepositoryS3Builder extends DfsRepositoryBuilder<RepositoryS3Builder, RepositoryS3>  {

    @NonNull
    private static RepositoryS3Builder INSTANCE = new RepositoryS3Builder();

    @NonNull
    public static RepositoryS3Builder getInstance() {
        return INSTANCE;
    }

    @Override
    public RepositoryS3 build() throws IOException {
        return new RepositoryS3(this);
    }

}
