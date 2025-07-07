package ru.volnenko.cloud.git.component;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import io.minio.MinioClient;
import lombok.Cleanup;
import lombok.NonNull;
import lombok.SneakyThrows;
import ru.volnenko.cloud.git.util.DataUtil;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

public final class CacheProvider {

    @NonNull
    private static final String TYPE = "application/octet-stream";

    private final Cache<String, byte[]> cache;

    @NonNull
    private final MinioClient minioClient;

    public CacheProvider(
            @NonNull final MinioClient minioClient
    ) {
        this.minioClient = minioClient;
        cache = Caffeine.newBuilder().expireAfterWrite(60, TimeUnit.SECONDS).build();
    }

    @NonNull
    @SneakyThrows
    public byte[] getBytes(@NonNull final String bucketName, @NonNull final String objectName) {
        byte[] bytes = cache.getIfPresent(bucketName + "/" + objectName);
        if (bytes != null) return bytes;
        @NonNull @Cleanup final InputStream stream = minioClient.getObject(bucketName, objectName);
        return DataUtil.getBytes(stream);
    }

    @SneakyThrows
    public void setBytes(
            @NonNull final String bucketName,
            @NonNull final String objectName,
            @NonNull byte[] bytes
    ) {
        cache.put(bucketName + "/" + objectName, bytes);
        @Cleanup @NonNull final ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
        minioClient.putObject(bucketName, objectName, inputStream, TYPE);
    }

}
