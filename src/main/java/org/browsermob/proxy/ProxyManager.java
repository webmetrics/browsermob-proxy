package org.browsermob.proxy;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.Provider;

import java.util.Hashtable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Singleton
public class ProxyManager {
    private AtomicInteger portCounter = new AtomicInteger(9090);
    private Provider<ProxyServer> proxyServerProvider;
    private Map<Integer, ProxyServer> proxies = new ConcurrentHashMap<Integer, ProxyServer>();

    @Inject
    public ProxyManager(Provider<ProxyServer> proxyServerProvider) {
        this.proxyServerProvider = proxyServerProvider;
    }

    public ProxyServer create(Map<String, String> options, int port) throws Exception {
        ProxyServer proxy = proxyServerProvider.get();
        proxy.setPort(port);
        proxy.start();
        proxy.setOptions(options);
        proxies.put(port, proxy);
        return proxy;
    }

    public ProxyServer create(Map<String, String> options) throws Exception {
        int port = portCounter.incrementAndGet();
        ProxyServer proxy = proxyServerProvider.get();

        proxy.setPort(port);
        proxy.start();
        proxy.setOptions(options);

        proxies.put(port, proxy);

        return proxy;
    }

    public ProxyServer get(int port) {
        return proxies.get(port);
    }

    public void delete(int port) throws Exception {
        ProxyServer proxy = proxies.remove(port);
        proxy.stop();
    }
}
