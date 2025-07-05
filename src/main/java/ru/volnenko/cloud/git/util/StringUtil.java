package ru.volnenko.cloud.git.util;

import lombok.NonNull;

public final class StringUtil {

    @NonNull
    public static String removeSuffixIfExists(@NonNull final String key, @NonNull final String suffix) {
        return key.endsWith(suffix) ? key.substring(0, key.length() - suffix.length()) : key;
    }

}
