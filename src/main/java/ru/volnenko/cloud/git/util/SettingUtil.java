package ru.volnenko.cloud.git.util;

import lombok.NonNull;

public final class SettingUtil {

    public static boolean getSecurityEnabled() {
        final String value = System.getenv("SECURITY_ENABLED");
        if (value == null || value.isEmpty()) return true;
        return "true".equalsIgnoreCase(value);
    }

    @NonNull
    public static String getSecurityUsername() {
        final String value = System.getenv("SECURITY_USERNAME");
        if (value == null || value.isEmpty()) return "admin";
        return value;
    }

    @NonNull
    public static String getSecurityPassword() {
        final String value = System.getenv("SECURITY_PASSWORD");
        if (value == null || value.isEmpty()) return "admin";
        return value;
    }

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
