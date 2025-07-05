package ru.volnenko.cloud.git.component;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.SneakyThrows;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jgit.http.server.GitServlet;
import org.eclipse.jgit.internal.storage.dfs.DfsRepositoryDescription;
import org.example.MemoryRepositoryWww;

public final class GitServer {

    @NonNull
    private final GitServlet gitServlet = new GitServlet();

    @NonNull
    private final ServletHandler handler = new ServletHandler();

    @NonNull
    private final ServletHolder holder = new ServletHolder(gitServlet);

    @Getter
    @Setter
    private int port = 80;

    public GitServer() {
        gitServlet.setRepositoryResolver(( req,  name) -> {
            throw new RuntimeException("Error!");
        });
    }

    @SneakyThrows
    public void start() {
        Server server = new Server(8080);
        server.setHandler(handler);
        handler.addServletWithMapping(holder, "/*");
        server.start();
    }

}
