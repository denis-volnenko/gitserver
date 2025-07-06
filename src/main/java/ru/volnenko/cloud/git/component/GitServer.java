package ru.volnenko.cloud.git.component;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.SneakyThrows;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jgit.http.server.GitServlet;

public final class GitServer {

    @NonNull
    private final GitServlet gitServlet = new GitServlet();

    @NonNull
    private final ServletHandler handler = new ServletHandler();

    @NonNull
    private final ServletHolder holder = new ServletHolder(gitServlet);

    @NonNull
    private final MainResolver mainResolver = new MainResolver();

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
        handler.addServletWithMapping(holder, "/*");
        server.start();
    }

}
