package org.browsermob.proxy;

import com.google.inject.Inject;

public class ProxyController {
    private ProxyServer proxyServer;
    private int bandwidth = 500;

    @Inject
    public ProxyController(ProxyServer proxyServer) {
        this.proxyServer = proxyServer;
    }

    public int getBandwidth() {
        return bandwidth;
    }

    public void setBandwidth(int kbPerSec) {
        bandwidth = kbPerSec;
    }
}
