package org.browsermob.proxy;

import com.google.inject.Inject;
import org.directwebremoting.annotations.RemoteMethod;
import org.directwebremoting.annotations.RemoteProxy;

@RemoteProxy
public class ProxyController {
    private ProxyServer proxyServer;
    private int bandwidth = 500;

    @Inject
    public ProxyController(ProxyServer proxyServer) {
        this.proxyServer = proxyServer;
    }

    @RemoteMethod
    public int getBandwidth() {
        return bandwidth;
    }

    @RemoteMethod
    public void setBandwidth(int kbPerSec) {
        bandwidth = kbPerSec;
    }
}
