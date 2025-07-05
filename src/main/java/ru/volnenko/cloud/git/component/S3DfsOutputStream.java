package ru.volnenko.cloud.git.component;

import org.eclipse.jgit.internal.storage.dfs.DfsOutputStream;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;

public abstract class S3DfsOutputStream extends DfsOutputStream {

    private final ByteArrayOutputStream dst = new ByteArrayOutputStream();

    private byte[] data;

    public S3DfsOutputStream() {
    }

    public void write(byte[] buf, int off, int len) {
        this.data = null;
        this.dst.write(buf, off, len);
    }

    public int read(long position, ByteBuffer buf) {
        byte[] d = this.getData();
        int n = Math.min(buf.remaining(), d.length - (int)position);
        if (n == 0) {
            return -1;
        } else {
            buf.put(d, (int)position, n);
            return n;
        }
    }

    public byte[] getData() {
        if (this.data == null) {
            this.data = this.dst.toByteArray();
        }

        return this.data;
    }

    public abstract void flush();

    public void close() {
        this.flush();
    }

}
