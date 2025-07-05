package ru.volnenko.cloud.git.component;

import jakarta.servlet.http.HttpServletRequest;
import lombok.NonNull;
import org.eclipse.jgit.errors.RepositoryNotFoundException;
import org.eclipse.jgit.internal.storage.dfs.DfsRepositoryDescription;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.transport.ServiceMayNotContinueException;
import org.eclipse.jgit.transport.resolver.RepositoryResolver;
import org.eclipse.jgit.transport.resolver.ServiceNotAuthorizedException;
import org.eclipse.jgit.transport.resolver.ServiceNotEnabledException;
import ru.volnenko.cloud.git.exception.IncorrectRepositoryException;

public final class MainResolver implements RepositoryResolver<HttpServletRequest> {

    @Override
    public Repository open(HttpServletRequest req, String name) throws RepositoryNotFoundException, ServiceNotAuthorizedException, ServiceNotEnabledException, ServiceMayNotContinueException {
        @NonNull final String url = req.getRequestURI();
        @NonNull final String[] parts = url.split(".git/");
        if (parts.length > 1) {
            @NonNull final DfsRepositoryDescription description = new DfsRepositoryDescription(url);
            @NonNull final String repo = parts[0];
            System.out.println(repo);
            @NonNull final RepositoryS3 repositoryS3 = new RepositoryS3(description);
            return repositoryS3;
        }
        throw new IncorrectRepositoryException();
    }

}
