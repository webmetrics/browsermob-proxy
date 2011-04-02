package org.browsermob.proxy.guice;

import com.google.inject.Binder;
import com.google.inject.Module;
import org.eclipse.jetty.server.Server;

public class JettyModule implements Module {
    @Override
    public void configure(Binder binder) {
        binder.bind(Server.class).toProvider(JettyServerProvider.class);
    }
}
