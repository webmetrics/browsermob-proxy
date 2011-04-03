package org.browsermob.proxy.util;

import java.io.ByteArrayOutputStream;

/**
 * A special ByteArrayOutputStream that will only write up to X number of bytes, after which it will simply ignore the
 * rest. This is useful for solving a JVM heap starvation issue (see MOB-216).
 */
public class CappedByteArrayOutputStream extends ByteArrayOutputStream {
    private int maxBytes;
    private boolean writeable = true;

    public CappedByteArrayOutputStream(int maxBytes) {
        this.maxBytes = maxBytes;
    }

    @Override
    public void write(int b) {
        if (writeable) {
            super.write(b);
            checkWritable();
        }
    }

    @Override
    public void write(byte[] b, int off, int len) {
        if (writeable) {
            super.write(b, off, len);
            checkWritable();
        }
    }

    private void checkWritable() {
        if (count > maxBytes) {
            writeable = false;
        }
    }
}
