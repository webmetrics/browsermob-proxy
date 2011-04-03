package org.browsermob.proxy.http;

import org.browsermob.proxy.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.nio.channels.SocketChannel;
import java.util.Date;

public class SimulatedSocket extends Socket {
    private static final Log LOG = new Log();

    private Socket socket;
    private long downstreamKbps;
    private long upstreamKbps;
    private long latency;

    public SimulatedSocket(Socket socket, long downstreamKbps, long upstreamKbps, long latency) {
        this.socket = configure(socket);
        this.downstreamKbps = downstreamKbps;
        this.upstreamKbps = upstreamKbps;
        this.latency = latency;
    }

    public static <T extends Socket> T configure(T sock) {
        // Configure the socket to be Load Test Friendly!
        // If we don't set these, we can easily use up too many sockets, even when we're cleaning/closing the sockets
        // responsibly. The reason is that they will stick around in TIME_WAIT for some time (ie: 1-4 minutes) and once
        // they get to 64K (on Linux) or 16K (on Mac) we can't make any more requests. While those limits can be raised
        // with a configuration setting in the OS, we really don't need to change things globally. We just need to make
        // sure that when we close a socket it gets ditched right away and doesn't stick around in TIME_WAIT.
        //
        // This problem is most easily noticable/problematic for load tests that use a single transaction to issue
        // one HTTP request and then end the transaction, thereby shutting down the HTTP socket. This can easily create
        // 64K+ sockets in TIME_WAIT state, preventing any other requests from going out and producing a false-negative
        // "connection refused" error message.
        //
        // For further reading, check out HttpClient's FAQ on this subject:
        // http://wiki.apache.org/HttpComponents/FrequentlyAskedConnectionManagementQuestions

        try {
            sock.setReuseAddress(true);
            sock.setSoLinger(true, 0);
        } catch (Exception e) {
            LOG.warn("Could not prepare socket: %s", e.getMessage());
        }

        return sock;
    }

    @Override
    public void connect(SocketAddress endpoint) throws IOException {
        Date start = new Date();
        socket.connect(endpoint);
        Date end = new Date();
        RequestInfo.get().connect(start, end);
    }

    @Override
    public void connect(SocketAddress endpoint, int timeout) throws IOException {
        Date start = new Date();
        socket.connect(endpoint, timeout);
        Date end = new Date();
        RequestInfo.get().connect(start, end);
    }

    @Override
    public void bind(SocketAddress bindpoint) throws IOException {
        socket.bind(bindpoint);
    }

    @Override
    public InetAddress getInetAddress() {
        return socket.getInetAddress();
    }

    @Override
    public InetAddress getLocalAddress() {
        return socket.getLocalAddress();
    }

    @Override
    public int getPort() {
        return socket.getPort();
    }

    @Override
    public int getLocalPort() {
        return socket.getLocalPort();
    }

    @Override
    public SocketAddress getRemoteSocketAddress() {
        return socket.getRemoteSocketAddress();
    }

    @Override
    public SocketAddress getLocalSocketAddress() {
        return socket.getLocalSocketAddress();
    }

    @Override
    public SocketChannel getChannel() {
        return socket.getChannel();
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return new SimulatedInputStream(socket.getInputStream(), downstreamKbps, latency);
    }

    @Override
    public OutputStream getOutputStream() throws IOException {
        return new SimulatedOutputStream(socket.getOutputStream(), upstreamKbps, latency);
    }

    @Override
    public void setTcpNoDelay(boolean on) throws SocketException {
        socket.setTcpNoDelay(on);
    }

    @Override
    public boolean getTcpNoDelay() throws SocketException {
        return socket.getTcpNoDelay();
    }

    @Override
    public void setSoLinger(boolean on, int linger) throws SocketException {
        socket.setSoLinger(on, linger);
    }

    @Override
    public int getSoLinger() throws SocketException {
        return socket.getSoLinger();
    }

    @Override
    public void sendUrgentData(int data) throws IOException {
        socket.sendUrgentData(data);
    }

    @Override
    public void setOOBInline(boolean on) throws SocketException {
        socket.setOOBInline(on);
    }

    @Override
    public boolean getOOBInline() throws SocketException {
        return socket.getOOBInline();
    }

    @Override
    public void setSoTimeout(int timeout) throws SocketException {
        socket.setSoTimeout(timeout);
    }

    @Override
    public int getSoTimeout() throws SocketException {
        return socket.getSoTimeout();
    }

    @Override
    public void setSendBufferSize(int size) throws SocketException {
        socket.setSendBufferSize(size);
    }

    @Override
    public int getSendBufferSize() throws SocketException {
        return socket.getSendBufferSize();
    }

    @Override
    public void setReceiveBufferSize(int size) throws SocketException {
        socket.setReceiveBufferSize(size);
    }

    @Override
    public int getReceiveBufferSize() throws SocketException {
        return socket.getReceiveBufferSize();
    }

    @Override
    public void setKeepAlive(boolean on) throws SocketException {
        socket.setKeepAlive(on);
    }

    @Override
    public boolean getKeepAlive() throws SocketException {
        return socket.getKeepAlive();
    }

    @Override
    public void setTrafficClass(int tc) throws SocketException {
        socket.setTrafficClass(tc);
    }

    @Override
    public int getTrafficClass() throws SocketException {
        return socket.getTrafficClass();
    }

    @Override
    public void setReuseAddress(boolean on) throws SocketException {
        socket.setReuseAddress(on);
    }

    @Override
    public boolean getReuseAddress() throws SocketException {
        return socket.getReuseAddress();
    }

    @Override
    public void close() throws IOException {
        socket.close();
    }

    @Override
    public void shutdownInput() throws IOException {
        socket.shutdownInput();
    }

    @Override
    public void shutdownOutput() throws IOException {
        socket.shutdownOutput();
    }

    @Override
    public String toString() {
        return socket.toString();
    }

    @Override
    public boolean isConnected() {
        return socket.isConnected();
    }

    @Override
    public boolean isBound() {
        return socket.isBound();
    }

    @Override
    public boolean isClosed() {
        return socket.isClosed();
    }

    @Override
    public boolean isInputShutdown() {
        return socket.isInputShutdown();
    }

    @Override
    public boolean isOutputShutdown() {
        return socket.isOutputShutdown();
    }

    @Override
    public void setPerformancePreferences(int connectionTime, int latency, int bandwidth) {
        socket.setPerformancePreferences(connectionTime, latency, bandwidth);
    }
}
