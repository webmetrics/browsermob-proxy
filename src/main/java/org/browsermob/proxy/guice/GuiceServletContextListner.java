package org.browsermob.proxy.guice;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Stage;
import org.browsermob.proxy.util.Log;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class GuiceServletContextListner implements ServletContextListener {
    private static final Log LOG = new Log();
    private static Injector injector;

    public void contextInitialized(ServletContextEvent sce) {
        synchronized (GuiceServletContextListner.class) {
            if (injector == null) {
                ProxyModule module = new ProxyModule();
                injector = Guice.createInjector(Stage.DEVELOPMENT, module);

                try {
                    injector.getInstance(Initialization.class).init();
                } catch (Exception e) {
                    throw LOG.severeAndRethrow("Unexpected exception when starting up", e);
                }
            }
        }
    }

    public void contextDestroyed(ServletContextEvent sce) {
    }

    public static Injector getInjector() {
        return injector;
    }

    public static <T> T newInstance(Class<T> clazz) {
        if (injector != null) {
            return injector.getInstance(clazz);
        } else {
            try {
                return clazz.newInstance();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

}
