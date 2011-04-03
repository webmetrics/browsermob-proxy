package org.browsermob.proxy;

import org.browsermob.proxy.http.BrowserMobHttpClient;
import org.browsermob.proxy.http.Transaction;
import org.browsermob.proxy.http.TransactionStep;
import org.browsermob.proxy.jetty.http.HttpContext;
import org.browsermob.proxy.jetty.http.SocketListener;
import org.browsermob.proxy.jetty.jetty.Server;
import org.browsermob.proxy.jetty.util.InetAddrPort;


public class ProxyServer {
    private Server server;
    private int port = -1;
    private BrowserMobHttpClient client;

    public void start() throws Exception {
        if (port == -1) {
            throw new IllegalStateException("Must set port before starting");
        }

        server = new Server();
        server.addListener(new SocketListener(new InetAddrPort(getPort()))); // todo: arg?
        HttpContext context = new HttpContext();
        context.setContextPath("/");
        server.addContext(context);

        BrowserMobProxyHandler handler = new BrowserMobProxyHandler();
        handler.setShutdownLock(new Object());
        client = new BrowserMobHttpClient(null);
        Transaction transaction = new Transaction();
        client.setActiveTransaction(transaction);
        TransactionStep step = new TransactionStep();
        transaction.addStep(step);
        client.setActiveStep(step);
        client.prepareForBrowser();
        handler.setHttpClient(client);
        client.setDownstreamKbps(500 * 1024 * 8);

        context.addHandler(handler);

        server.start();
    }

    public void stop() throws Exception {
        server.stop();
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public Transaction getTransaction() {
        return client.getActiveTransaction();
    }
}
