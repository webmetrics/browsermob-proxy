package org.browsermob.proxy;

import org.mortbay.jetty.Server;
import org.mortbay.jetty.nio.SelectChannelConnector;
import org.mortbay.jetty.webapp.WebAppContext;

import java.net.URL;

public class WebServer {
    public static void start(int port) throws Exception {
        Server server = new Server();

        SelectChannelConnector connector = new SelectChannelConnector();
        connector.setPort(port);
        server.addConnector(connector);

        URL baseUrl = WebServer.class.getResource("/src/main/webapp");
        WebAppContext webApp = new WebAppContext(baseUrl.toString(), "/");
        webApp.setContextPath("/");
        server.addHandler(webApp);
        server.start();
    }
}
