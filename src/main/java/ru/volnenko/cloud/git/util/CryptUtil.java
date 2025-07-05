package ru.volnenko.cloud.git.util;

import lombok.NonNull;
import lombok.SneakyThrows;

import java.security.MessageDigest;

public final class CryptUtil {

    @SneakyThrows
    public static String toSHA1(@NonNull final String input) {
        @NonNull final MessageDigest md = MessageDigest.getInstance("SHA-1");
        @NonNull final byte[] result = md.digest(input.getBytes());
        @NonNull final StringBuilder hexString = new StringBuilder();
        for (final byte b : result) hexString.append(String.format("%02x", b));
        return hexString.toString();
    }

}
