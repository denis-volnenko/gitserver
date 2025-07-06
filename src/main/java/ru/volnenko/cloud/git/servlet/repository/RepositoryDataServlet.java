package ru.volnenko.cloud.git.servlet.repository;

import io.minio.MinioClient;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Cleanup;
import lombok.NonNull;
import lombok.SneakyThrows;
import ru.volnenko.cloud.git.util.SettingUtil;
import ru.volnenko.cloud.git.util.StringUtil;

import java.io.ByteArrayInputStream;
import java.io.IOException;

public final class RepositoryDataServlet extends HttpServlet {

    @NonNull
    private final MinioClient minioClient;

    public RepositoryDataServlet(@NonNull final MinioClient minioClient) {
        this.minioClient = minioClient;
    }

    @Override
    @SneakyThrows
    protected void doPost(
            @NonNull final HttpServletRequest req,
            @NonNull final HttpServletResponse resp
    ) throws ServletException, IOException {
        final String repo = StringUtil.removePrefixIfExists(req.getRequestURI(), "/curl/repository/");
        resp.getWriter().println("CREATED: " + repo);
        {
            @Cleanup @NonNull final ByteArrayInputStream inputStream = new ByteArrayInputStream("".getBytes());
            minioClient.putObject(SettingUtil.getS3Bucket(), repo + "/description", inputStream, "text/plain");
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
            minioClient.putObject(SettingUtil.getS3Bucket(), repo + "/config", inputStream, "text/plain");
        }
        resp.getWriter().println("OK");
    }

}
