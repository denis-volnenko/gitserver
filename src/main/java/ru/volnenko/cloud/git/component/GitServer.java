package ru.volnenko.cloud.git.component;

import io.minio.MinioClient;
import lombok.*;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jgit.http.server.GitServlet;
import ru.volnenko.cloud.git.builder.RepositorySettingBuilder;
import ru.volnenko.cloud.git.servlet.filter.IndexFilter;
import ru.volnenko.cloud.git.servlet.filter.SecurityFilter;
import ru.volnenko.cloud.git.servlet.repository.RepositoryDescriptionServlet;
import ru.volnenko.cloud.git.servlet.system.HealthzServlet;
import ru.volnenko.cloud.git.servlet.repository.RepositoryDataServlet;
import ru.volnenko.cloud.git.util.MinioUtil;
import ru.volnenko.cloud.git.util.SettingUtil;

public final class GitServer {


    @NonNull
    private final GitServlet gitServlet = new GitServlet();

    @NonNull
    private final HealthzServlet healthzServlet = new HealthzServlet();

    @NonNull
    private final RepositorySettingBuilder repositorySettingBuilder = new RepositorySettingBuilder();

    @NonNull
    private final IndexFilter indexFilter = new IndexFilter();

    @NonNull
    private final ServletHandler handler = new ServletHandler();

    @NonNull
    private final MinioClient minioClient = MinioUtil.getMinioClient();

    @NonNull
    private final CacheProvider cacheProvider = new CacheProvider(minioClient);

    @NonNull
    private final RepositoryInitializer repositoryInitializer = new RepositoryInitializer(minioClient, repositorySettingBuilder);

    @NonNull
    private final SecurityFilter securityFilter = new SecurityFilter();

    @NonNull
    private final RepositoryDataServlet repositoryDataServlet = new RepositoryDataServlet(repositoryInitializer);

    @NonNull
    private final MainResolver mainResolver = new MainResolver(minioClient, repositoryInitializer, cacheProvider);

    private final RepositoryDescriptionServlet repositoryDescriptionServlet = new RepositoryDescriptionServlet(repositoryInitializer);

    @Getter
    @Setter
    private final int port = 8080;

    public GitServer() {
        gitServlet.setRepositoryResolver(mainResolver);
    }

    @SneakyThrows
    public void start() {
        @NonNull final Server server = new Server(port);
        server.setHandler(handler);
        handler.addServletWithMapping(new ServletHolder(gitServlet), "/*");
        handler.addServletWithMapping(new ServletHolder(healthzServlet), "/healthz/");
        handler.addServletWithMapping(new ServletHolder(repositoryDataServlet), "/curl/repository/data/*");
        handler.addServletWithMapping(new ServletHolder(repositoryDescriptionServlet), "/curl/repository/description/*");
        if (SettingUtil.getSecurityEnabled()) handler.addFilterWithMapping(new FilterHolder(securityFilter), "/*", null);
        handler.addFilterWithMapping(new FilterHolder(indexFilter), "/", null);
        server.start();
    }

}
