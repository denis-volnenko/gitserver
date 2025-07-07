package ru.volnenko.cloud.git.component;

import io.minio.MinioClient;
import lombok.Cleanup;
import lombok.NonNull;
import lombok.SneakyThrows;
import ru.volnenko.cloud.git.util.SettingUtil;

import java.io.ByteArrayInputStream;

public final class RepositoryInitializer {

    @NonNull
    private final MinioClient minioClient;

    public RepositoryInitializer(@NonNull final MinioClient minioClient) {
        this.minioClient = minioClient;
    }

    @SneakyThrows
    public void init(@NonNull final String repositoryName) {
        {
            @Cleanup @NonNull final ByteArrayInputStream inputStream = new ByteArrayInputStream("".getBytes());
            minioClient.putObject(SettingUtil.getS3Bucket(), repositoryName + "/description", inputStream, "text/plain");
        }
        {
            @Cleanup @NonNull final ByteArrayInputStream inputStream = new ByteArrayInputStream(
                    ("[core]\n" +
                            "\trepositoryformatversion = 0\n" +
                            "\tfilemode = true\n" +
                            "\tbare = false\n" +
                            "\tlogallrefupdates = true\n" +
                            "\tignorecase = true\n" +
                            "\tprecomposeunicode = true").getBytes());
            minioClient.putObject(SettingUtil.getS3Bucket(), repositoryName + "/config", inputStream, "text/plain");
        }
    }

}
