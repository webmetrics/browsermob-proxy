package org.browsermob.proxy;

import net.jcip.annotations.GuardedBy;

import com.google.inject.Singleton;
import org.browsermob.proxy.jetty.http.HttpContext;
import org.browsermob.proxy.jetty.http.SocketListener;
import org.browsermob.proxy.jetty.jetty.Server;
import org.browsermob.proxy.jetty.util.InetAddrPort;
import org.directwebremoting.annotations.RemoteMethod;
import org.directwebremoting.annotations.RemoteProxy;

import java.util.ArrayList;
import java.util.List;


@Singleton
@RemoteProxy
public class ProxyServer {
    private Server server;
    private int bandwidth = 500; // KB/sec
    private BrowserMobProxyHandler handler;
    private final int MAXBLOCKS = 100;
    private int port = 9638;
    private ProxyServerLog serverLog = new ProxyServerLog(MAXBLOCKS);

    public void start() throws Exception {
        server = new Server();
        server.addListener(new SocketListener(new InetAddrPort(getPort()))); // todo: arg?
        HttpContext context = new HttpContext();
        context.setContextPath("/");
        server.addContext(context);

        handler = new BrowserMobProxyHandler();
        handler.setShutdownLock(new Object());
        handler.setSimulatedBps(bandwidth * 1024 * 8);
        handler.setProxyServer(this);
        
        context.addHandler(handler);

        clearBlocks();

        server.start();
    }

    public void stop() throws Exception {
        server.stop();
    }

    @RemoteMethod
    public void clearBlocks() {
        serverLog.clearRecentBlocks();
    }

    @RemoteMethod
    public int getBandwidth() {
        return bandwidth;
    }

    @RemoteMethod
    public void setBandwidth(int bandwidth) {
        handler.setSimulatedBps(bandwidth * 1024 * 8); // convert to BPS
        this.bandwidth = bandwidth;
    }

    @RemoteMethod
    public List<Block> getBlocks() {
        return serverLog.getRecentBlocks();
    }

    @RemoteMethod
    public synchronized List<Block> getLastNBlocks(int n) {
        return serverLog.getLastNRecentBlocks(n);
    }

    public void record(HttpObject httpObject) {
        serverLog.record(httpObject);
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    @RemoteMethod
    public void setMockResponse(MockResponse mockResponse) {
        handler.setMockResponse(mockResponse);
    }

    @RemoteMethod
    public MockResponse getMockResponse() {
        return handler.getMockResponse();
    }


    public ProxyServerLog getServerLog()  {
        // returning mutable reference on purpose
        return serverLog;
    }
}
