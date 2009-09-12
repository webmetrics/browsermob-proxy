package org.browsermob.proxy;

import org.browsermob.proxy.jetty.http.HttpContext;
import org.browsermob.proxy.jetty.http.SocketListener;
import org.browsermob.proxy.jetty.http.handler.ProxyHandler;
import org.browsermob.proxy.jetty.jetty.Server;
import org.browsermob.proxy.jetty.util.InetAddrPort;

public class ProxyMain {
    public static void main(String[] args) throws Exception {
        Server server = new Server();
        server.addListener(new SocketListener(new InetAddrPort(9638)));
        HttpContext context = new HttpContext();
        context.setContextPath("/");
        ProxyHandler handler = new ProxyHandler();
        context.addHandler(handler);
        server.addContext(context);
        server.start();
    }
}
