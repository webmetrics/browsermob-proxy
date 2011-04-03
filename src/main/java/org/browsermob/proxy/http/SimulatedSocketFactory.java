package org.browsermob.proxy.http;

import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.scheme.HostNameResolver;
import org.apache.http.conn.scheme.SocketFactory;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;

public class SimulatedSocketFactory implements SocketFactory {
    private final HostNameResolver nameResolver;
    private long downstreamKbps;
    private long upstreamKbps;
    private long latency;

    public SimulatedSocketFactory(final HostNameResolver nameResolver) {
        super();
        this.nameResolver = nameResolver;
    }

    public Socket createSocket() {
        return new SimulatedSocket(new Socket(), downstreamKbps, upstreamKbps, latency);
    }

    public Socket connectSocket(Socket sock, String host, int port,
                                InetAddress localAddress, int localPort,
                                HttpParams params)
            throws IOException {

        if (host == null) {
            throw new IllegalArgumentException("Target host may not be null.");
        }
        if (params == null) {
            throw new IllegalArgumentException("Parameters may not be null.");
        }

        if (sock == null)
            sock = createSocket();

        if ((localAddress != null) || (localPort > 0)) {

            // we need to bind explicitly
            if (localPort < 0)
                localPort = 0; // indicates "any"

            InetSocketAddress isa =
                    new InetSocketAddress(localAddress, localPort);
            sock.bind(isa);
        }

        int timeout = HttpConnectionParams.getConnectionTimeout(params);

        InetSocketAddress remoteAddress;
        if (this.nameResolver != null) {
            remoteAddress = new InetSocketAddress(this.nameResolver.resolve(host), port);
        } else {
            remoteAddress = new InetSocketAddress(host, port);
        }

        try {
            sock.connect(remoteAddress, timeout);
        } catch (SocketTimeoutException ex) {
            throw new ConnectTimeoutException("Connect to " + remoteAddress + " timed out");
        }

        return new SimulatedSocket(sock, downstreamKbps, upstreamKbps, latency);
    }

    /**
     * Checks whether a socket connection is secure. This factory creates plain socket connections which are not
     * considered secure.
     *
     * @param sock the connected socket
     * @return <code>false</code>
     * @throws IllegalArgumentException if the argument is invalid
     */
    public final boolean isSecure(Socket sock)
            throws IllegalArgumentException {

        if (sock == null) {
            throw new IllegalArgumentException("Socket may not be null.");
        }
        // This check is performed last since it calls a method implemented
        // by the argument object. getClass() is final in java.lang.Object.
        if (sock.isClosed()) {
            throw new IllegalArgumentException("Socket is closed.");
        }
        return false;
    }

    public void setDownstreamKbps(long downstreamKbps) {
        this.downstreamKbps = downstreamKbps;
    }

    public void setUpstreamKbps(long upstreamKbps) {
        this.upstreamKbps = upstreamKbps;
    }

    public void setLatency(long latency) {
        this.latency = latency;
    }
}
