package ru.volnenko.cloud.git.component;

import lombok.NonNull;
import org.eclipse.jgit.internal.storage.dfs.DfsObjDatabase;
import org.eclipse.jgit.internal.storage.dfs.DfsRepository;
import org.eclipse.jgit.internal.storage.dfs.DfsRepositoryBuilder;
import org.eclipse.jgit.internal.storage.dfs.DfsRepositoryDescription;
import org.eclipse.jgit.lib.RefDatabase;

public final class RepositoryS3 extends DfsRepository {

    @NonNull
    private static final RepositoryS3Builder BUILDER = new RepositoryS3Builder();

    public RepositoryS3(@NonNull final DfsRepositoryDescription desc) {
        super(BUILDER);
        BUILDER.setRepositoryDescription(desc);
    }

    public RepositoryS3(@NonNull final DfsRepositoryBuilder builder) {
        super(builder);
    }

    @Override
    public DfsObjDatabase getObjectDatabase() {
        return null;
    }

    @Override
    public RefDatabase getRefDatabase() {
        return null;
    }

}
