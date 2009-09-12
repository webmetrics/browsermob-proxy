package org.browsermob.proxy;

import org.browsermob.proxy.jetty.http.HttpContext;
import org.browsermob.proxy.jetty.http.SocketListener;
import org.browsermob.proxy.jetty.jetty.Server;
import org.browsermob.proxy.jetty.util.InetAddrPort;

public class ProxyServer {
    public static void start() throws Exception {
        Server server = new Server();
        server.addListener(new SocketListener(new InetAddrPort(9638)));
        HttpContext context = new HttpContext();
        context.setContextPath("/");
        BrowserMobProxyHandler handler = new BrowserMobProxyHandler();
        handler.setShutdownLock(new Object());
        context.addHandler(handler);
        server.addContext(context);
        server.start();
    }
}
