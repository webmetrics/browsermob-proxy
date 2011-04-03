package org.browsermob.proxy.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class ClonedInputStream extends InputStream {
    private InputStream is;
    private ByteArrayOutputStream os = new ByteArrayOutputStream();

    public ClonedInputStream(InputStream is) {
        this.is = is;
    }

    public int read() throws IOException {
        int resp = is.read();
        os.write(resp);

        return resp;
    }

    public int read(byte[] b) throws IOException {
        int resp = is.read(b);
        os.write(b);

        return resp;
    }

    public int read(byte[] b, int off, int len) throws IOException {
        int resp = is.read(b, off, len);
        os.write(b, off, len);

        return resp;
    }

    public long skip(long n) throws IOException {
        return is.skip(n);
    }

    public int available() throws IOException {
        return is.available();
    }

    public void close() throws IOException {
        os.close();
        is.close();
    }

    public void mark(int readlimit) {
        is.mark(readlimit);
    }

    public void reset() throws IOException {
        os.reset();
        is.reset();
    }

    public boolean markSupported() {
        return is.markSupported();
    }

    public ByteArrayOutputStream getOutput() {
        return os;
    }
}
