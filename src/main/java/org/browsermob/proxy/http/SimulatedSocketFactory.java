package org.browsermob.proxy.http;

import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.scheme.HostNameResolver;
import org.apache.http.conn.scheme.SchemeSocketFactory;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.java_bandwidthlimiter.StreamManager;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketTimeoutException;
import java.util.Date;

public class SimulatedSocketFactory implements SchemeSocketFactory {
    private HostNameResolver hostNameResolver;
    private StreamManager streamManager;

    public SimulatedSocketFactory(HostNameResolver hostNameResolver, StreamManager streamManager) {
        super();
        assert hostNameResolver != null;
        assert streamManager != null;
        this.hostNameResolver = hostNameResolver;
        this.streamManager = streamManager;
    }

    public static <T extends Socket> void configure(T sock) {
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
        } catch (Exception e) {}
    }

    @Override
    public Socket createSocket(HttpParams httpParams) {
        //Ignoring httpParams
        //apparently it's only useful to pass through a SOCKS server
        //see: http://svn.apache.org/repos/asf/httpcomponents/httpclient/trunk/httpclient/src/examples/org/apache/http/examples/client/ClientExecuteSOCKS.java

        //creating an anonymous class deriving from socket
        //we just need to override methods for connect to get some metrics
        //and get-in-out streams to provide throttling
        Socket newSocket = new Socket() {
            @Override
            public void connect(SocketAddress endpoint) throws IOException {
                Date start = new Date();
                super.connect(endpoint);
                Date end = new Date();
                RequestInfo.get().connect(start, end);
            }
            @Override
            public void connect(SocketAddress endpoint, int timeout) throws IOException {
                Date start = new Date();
                super.connect(endpoint, timeout);
                Date end = new Date();
                RequestInfo.get().connect(start, end);
            }
            @Override
            public InputStream getInputStream() throws IOException {
                // whenever this socket is asked for its input stream
                // we get it ourselves via socket.getInputStream()
                // and register it to the stream manager so it will
                // automatically be throttled
                return streamManager.registerStream(super.getInputStream());
            }
            @Override
            public OutputStream getOutputStream() throws IOException {
                // whenever this socket is asked for its output stream
                // we get it ourselves via socket.getOutputStream()
                // and register it to the stream manager so it will
                // automatically be throttled
                return streamManager.registerStream(super.getOutputStream());
            }
        };
        SimulatedSocketFactory.configure(newSocket);
        return newSocket;
    }

    /**
     * Prevent unnecessary class inspection at runtime.
     */
    private static Method getHostStingOnInetSocketAddress; 
    static {
        try {
            getHostStingOnInetSocketAddress = InetSocketAddress.class.getDeclaredMethod("getHostString", new Class[]{});
        } catch (SecurityException e) {
            throw new RuntimeException("Expecting InetSocketAddress to have a package scoped \"getHostString\" method which returns a String and takes no input", e);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("Expecting InetSocketAddress to have a package scoped \"getHostString\" method which returns a String and takes no input", e);
        }
        getHostStingOnInetSocketAddress.setAccessible(true);
    }
    
    /**
     * A minor optimization to prevent possible host resolution when inspecting a InetSocketAddress for a hostname....
     * 
     * @param remoteAddress
     * @return
     * @throws IOException
     */
    private String resolveHostName(InetSocketAddress remoteAddress) {
        String hostString = null;
        try {
            hostString = (String) getHostStingOnInetSocketAddress.invoke(remoteAddress, new Object[]{});
        } catch (InvocationTargetException ite) {
            throw new RuntimeException("Expecting InetSocketAddress to have a package scoped \"getHostString\" method which returns a String and takes no input");
        } catch (IllegalAccessException iae) {
            throw new RuntimeException("Expecting InetSocketAddress to have a package scoped \"getHostString\" method which returns a String and takes no input");
        }
        return hostString;
    }

    @Override
    public Socket connectSocket(Socket sock, InetSocketAddress remoteAddress, InetSocketAddress localAddress, HttpParams params) throws IOException {
        if (remoteAddress == null) {
            throw new IllegalArgumentException("Target host may not be null.");
        }

        if (params == null) {
            throw new IllegalArgumentException("Parameters may not be null.");
        }

        if (sock == null) {
            sock = createSocket(null);
        }

        if ((localAddress != null) ) {
            sock.bind( localAddress );
        }

        //TODO: this has to be changed to HttpInetSocketAddress once we upgrade to 4.2.1
        InetSocketAddress remoteAddr = remoteAddress;
        if (this.hostNameResolver != null) {
            remoteAddr = new InetSocketAddress(this.hostNameResolver.resolve(resolveHostName(remoteAddress)), remoteAddress.getPort());
        }

        int timeout = HttpConnectionParams.getConnectionTimeout(params);

        try {
            sock.connect(remoteAddr, timeout);
        } catch (SocketTimeoutException ex) {
            throw new ConnectTimeoutException("Connect to " + remoteAddress + " timed out");
        }

        return sock;
    }

    /**
     * Checks whether a socket connection is secure. This factory creates plain socket connections which are not
     * considered secure.
     *
     * @param sock the connected socket
     * @return <code>false</code>
     * @throws IllegalArgumentException if the argument is invalid
     */
    @Override
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
}