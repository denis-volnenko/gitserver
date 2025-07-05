package ru.volnenko.cloud.git.component;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.eclipse.jgit.internal.storage.dfs.DfsReftableDatabase;
import org.eclipse.jgit.internal.storage.dfs.DfsRepository;
import org.eclipse.jgit.internal.storage.reftable.ReftableConfig;

public final class S3RefDatabase extends DfsReftableDatabase {

    @NonNull
    private final ReftableConfig reftableConfig;

    @Getter
    @Setter
    private boolean performsAtomicTransactions = true;

    public S3RefDatabase(DfsRepository repo) {
        super(repo);
        @NonNull final ReftableConfig cfg = new ReftableConfig();
        cfg.setAlignBlocks(false);
        cfg.setIndexObjects(false);
        cfg.fromConfig(repo.getConfig());
        reftableConfig = cfg;
    }

    @NonNull
    public ReftableConfig createConfig() {
        return reftableConfig;
    }

    @NonNull
    @Override
    public ReftableConfig getReftableConfig() {
        return super.getReftableConfig();
    }

    @Override
    public boolean performsAtomicTransactions() {
        return this.performsAtomicTransactions;
    }

}
