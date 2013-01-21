package org.browsermob.proxy.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class ClonedOutputStream extends OutputStream {
    private OutputStream os;
    private ByteArrayOutputStream copy = new ByteArrayOutputStream();

    public ClonedOutputStream(OutputStream os) {
        this.os = os;
    }

    @Override
    public void write(int b) throws IOException {
        os.write(b);
        copy.write(b);
    }

    @Override
    public void write(byte[] b) throws IOException {
        os.write(b);
        copy.write(b);
    }

    @Override
    public void write(byte[] b, int off, int len) throws IOException {
        os.write(b, off, len);
        copy.write(b, off, len);
    }

    @Override
    public void flush() throws IOException {
        os.flush();
        copy.flush();
    }

    @Override
    public void close() throws IOException {
        os.close();
        copy.close();
    }

    public ByteArrayOutputStream getOutput() {
        return copy;
    }
}
