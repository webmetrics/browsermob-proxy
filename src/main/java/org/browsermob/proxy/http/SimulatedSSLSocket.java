package org.browsermob.proxy.http;

import javax.net.ssl.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.SocketAddress;
import java.net.SocketException;
import java.nio.channels.SocketChannel;
import java.util.Date;

public class SimulatedSSLSocket extends SSLSocket {
    private SSLSocket socket;
    private long downstreamKbps;
    private long upstreamKbps;
    private long latency;
    private Date handshakeStart;
    private RequestInfo requestInfo;

    public SimulatedSSLSocket(SSLSocket socket, long downstreamKbps, long upstreamKbps, long latency) {
        this.socket = SimulatedSocket.configure(socket);
        this.downstreamKbps = downstreamKbps;
        this.upstreamKbps = upstreamKbps;
        this.latency = latency;
        this.addHandshakeCompletedListener(new HandshakeCompletedListener() {
            @Override
            public void handshakeCompleted(HandshakeCompletedEvent handshakeCompletedEvent) {
                if (requestInfo != null) {
                    requestInfo.ssl(handshakeStart, new Date());
                }
            }
        });
    }

    @Override
    public String[] getSupportedCipherSuites() {
        return socket.getSupportedCipherSuites();
    }

    @Override
    public String[] getEnabledCipherSuites() {
        return socket.getEnabledCipherSuites();
    }

    @Override
    public void setEnabledCipherSuites(String[] strings) {
        socket.setEnabledCipherSuites(strings);
    }

    @Override
    public String[] getSupportedProtocols() {
        return socket.getSupportedProtocols();
    }

    @Override
    public String[] getEnabledProtocols() {
        return socket.getEnabledProtocols();
    }

    @Override
    public void setEnabledProtocols(String[] strings) {
        socket.setEnabledProtocols(strings);
    }

    @Override
    public SSLSession getSession() {
        return socket.getSession();
    }

    @Override
    public void addHandshakeCompletedListener(HandshakeCompletedListener handshakeCompletedListener) {
        socket.addHandshakeCompletedListener(handshakeCompletedListener);
    }

    @Override
    public void removeHandshakeCompletedListener(HandshakeCompletedListener handshakeCompletedListener) {
        socket.removeHandshakeCompletedListener(handshakeCompletedListener);
    }

    @Override
    public void startHandshake() throws IOException {
        socket.startHandshake();
    }

    @Override
    public void setUseClientMode(boolean b) {
        socket.setUseClientMode(b);
    }

    @Override
    public boolean getUseClientMode() {
        return socket.getUseClientMode();
    }

    @Override
    public void setNeedClientAuth(boolean b) {
        socket.setNeedClientAuth(b);
    }

    @Override
    public boolean getNeedClientAuth() {
        return socket.getNeedClientAuth();
    }

    @Override
    public void setWantClientAuth(boolean b) {
        socket.setWantClientAuth(b);
    }

    @Override
    public boolean getWantClientAuth() {
        return socket.getWantClientAuth();
    }

    @Override
    public void setEnableSessionCreation(boolean b) {
        socket.setEnableSessionCreation(b);
    }

    @Override
    public boolean getEnableSessionCreation() {
        return socket.getEnableSessionCreation();
    }

    @Override
    public SSLParameters getSSLParameters() {
        return socket.getSSLParameters();
    }

    @Override
    public void setSSLParameters(SSLParameters sslParameters) {
        socket.setSSLParameters(sslParameters);
    }

    @Override
    public void connect(SocketAddress endpoint) throws IOException {
        Date start = new Date();
        socket.connect(endpoint);
        Date end = new Date();
        RequestInfo.get().connect(start, end);
        handshakeStart = new Date();
        requestInfo = RequestInfo.get();
    }

    @Override
    public void connect(SocketAddress endpoint, int timeout) throws IOException {
        Date start = new Date();
        socket.connect(endpoint, timeout);
        Date end = new Date();
        RequestInfo.get().connect(start, end);
        handshakeStart = new Date();
        requestInfo = RequestInfo.get();
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
