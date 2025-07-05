package ru.volnenko.cloud.git.util;

import lombok.NonNull;
import lombok.SneakyThrows;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

public final class DataUtil {

    @NonNull
    @SneakyThrows
    public static byte[] getBytes(@NonNull final InputStream inputStream) {
        @NonNull final ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        int nRead;
        @NonNull final byte[] data = new byte[1024];
        while ((nRead = inputStream.read(data, 0, data.length)) != -1) {
            buffer.write(data, 0, nRead);
        }
        buffer.flush();
        return buffer.toByteArray();
    }

}
