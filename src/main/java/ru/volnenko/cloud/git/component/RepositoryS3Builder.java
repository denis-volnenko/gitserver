package ru.volnenko.cloud.git.component;

import org.eclipse.jgit.internal.storage.dfs.DfsRepositoryBuilder;

import java.io.IOException;

public final class RepositoryS3Builder extends DfsRepositoryBuilder<RepositoryS3Builder, RepositoryS3>  {

    @Override
    public RepositoryS3 build() throws IOException {
        return new RepositoryS3(this);
    }

}
