package org.browsermob.proxy.http;

import java.io.IOException;
import java.io.OutputStream;

public class SimulatedOutputStream extends OutputStream {
    private OutputStream outputStream;
    private long latency;
    private long bytesPerSecond;
    private boolean roundUp;

    public SimulatedOutputStream(OutputStream outputStream, long kiloBitsPerSecond, long latency) {
        this.outputStream = outputStream;
        this.latency = latency;
        this.bytesPerSecond = kiloBitsPerSecond * 1000 / 8;
    }

    public void write(int b) throws IOException {
        long start = System.currentTimeMillis();
        outputStream.write(b);
        long end = System.currentTimeMillis();
        simulate(1, end - start);
    }

    public void write(byte[] b) throws IOException {
        long start = System.currentTimeMillis();
        outputStream.write(b);
        long end = System.currentTimeMillis();
        simulate(b.length, end - start);
    }

    public void write(byte[] b, int off, int len) throws IOException {
        long start = System.currentTimeMillis();
        outputStream.write(b, off, len);
        long end = System.currentTimeMillis();
        simulate(len, end - start);
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


    public void flush() throws IOException {
        outputStream.flush();
    }

    public void close() throws IOException {
        outputStream.close();
    }
}
