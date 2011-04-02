package org.browsermob.proxy.guice;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.name.Named;
import com.google.inject.servlet.GuiceFilter;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;

public class JettyServerProvider implements Provider<Server> {

    private Server server;

    @Inject
    public JettyServerProvider(@Named("port") int port) {
        server = new Server(port);

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");

        context.addFilter(GuiceFilter.class, "/*", 0);
        context.addServlet(DefaultServlet.class, "/");

        server.setHandler(context);
    }

    @Override
    public Server get() {
        return server;
    }
}
