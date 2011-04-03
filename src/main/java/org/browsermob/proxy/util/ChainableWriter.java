package org.browsermob.proxy.util;

import java.io.IOException;
import java.io.Writer;

public class ChainableWriter extends Writer {
    private Writer writer;

    public ChainableWriter(Writer writer) {
        this.writer = writer;
    }

    public void write(int c) throws IOException {
        writer.write(c);
    }

    public void write(char[] cbuf) throws IOException {
        writer.write(cbuf);
    }

    public void write(char[] cbuf, int off, int len) throws IOException {
        writer.write(cbuf, off, len);
    }

    public void write(String str) throws IOException {
        writer.write(str);
    }

    public void write(String str, int off, int len) throws IOException {
        writer.write(str, off, len);
    }

    public ChainableWriter append(CharSequence csq) throws IOException {
        writer.append(csq);

        return this;
    }

    public ChainableWriter append(CharSequence csq, int start, int end) throws IOException {
        writer.append(csq, start, end);

        return this;
    }

    public ChainableWriter append(long aLong) throws IOException {
        return append(String.valueOf(aLong));
    }

    public ChainableWriter append(int anInt) throws IOException {
        return append(String.valueOf(anInt));
    }

    public ChainableWriter append(boolean bool) throws IOException {
        return append(String.valueOf(bool));
    }

    public ChainableWriter append(Object o) throws IOException {
        return append(o.toString());
    }

    public ChainableWriter append(char c) throws IOException {
        writer.append(c);

        return this;
    }

    public void flush() throws IOException {
        writer.flush();
    }

    public void close() throws IOException {
        writer.close();
    }
}
