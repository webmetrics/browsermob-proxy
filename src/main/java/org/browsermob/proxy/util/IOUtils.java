package org.browsermob.proxy.util;

import java.io.*;
import java.util.Date;

public class IOUtils {
    private static final int BUFFER = 4096;

    public static void copy(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[BUFFER];
        int length;
        while ((length = in.read(buffer)) != -1) {
            out.write(buffer, 0, length);
        }

        out.close();
        in.close();
    }

    public static Stats copyWithStats(InputStream is, OutputStream os, BandwidthSimulator simulator, boolean copyOutputForReadingLater) throws IOException {
        Date timeToFirstByte = null;

        byte[] buffer = new byte[BUFFER];
        int length;
        int bytes = 0;

        ByteArrayInputStream bais = null;

        try {
            ByteArrayOutputStream baos = null;
            if (copyOutputForReadingLater) {
                baos = new ByteArrayOutputStream();
            }

            // read the first byte
            int maxBytes = Math.min(simulator.maximumBytes(bytes), buffer.length);
            int firstByte = is.read();
            os.write(firstByte);
            if (copyOutputForReadingLater) {
                baos.write(firstByte);
            }
            bytes++;
            timeToFirstByte = new Date();

            do {
                length = is.read(buffer, 0, maxBytes);
                if (length != -1) {
                    bytes += length;
                    os.write(buffer, 0, length);
                    if (copyOutputForReadingLater) {
                        baos.write(buffer, 0, length);
                    }
                }
                maxBytes = Math.min(simulator.maximumBytes(bytes), buffer.length);
            } while (length != -1);

            if (copyOutputForReadingLater) {
                bais = new ByteArrayInputStream(baos.toByteArray());
            }
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                // ok to ignore
            }

            try {
                os.close();
            } catch (IOException e) {
                // ok to ignore
            }
        }

        return new Stats(bytes, timeToFirstByte, bais);
    }

    public static String readFully(InputStream in) throws IOException {
        StringBuilder sb = new StringBuilder();
        byte[] buffer = new byte[BUFFER];
        int length;
        while ((length = in.read(buffer)) != -1) {
            sb.append(new String(buffer, 0, length, "UTF-8"));
        }

        in.close();

        return sb.toString();
    }

    public static String readFully(InputStreamReader in) throws IOException {
        StringBuilder sb = new StringBuilder();
        char[] buffer = new char[BUFFER];
        int length;
        while ((length = in.read(buffer)) != -1) {
            sb.append(new String(buffer, 0, length));
        }

        in.close();

        return sb.toString();
    }

    public static class Stats {
        private long bytesCopied;
        private Date timeToFirstByte;
        private InputStream copy;

        public Stats(long bytesCopied, Date timeToFirstByte, InputStream copy) {
            this.bytesCopied = bytesCopied;
            this.timeToFirstByte = timeToFirstByte;
            this.copy = copy;
        }

        public long getBytesCopied() {
            return bytesCopied;
        }

        public Date getTimeToFirstByte() {
            return timeToFirstByte;
        }

        public InputStream getCopy() {
            return copy;
        }
    }
}
