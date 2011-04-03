package org.browsermob.proxy.http;

import java.io.IOException;
import java.io.InputStream;

public class SimulatedInputStream extends InputStream {
    private InputStream inputStream;
    private long latency;
    private long bytesPerSecond;
    private boolean roundUp;

    public SimulatedInputStream(InputStream inputStream, long kiloBitsPerSecond, long latency) {
        this.inputStream = inputStream;
        this.latency = latency;
        this.bytesPerSecond = kiloBitsPerSecond * 1000 / 8;
    }

    public int read() throws IOException {
        long start = System.currentTimeMillis();
        int b = inputStream.read();
        long end = System.currentTimeMillis();
        simulate(1, end - start);
        return b;
    }

    public int read(byte[] b) throws IOException {
        long start = System.currentTimeMillis();
        int bytesRead = inputStream.read(b);
        long end = System.currentTimeMillis();
        simulate(bytesRead, end - start);
        return bytesRead;
    }

    public int read(byte[] b, int off, int len) throws IOException {
        long start = System.currentTimeMillis();
        int bytesRead = inputStream.read(b, off, len);
        long end = System.currentTimeMillis();
        simulate(bytesRead, end - start);
        return bytesRead;
    }

    private void simulate(int bytes, long timeTaken) {
        if (bytesPerSecond <= 0) {
            return;
        }

        double d = ((double) bytes / bytesPerSecond) * 1000;
        long expectedTime = (long) (roundUp ? Math.ceil(d) : Math.floor(d)) + latency;
        roundUp = !roundUp;
        long diff = expectedTime - timeTaken;

        if (diff > 0) {
            try {
                Thread.sleep(diff);
            } catch (InterruptedException e) {
                Thread.interrupted();
            }
        }
    }

    public long skip(long n) throws IOException {
        return inputStream.skip(n);
    }

    public int available() throws IOException {
        return inputStream.available();
    }

    public void close() throws IOException {
        inputStream.close();
    }

    public void mark(int readlimit) {
        inputStream.mark(readlimit);
    }

    public void reset() throws IOException {
        inputStream.reset();
    }

    public boolean markSupported() {
        return inputStream.markSupported();
    }
}
