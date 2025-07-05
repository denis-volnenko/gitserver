package ru.volnenko.cloud.git.component;

import io.minio.MinioClient;
import lombok.NonNull;
import org.eclipse.jgit.internal.storage.dfs.*;

public final class RepositoryS3 extends DfsRepository {

    @NonNull
    private final DfsReaderOptions options = new DfsReaderOptions();

    @NonNull
    private S3ObjectDatabase objectDatabase;

    @NonNull
    private final S3RefDatabase refDatabase = new S3RefDatabase(this);

    @NonNull
    private String gitwebDescription = "";

    @NonNull
    private MinioClient minioClient;

    @NonNull
    private String bucketName;

    public RepositoryS3(
            @NonNull final DfsRepositoryDescription desc,
            @NonNull final DfsRepositoryBuilder builder,
            @NonNull final MinioClient minioClient,
            @NonNull final String bucketName
    ) {
        this(builder);
        this.minioClient = minioClient;
        this.bucketName = bucketName;
        builder.setRepositoryDescription(desc);
        objectDatabase = new S3ObjectDatabase(this, options, minioClient, bucketName);
    }

    public RepositoryS3(@NonNull final DfsRepositoryBuilder builder) {
        super(builder);
    }

    @NonNull
    @Override
    public String getGitwebDescription() {
        return gitwebDescription;
    }

    @Override
    public void setGitwebDescription(@NonNull final String gitwebDescription) {
        this.gitwebDescription = gitwebDescription;
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
