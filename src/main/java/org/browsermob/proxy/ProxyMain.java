package org.browsermob.proxy;

import org.browsermob.proxy.util.Log;

public class ProxyMain {
    private static final Log LOG = new Log();

    public static void main(String[] args) throws Exception {
        LOG.info("Starting up BrowserMob Proxy");
        WebServer.start();
    }
}
