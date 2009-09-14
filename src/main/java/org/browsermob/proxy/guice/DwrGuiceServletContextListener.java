package org.browsermob.proxy.guice;

import com.google.inject.Injector;
import org.directwebremoting.guice.CustomInjectorServletContextListener;

public class DwrGuiceServletContextListener extends CustomInjectorServletContextListener {
    @Override
    protected Injector createInjector() {
        return GuiceServletContextListner.getInjector();
    }
}
