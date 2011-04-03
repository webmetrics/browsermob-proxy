package org.browsermob.proxy.http;

import org.apache.http.conn.scheme.HostNameResolver;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.params.HttpParams;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

public class TrustingSSLSocketFactory extends SSLSocketFactory {
    private static SSLContext sslContext;

    private long downstreamKbps;
    private long upstreamKbps;
    private long latency;

    static {
        try {
            sslContext = SSLContext.getInstance("SSLv3");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("TLS algorithm not found! Critical SSL error!", e);
        }
        TrustManager easyTrustManager = new X509TrustManager() {
            @Override
            public void checkClientTrusted(
                    X509Certificate[] chain,
                    String authType) throws CertificateException {
                // Oh, I am easy!
            }

            @Override
            public void checkServerTrusted(
                    X509Certificate[] chain,
                    String authType) throws CertificateException {
                // Oh, I am easy!
            }

            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }

        };
        try {
            sslContext.init(null, new TrustManager[]{easyTrustManager}, null);
        } catch (KeyManagementException e) {
            throw new RuntimeException("Unexpected key management error", e);
        }
    }

    @Override
    public Socket createSocket(Socket socket, String host, int port, boolean autoClose) throws IOException, UnknownHostException {
        SSLSocket sslSocket = (SSLSocket) super.createSocket(socket, host, port, autoClose);

        return new SimulatedSSLSocket(sslSocket, downstreamKbps, upstreamKbps, latency);
    }

    @Override
    public Socket connectSocket(Socket sock, String host, int port, InetAddress localAddress, int localPort, HttpParams params) throws IOException {
        SSLSocket socket = (SSLSocket) super.connectSocket(sock, host, port, localAddress, localPort, params);
        return new SimulatedSSLSocket(socket, downstreamKbps, upstreamKbps, latency);
    }

    @Override
    public Socket createSocket() throws IOException {
        SSLSocket socket = (SSLSocket) super.createSocket();
        socket.setEnabledProtocols(new String[] {"SSLv3", "TLSv1"});
//        socket.setEnabledCipherSuites(new String[] { "SSL_RSA_WITH_RC4_128_MD5" });


        return new SimulatedSSLSocket(socket, downstreamKbps, upstreamKbps, latency);
    }

    public TrustingSSLSocketFactory(HostNameResolver nameResolver) {
        super(sslContext, nameResolver);
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
