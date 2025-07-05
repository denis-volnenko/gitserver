package ru.volnenko.cloud.git.util;

import io.minio.MinioClient;
import lombok.NonNull;
import lombok.SneakyThrows;

public final class MinioUtil {

    @SneakyThrows
    public static MinioClient getMinioClient()  {
        @NonNull final String endpoint = SettingUtil.getS3Endpoint();
        @NonNull final String clientId = SettingUtil.getS3ClientId();
        @NonNull final String secretKey = SettingUtil.getS3SecretKey();
        return new MinioClient(endpoint, clientId, secretKey);
    }

}
