package ru.volnenko.cloud.git.component;

import io.minio.MinioClient;
import lombok.Cleanup;
import lombok.NonNull;
import lombok.SneakyThrows;
import org.eclipse.jgit.internal.storage.dfs.DfsRepositoryDescription;
import ru.volnenko.cloud.git.builder.RepositorySettingBuilder;
import ru.volnenko.cloud.git.exception.NotImplementedException;
import ru.volnenko.cloud.git.util.SettingUtil;

import java.io.ByteArrayInputStream;

public final class RepositoryInitializer {

    @NonNull
    private static final String TYPE = "text/plain";

    @NonNull
    private final MinioClient minioClient;

    private final RepositorySettingBuilder repositorySettingBuilder;

    public RepositoryInitializer(
            @NonNull final MinioClient minioClient,
            @NonNull final RepositorySettingBuilder repositorySettingBuilder
    ) {
        this.minioClient = minioClient;
        this.repositorySettingBuilder = repositorySettingBuilder;
    }

    @SneakyThrows
    private void initDescription(@NonNull final String repositoryName) {
        @Cleanup @NonNull final ByteArrayInputStream inputStream = new ByteArrayInputStream("".getBytes());
        minioClient.putObject(SettingUtil.getS3Bucket(), repositoryName + "/description", inputStream, TYPE);
    }

    @SneakyThrows
    private void initConfig(@NonNull final String repositoryName) {
        @NonNull final String value = repositorySettingBuilder.toString();
        @Cleanup @NonNull final ByteArrayInputStream inputStream = new ByteArrayInputStream(value.getBytes());
        minioClient.putObject(SettingUtil.getS3Bucket(), repositoryName + "/config", inputStream, TYPE);
    }

    public void init(@NonNull final String repositoryName) {
        initDescription(repositoryName);
        initConfig(repositoryName);
    }

    public void init(@NonNull final DfsRepositoryDescription description) {
        init(description.getRepositoryName() + ".git");
    }

    @NonNull
    public String getDescription(@NonNull final DfsRepositoryDescription description) {
        throw new NotImplementedException();
    }

}
