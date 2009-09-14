package org.browsermob.proxy.guice;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.browsermob.proxy.ProxyServer;

@Singleton
public class Initialization {
    private ProxyServer proxyServer;

    @Inject
    public Initialization(ProxyServer proxyServer) {
        this.proxyServer = proxyServer;
    }

    public void init() throws Exception {
        proxyServer.start();
    }
}
