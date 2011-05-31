package org.browsermob.proxy;

import org.apache.http.HttpRequestInterceptor;
import org.apache.http.HttpResponseInterceptor;
import org.browsermob.core.har.Har;
import org.browsermob.core.har.HarLog;
import org.browsermob.core.har.HarNameVersion;
import org.browsermob.core.har.HarPage;
import org.browsermob.proxy.http.BrowserMobHttpClient;
import org.browsermob.proxy.jetty.http.HttpContext;
import org.browsermob.proxy.jetty.http.SocketListener;
import org.browsermob.proxy.jetty.jetty.Server;
import org.browsermob.proxy.jetty.util.InetAddrPort;
import org.openqa.selenium.Proxy;

import java.util.Date;


public class ProxyServer {
    private static final HarNameVersion CREATOR = new HarNameVersion("BrowserMob Proxy", "2.0");

    private Server server;
    private int port = -1;
    private BrowserMobHttpClient client;
    private HarPage currentPage;
    private BrowserMobProxyHandler handler;

    public ProxyServer() {
    }

    public ProxyServer(int port) {
        this.port = port;
    }

    public void start() throws Exception {
        if (port == -1) {
            throw new IllegalStateException("Must set port before starting");
        }

        server = new Server();
        server.addListener(new SocketListener(new InetAddrPort(getPort()))); // todo: arg?
        HttpContext context = new HttpContext();
        context.setContextPath("/");
        server.addContext(context);

        handler = new BrowserMobProxyHandler();
        handler.setShutdownLock(new Object());
        client = new BrowserMobHttpClient();
        client.prepareForBrowser();
        handler.setHttpClient(client);
        client.setDownstreamKbps(500 * 1024 * 8);

        context.addHandler(handler);

        server.start();
    }

    public org.openqa.selenium.Proxy seleniumProxy() {
        Proxy proxy = new Proxy();
        proxy.setProxyType(Proxy.ProxyType.MANUAL);
        proxy.setHttpProxy("localhost:4444");
        proxy.setSslProxy("localhost:4444");

        return proxy;
    }

    public void cleanup() {
        handler.cleanup();
    }

    public void stop() throws Exception {
        cleanup();
        client.shutdown();
        server.stop();
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public Har getHar() {
        return client.getHar();
    }

    public Har newHar(String initialPageRef) {
        Har oldHar = getHar();

        Har har = new Har(new HarLog(CREATOR));
        client.setHar(har);
        newPage(initialPageRef);

        return oldHar;
    }

    public void newPage(String pageRef) {
        client.setHarPageRef(pageRef);
        currentPage = new HarPage(pageRef);
        client.getHar().getLog().addPage(currentPage);
    }

    public void endPage() {
        if (currentPage == null) {
            return;
        }

        currentPage.getPageTimings().setOnLoad(new Date().getTime() - currentPage.getStartedDateTime().getTime());
        client.setHarPageRef(null);
        currentPage = null;
    }

    public void setRetryCount(int count) {
        client.setRetryCount(count);
    }

    public void remapHost(String source, String target) {
        client.remapHost(source, target);
    }

    public void addRequestInterceptor(HttpRequestInterceptor i) {
        client.addRequestInterceptor(i);
    }

    public void addResponseInterceptor(HttpResponseInterceptor i) {
        client.addResponseInterceptor(i);
    }

    public void setDownstreamKbps(long downstreamKbps) {
        client.setDownstreamKbps(downstreamKbps);
    }

    public void setUpstreamKbps(long upstreamKbps) {
        client.setUpstreamKbps(upstreamKbps);
    }

    public void setLatency(long latency) {
        client.setLatency(latency);
    }

    public void setRequestTimeout(int requestTimeout) {
        client.setRequestTimeout(requestTimeout);
    }

    public void setSocketOperationTimeout(int readTimeout) {
        client.setSocketOperationTimeout(readTimeout);
    }

    public void setConnectionTimeout(int connectionTimeout) {
        client.setConnectionTimeout(connectionTimeout);
    }

    public void autoBasicAuthorization(String domain, String username, String password) {
        client.autoBasicAuthorization(domain, username, password);
    }

    public void rewriteUrl(String match, String replace) {
        client.rewriteUrl(match, replace);
    }

    public void blacklistRequests(String pattern, int responseCode) {
        client.blacklistRequests(pattern, responseCode);
    }

    public void whitelistRequests(String[] patterns, int responseCode) {
        client.whitelistRequests(patterns, responseCode);
    }

    public void setCaptureHeaders(boolean captureHeaders) {
        client.setCaptureHeaders(captureHeaders);
    }

    public void setCaptureContent(boolean captureContent) {
        client.setCaptureContent(captureContent);
    }

    public void clearDNSCache() {
        client.clearDNSCache();
    }

    public void setDNSCacheTimeout(int timeout) {
        client.setDNSCacheTimeout(timeout);
    }

    public void waitForNetworkTrafficToStop(final long quietPeriodInMs, long timeoutInMs) {
        // todo: need to implement
    }

}
