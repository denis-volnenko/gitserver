package ru.volnenko.cloud.git.util;

import lombok.NonNull;

public final class StringUtil {

    @NonNull
    public static String removeSuffixIfExists(@NonNull final String key, @NonNull final String suffix) {
        return key.endsWith(suffix) ? key.substring(0, key.length() - suffix.length()) : key;
    }

    public static String removePrefixIfExists(final String str, final String remove) {
        if (str.isEmpty() || remove.isEmpty()) {
            return str;
        }
        if (str.startsWith(remove)){
            return str.substring(remove.length());
        }
        return str;
    }

}
