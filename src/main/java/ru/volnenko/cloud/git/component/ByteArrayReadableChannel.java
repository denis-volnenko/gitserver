package ru.volnenko.cloud.git.component;

import org.eclipse.jgit.internal.storage.dfs.ReadableChannel;

import java.nio.ByteBuffer;

public final class ByteArrayReadableChannel implements ReadableChannel {

    private final byte[] data;

    private final int blockSize;

    private int position;

    private boolean open = true;

    public ByteArrayReadableChannel(byte[] buf, int blockSize) {
        this.data = buf;
        this.blockSize = blockSize;
    }

    public int read(ByteBuffer dst) {
        int n = Math.min(dst.remaining(), this.data.length - this.position);
        if (n == 0) {
            return -1;
        } else {
            dst.put(this.data, this.position, n);
            this.position += n;
            return n;
        }
    }

    public void close() {
        this.open = false;
    }

    public boolean isOpen() {
        return this.open;
    }

    public long position() {
        return (long)this.position;
    }

    public void position(long newPosition) {
        this.position = (int)newPosition;
    }

    public long size() {
        return (long)this.data.length;
    }

    public int blockSize() {
        return this.blockSize;
    }

    public void setReadAheadBytes(int b) {
    }

}
