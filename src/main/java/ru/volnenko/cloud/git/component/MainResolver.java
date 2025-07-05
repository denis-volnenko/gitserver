package ru.volnenko.cloud.git.component;

import jakarta.servlet.http.HttpServletRequest;
import org.eclipse.jgit.errors.RepositoryNotFoundException;
import org.eclipse.jgit.internal.storage.dfs.DfsRepositoryDescription;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.transport.ServiceMayNotContinueException;
import org.eclipse.jgit.transport.resolver.RepositoryResolver;
import org.eclipse.jgit.transport.resolver.ServiceNotAuthorizedException;
import org.eclipse.jgit.transport.resolver.ServiceNotEnabledException;
import org.example.MemoryRepositoryWww;
import ru.volnenko.cloud.git.exception.IncorrectRepositoryException;

public final class MainResolver implements RepositoryResolver<HttpServletRequest> {

    @Override
    public Repository open(HttpServletRequest req, String name) throws RepositoryNotFoundException, ServiceNotAuthorizedException, ServiceNotEnabledException, ServiceMayNotContinueException {
        final String url = req.getRequestURI();
        final String[] parts = url.split(".git/");
        if (parts.length > 1) {
            final DfsRepositoryDescription description = new DfsRepositoryDescription(url);
            final String repo = parts[0];
            System.out.println(repo);
        }
        throw new IncorrectRepositoryException();
    }

}
