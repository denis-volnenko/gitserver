package ru.volnenko.cloud.git.component;

import org.eclipse.jgit.internal.storage.dfs.DfsReftableDatabase;
import org.eclipse.jgit.internal.storage.dfs.DfsRepository;

public final class S3RefDatabase extends DfsReftableDatabase {

    public S3RefDatabase(DfsRepository repo) {
        super(repo);
    }
}
