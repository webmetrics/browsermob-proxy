package org.browsermob.proxy;

import org.browsermob.proxy.jetty.http.HttpContext;
import org.browsermob.proxy.jetty.http.HttpListener;
import org.browsermob.proxy.jetty.http.SocketListener;
import org.browsermob.proxy.jetty.http.handler.ResourceHandler;
import org.browsermob.proxy.jetty.jetty.Server;
import org.browsermob.proxy.jetty.util.InetAddrPort;
import org.browsermob.proxy.jetty.util.Resource;

import java.io.IOException;

public class DummyServer {
    private int port;
    private Server server = new Server();

    public DummyServer(int port) {
        this.port = port;
    }

    public void start() throws Exception {
        HttpListener listener = new SocketListener(new InetAddrPort(port));
        server.addListener(listener);
        HttpContext context = new HttpContext();
        context.setContextPath("/");
        context.setBaseResource(Resource.newResource("src/test/dummy-server"));
        server.addContext(context);
        context.addHandler(new ResourceHandler());

        server.start();
    }

    public void stop() throws InterruptedException {
        server.stop();
    }
}
