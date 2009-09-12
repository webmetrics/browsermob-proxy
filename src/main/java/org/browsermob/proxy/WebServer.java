package org.browsermob.proxy;

import org.mortbay.jetty.Server;
import org.mortbay.jetty.nio.SelectChannelConnector;
import org.mortbay.jetty.webapp.WebAppContext;

public class WebServer {
    public static void start() throws Exception {
        Server server = new Server();

        SelectChannelConnector connector = new SelectChannelConnector();
        connector.setPort(8081);
        server.addConnector(connector);

        WebAppContext webApp = new WebAppContext("src/main/webapp", "/");
        webApp.setContextPath("/");
        server.addHandler(webApp);
        server.start();
    }
}
