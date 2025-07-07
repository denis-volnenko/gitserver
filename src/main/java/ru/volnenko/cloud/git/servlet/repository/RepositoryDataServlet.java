package ru.volnenko.cloud.git.servlet.repository;

import io.minio.MinioClient;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.SneakyThrows;
import ru.volnenko.cloud.git.component.RepositoryInitializer;
import ru.volnenko.cloud.git.util.StringUtil;

import java.io.IOException;

public final class RepositoryDataServlet extends HttpServlet {

    @NonNull
    private final RepositoryInitializer repositoryInitializer;

    public RepositoryDataServlet(
            @NonNull final RepositoryInitializer repositoryInitializer
    ) {
        this.repositoryInitializer = repositoryInitializer;
    }

    @Override
    @SneakyThrows
    protected void doPost(
            @NonNull final HttpServletRequest req,
            @NonNull final HttpServletResponse resp
    ) throws ServletException, IOException {
        final String repositoryName = StringUtil.removePrefixIfExists(req.getRequestURI(), "/curl/repository/");
        resp.getWriter().println("CREATED: " + repositoryName);
        repositoryInitializer.init(repositoryName);
        resp.getWriter().println("OK");
    }

}
