package ru.volnenko.cloud.git.component;

import io.minio.MinioClient;
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
import ru.volnenko.cloud.git.util.SettingUtil;

import static ru.volnenko.cloud.git.util.StringUtil.removePrefixIfExists;

public final class MainResolver implements RepositoryResolver<HttpServletRequest> {

    @NonNull
    private final MinioClient minioClient;

    public MainResolver(@NonNull MinioClient minioClient) {
        this.minioClient = minioClient;
    }

    @NonNull
    private final String bucketName = SettingUtil.getS3Bucket();

    @Override
    public Repository open(@NonNull final HttpServletRequest req, @NonNull final String name) throws RepositoryNotFoundException, ServiceNotAuthorizedException, ServiceNotEnabledException, ServiceMayNotContinueException {
        @NonNull final String url = req.getRequestURI();
        @NonNull final String[] parts = url.split(".git/");
        if (parts.length > 1) {
            @NonNull final String repo = removePrefixIfExists(parts[0], "/");
            @NonNull final DfsRepositoryDescription description = new DfsRepositoryDescription(repo);
            System.out.println(repo);
            @NonNull final S3Repository repository = new S3Repository(description,  minioClient, bucketName);
            repository.getConfig().setString("http", null, "receivepack", "true");
            return repository;
        }
        throw new IncorrectRepositoryException();
    }

}
