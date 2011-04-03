package org.browsermob.proxy;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceServletContextListener;
import com.google.inject.servlet.ServletModule;
import com.sun.jersey.api.json.JSONConfiguration;
import com.sun.jersey.guice.spi.container.servlet.GuiceContainer;
import org.browsermob.proxy.guice.ConfigModule;
import org.browsermob.proxy.guice.JettyModule;
import org.browsermob.proxy.util.Log;
import org.codehaus.jackson.jaxrs.JacksonJsonProvider;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;

import javax.servlet.ServletContextEvent;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.MessageBodyWriter;
import java.util.HashMap;
import java.util.Map;

public class Main {
    private static final Log LOG = new Log();

    public static void main(String[] args) throws Exception {
        LOG.info("Starting up...");

        final Injector injector = Guice.createInjector(new ConfigModule(args), new JettyModule(), new ServletModule() {
            @Override
            protected void configureServlets() {
                bind(ProxyResource.class);

                /* bind jackson converters for JAXB/JSON serialization */
                bind(MessageBodyReader.class).to(JacksonJsonProvider.class);
                bind(MessageBodyWriter.class).to(JacksonJsonProvider.class);

                Map<String, String> params = new HashMap<String, String>();
                params.put(JSONConfiguration.FEATURE_POJO_MAPPING, "true");

                serve("*").with(GuiceContainer.class, params);

            }
        });

        Server server = injector.getInstance(Server.class);
        GuiceServletContextListener gscl = new GuiceServletContextListener() {
            @Override
            protected Injector getInjector() {
                return injector;
            }
        };
        server.start();

        ServletContextHandler context = (ServletContextHandler) server.getHandler();
        gscl.contextInitialized(new ServletContextEvent(context.getServletContext()));

        server.join();
    }
}
