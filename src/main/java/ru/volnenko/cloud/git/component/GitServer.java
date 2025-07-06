package ru.volnenko.cloud.git.component;

import io.minio.MinioClient;
import lombok.*;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jgit.http.server.GitServlet;
import ru.volnenko.cloud.git.servlet.system.HealthzServlet;
import ru.volnenko.cloud.git.servlet.repository.RepositoryDataServlet;
import ru.volnenko.cloud.git.util.MinioUtil;

public final class GitServer {

    @NonNull
    private final GitServlet gitServlet = new GitServlet();

    @NonNull
    private final HealthzServlet healthzServlet = new HealthzServlet();

    @NonNull
    private final ServletHandler handler = new ServletHandler();

    @NonNull
    private final MinioClient minioClient = MinioUtil.getMinioClient();

    @NonNull
    private final MainResolver mainResolver = new MainResolver(minioClient);

    @NonNull
    private final RepositoryDataServlet repositoryDataServlet = new RepositoryDataServlet(minioClient);

    @Getter
    @Setter
    private final int port = 80;

    public GitServer() {
        gitServlet.setRepositoryResolver(mainResolver);
    }

    @SneakyThrows
    public void start() {
        @NonNull final Server server = new Server(port);
        server.setHandler(handler);
        handler.addServletWithMapping(new ServletHolder(gitServlet), "/*");
        handler.addServletWithMapping(new ServletHolder(healthzServlet), "/healthz/");
        handler.addServletWithMapping(new ServletHolder(repositoryDataServlet), "/curl/repository/*");
        server.start();
    }

}
