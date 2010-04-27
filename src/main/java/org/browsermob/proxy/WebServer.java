package org.browsermob.proxy;

import org.mortbay.jetty.Server;
import org.mortbay.jetty.nio.SelectChannelConnector;
import org.mortbay.jetty.webapp.WebAppContext;

import java.io.File;
import java.net.URL;

public class WebServer {
    public static void start(int port) throws Exception {
        Server server = new Server();

        SelectChannelConnector connector = new SelectChannelConnector();
        connector.setPort(port);
        server.addConnector(connector);

        String path = "src/main/webapp";
        if (!new File(path).exists()) {
            URL baseUrl = WebServer.class.getResource("/src/main/webapp");
            path = baseUrl.toString() + "/";
        }

        WebAppContext webApp = new WebAppContext(path, "/");
        webApp.setContextPath("/");
        server.addHandler(webApp);
        server.start();
    }
}
