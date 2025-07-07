package ru.volnenko.cloud.git.component;

import io.minio.MinioClient;
import lombok.Cleanup;
import lombok.NonNull;
import lombok.SneakyThrows;
import org.eclipse.jgit.internal.storage.dfs.DfsRepositoryDescription;
import ru.volnenko.cloud.git.util.SettingUtil;

import java.io.ByteArrayInputStream;

public final class RepositoryInitializer {

    @NonNull
    private static final String TYPE = "text/plain";

    @NonNull
    private final MinioClient minioClient;

    public RepositoryInitializer(@NonNull final MinioClient minioClient) {
        this.minioClient = minioClient;
    }

    @SneakyThrows
    public void init(@NonNull final String repoName) {
        {
            @Cleanup @NonNull final ByteArrayInputStream inputStream = new ByteArrayInputStream("".getBytes());
            minioClient.putObject(SettingUtil.getS3Bucket(), repoName + "/description", inputStream, TYPE);
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
            minioClient.putObject(SettingUtil.getS3Bucket(), repoName + "/config", inputStream, TYPE);
        }
    }

    public void init(@NonNull final DfsRepositoryDescription description) {
        init(description.getRepositoryName() + ".git");
    }

}
