package ru.volnenko.cloud.git.util;

import lombok.NonNull;

public final class SettingUtil {

    @NonNull
    public static String getS3Endpoint() {
        final String value = System.getenv("S3_ENDPOINT");
        if (value == null || value.isEmpty()) return "http://localhost:9000/";
        return value;
    }

    @NonNull
    public static String getS3Bucket() {
        final String value = System.getenv("S3_BUCKET");
        if (value == null || value.isEmpty()) return "gits3";
        return value;
    }

    @NonNull
    public static String getS3ClientId() {
        final String value = System.getenv("S3_CLIENT_ID");
        if (value == null || value.isEmpty()) return "minio";
        return value;
    }

    @NonNull
    public static String getS3SecretKey() {
        final String value = System.getenv("S3_SECRET_KEY");
        if (value == null || value.isEmpty()) return "minio";
        return value;
    }

}
