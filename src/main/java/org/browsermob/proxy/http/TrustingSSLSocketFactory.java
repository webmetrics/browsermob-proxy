package org.browsermob.proxy.http;

import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.scheme.HostNameResolver;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.params.HttpParams;
import org.java_bandwidthlimiter.StreamManager;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

public class TrustingSSLSocketFactory extends SSLSocketFactory {

    public enum SSLAlgorithm {
        SSLv3,
        TLSv1
    }

    private static SSLContext sslContext;
    private StreamManager streamManager;

    static {
        try {
            sslContext = SSLContext.getInstance( SSLAlgorithm.SSLv3.name() );
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

    public TrustingSSLSocketFactory(HostNameResolver nameResolver, StreamManager streamManager) {
        super(sslContext, nameResolver);
        assert nameResolver != null;
        assert streamManager != null;
        this.streamManager = streamManager;
    }

    //just an helper function to wrap a normal sslSocket into a simulated one so we can do throttling
    private Socket createSimulatedSocket(SSLSocket socket) {
        SimulatedSocketFactory.configure(socket);
        socket.setEnabledProtocols(new String[] { SSLAlgorithm.SSLv3.name(), SSLAlgorithm.TLSv1.name() } );
        //socket.setEnabledCipherSuites(new String[] { "SSL_RSA_WITH_RC4_128_MD5" });
        return new SimulatedSSLSocket(socket, streamManager);
    }

    @SuppressWarnings("deprecation")
    @Override
    public Socket createSocket() throws java.io.IOException {
        SSLSocket sslSocket = (SSLSocket) super.createSocket();
        return createSimulatedSocket(sslSocket);
    }

    @SuppressWarnings("deprecation")
    @Override
    public Socket connectSocket(Socket socket, String host, int port, InetAddress localAddress, int localPort, HttpParams params)
            throws java.io.IOException, java.net.UnknownHostException, org.apache.http.conn.ConnectTimeoutException {
        SSLSocket sslSocket = (SSLSocket) super.connectSocket(socket, host, port, localAddress, localPort, params);
        if( sslSocket instanceof SimulatedSSLSocket ) {
            return sslSocket;
        } else {
            return createSimulatedSocket(sslSocket);
        }
    }

    @Override
    public Socket createSocket(org.apache.http.params.HttpParams params) throws java.io.IOException {
        SSLSocket sslSocket = (SSLSocket) super.createSocket(params);
        return createSimulatedSocket(sslSocket);
    }

    @Override
    public Socket connectSocket(Socket socket, InetSocketAddress remoteAddress, InetSocketAddress localAddress, HttpParams params)
            throws IOException, ConnectTimeoutException {
        SSLSocket sslSocket = (SSLSocket) super.connectSocket(socket, remoteAddress, localAddress, params);
        if( sslSocket instanceof SimulatedSSLSocket ) {
            return sslSocket;
        } else {
            //not sure this is needed
            return createSimulatedSocket(sslSocket);
        }
    }
}
