package org.browsermob.proxy;

import org.browsermob.core.har.Har;
import org.browsermob.core.har.HarLog;
import org.browsermob.core.har.HarNameVersion;
import org.browsermob.core.har.HarPage;
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
        client.setHar(new Har(new HarLog()));
        client.getHar().getLog().setCreator(new HarNameVersion("BrowserMob Proxy", "2.0"));
        client.getHar().getLog().addPage(new HarPage("PageRef_1"));
        client.setHarPageRef("PageRef_1");
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

    public Har getHar() {
        return client.getHar();
    }
}
