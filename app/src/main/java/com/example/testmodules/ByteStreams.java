package com.example.testmodules;

import android.annotation.SuppressLint;

import androidx.core.util.Preconditions;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public final class ByteStreams {
    private static final int BUF_SIZE = 4096;

    @SuppressLint("RestrictedApi")
    public static long copy(InputStream from, OutputStream to) throws IOException {
        Preconditions.checkNotNull(from);
        Preconditions.checkNotNull(to);
        byte[] buf = new byte[4096];
        long total = 0L;

        while(true) {
            int r = from.read(buf);
            if (r == -1) {
                return total;
            }

            to.write(buf, 0, r);
            total += (long)r;
        }
    }

    public static InputStream limit(InputStream in, long limit) {
        return new ByteStreams.LimitedInputStream(in, limit);
    }

    public static int read(InputStream in, byte[] b, int off, int len) throws IOException {
        Preconditions.checkNotNull(in);
        Preconditions.checkNotNull(b);
        if (len < 0) {
            throw new IndexOutOfBoundsException("len is negative");
        } else {
            int total;
            int result;
            for(total = 0; total < len; total += result) {
                result = in.read(b, off + total, len - total);
                if (result == -1) {
                    break;
                }
            }

            return total;
        }
    }

    private ByteStreams() {
    }

    private static final class LimitedInputStream extends FilterInputStream {
        private long left;
        private long mark = -1L;

        LimitedInputStream(InputStream in, long limit) {
            super(in);
            Preconditions.checkNotNull(in);
            Preconditions.checkArgument(limit >= 0L, "limit must be non-negative");
            this.left = limit;
        }

        public int available() throws IOException {
            return (int)Math.min((long)this.in.available(), this.left);
        }

        public synchronized void mark(int readLimit) {
            this.in.mark(readLimit);
            this.mark = this.left;
        }

        public int read() throws IOException {
            if (this.left == 0L) {
                return -1;
            } else {
                int result = this.in.read();
                if (result != -1) {
                    --this.left;
                }

                return result;
            }
        }

        public int read(byte[] b, int off, int len) throws IOException {
            if (this.left == 0L) {
                return -1;
            } else {
                len = (int)Math.min((long)len, this.left);
                int result = this.in.read(b, off, len);
                if (result != -1) {
                    this.left -= (long)result;
                }

                return result;
            }
        }

        public synchronized void reset() throws IOException {
            if (!this.in.markSupported()) {
                throw new IOException("Mark not supported");
            } else if (this.mark == -1L) {
                throw new IOException("Mark not set");
            } else {
                this.in.reset();
                this.left = this.mark;
            }
        }

        public long skip(long n) throws IOException {
            n = Math.min(n, this.left);
            long skipped = this.in.skip(n);
            this.left -= skipped;
            return skipped;
        }
    }
}