package org.browsermob.proxy.bricks;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.google.sitebricks.At;
import com.google.sitebricks.client.transport.Json;
import com.google.sitebricks.headless.Reply;
import com.google.sitebricks.headless.Request;
import com.google.sitebricks.headless.Service;
import com.google.sitebricks.http.Delete;
import com.google.sitebricks.http.Get;
import com.google.sitebricks.http.Post;
import com.google.sitebricks.http.Put;
import org.browsermob.core.har.Har;
import org.browsermob.proxy.ProxyManager;
import org.browsermob.proxy.ProxyServer;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.protocol.HttpContext;
import org.apache.http.HttpRequest;

import java.util.Hashtable;
import java.util.Map;
import java.util.Set;
import java.util.List;

@At("/proxy")
@Service
public class ProxyResource {
    private ProxyManager proxyManager;

    @Inject
    public ProxyResource(ProxyManager proxyManager) {
        this.proxyManager = proxyManager;
    }

    @Post
    public Reply<ProxyDescriptor> newProxy(Request request) throws Exception {
        String httpProxy = request.param("httpProxy");
        Hashtable<String, String> options = new Hashtable<String, String>();

        if (httpProxy != null) {
            options.put("httpProxy", httpProxy);
        }

        String paramPort = request.param("port");
        int port = 0;
        if (paramPort != null) {
            port = Integer.parseInt(paramPort);
            ProxyServer proxy = proxyManager.create(options, port);
        } else {
            ProxyServer proxy = proxyManager.create(options);
            port = proxy.getPort();
        }
        return Reply.with(new ProxyDescriptor(port)).as(Json.class);
    }

    @Get
    @At("/:port/har")
    public Reply<Har> getHar(@Named("port") int port) {
        ProxyServer proxy = proxyManager.get(port);
        Har har = proxy.getHar();

        return Reply.with(har).as(Json.class);
    }

    @Put
    @At("/:port/har")
    public Reply<?> newHar(@Named("port") int port, Request request) {
        String initialPageRef = request.param("initialPageRef");
        ProxyServer proxy = proxyManager.get(port);
        Har oldHar = proxy.newHar(initialPageRef);

        String captureHeaders = request.param("captureHeaders");
        String captureContent = request.param("captureContent");
        proxy.setCaptureHeaders(Boolean.parseBoolean(captureHeaders));
        proxy.setCaptureContent(Boolean.parseBoolean(captureContent));

        if (oldHar != null) {
            return Reply.with(oldHar).as(Json.class);
        } else {
            return Reply.saying().noContent();
        }
    }

    @Put
    @At("/:port/har/pageRef")
    public Reply<?> setPage(@Named("port") int port, Request request) {
        String pageRef = request.param("pageRef");
        ProxyServer proxy = proxyManager.get(port);
        proxy.newPage(pageRef);

        return Reply.saying().ok();
    }

    @Put
    @At("/:port/blacklist")
    public Reply<?> blacklist(@Named("port") int port, Request request) {
        String blacklist = request.param("regex");
        int responseCode = parseResponseCode(request.param("status"));
        ProxyServer proxy = proxyManager.get(port);
        proxy.blacklistRequests(blacklist, responseCode);

        return Reply.saying().ok();
    }

    @Put
    @At("/:port/whitelist")
    public Reply<?> whitelist(@Named("port") int port, Request request) {
        String regex = request.param("regex");
        int responseCode = parseResponseCode(request.param("status"));
        ProxyServer proxy = proxyManager.get(port);
        proxy.whitelistRequests(regex.split(","), responseCode);

        return Reply.saying().ok();
    }

    @Post
    @At("/:port/headers")
    public Reply<?> updateHeaders(@Named("port") int port, Request request) {
        ProxyServer proxy = proxyManager.get(port);
        Map<String, String> headers = request.read(Map.class).as(Json.class);
        for (Map.Entry<String, String> entry : headers.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            proxy.addHeader(key, value);
        }
        return Reply.saying().ok();

    @Put
    @At("/:port/limit")
    public Reply<?> limit(@Named("port") int port, Request request) {
        ProxyServer proxy = proxyManager.get(port);
        String upstreamKbps = request.param("upstreamKbps");
        if (upstreamKbps != null) {
            try {
                proxy.setUpstreamKbps(Integer.parseInt(upstreamKbps));
            } catch (NumberFormatException e) { }
        }
        String downstreamKbps = request.param("downstreamKbps");
        if (downstreamKbps != null) {
            try {
                proxy.setDownstreamKbps(Integer.parseInt(downstreamKbps));
            } catch (NumberFormatException e) { }
        }
        String latency = request.param("latency");
        if (latency != null) {
            try {
                proxy.setLatency(Integer.parseInt(latency));
            } catch (NumberFormatException e) { }
        }
        return Reply.saying().ok();
    }

    @Delete
    @At("/:port")
    public Reply<?> delete(@Named("port") int port) throws Exception {
        proxyManager.delete(port);
        return Reply.saying().ok();
    }

    private int parseResponseCode(String response)
    {
        int responseCode = 200;
        if (response != null) {
            try {
                responseCode = Integer.parseInt(response);
            } catch (NumberFormatException e) { }
        }
        return responseCode;
    }

    public static class ProxyDescriptor {
        private int port;

        public ProxyDescriptor() {
        }

        public ProxyDescriptor(int port) {
            this.port = port;
        }

        public int getPort() {
            return port;
        }

        public void setPort(int port) {
            this.port = port;
        }
    }
}
