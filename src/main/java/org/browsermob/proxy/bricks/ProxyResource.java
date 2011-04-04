package org.browsermob.proxy.bricks;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.google.sitebricks.At;
import com.google.sitebricks.client.transport.Json;
import com.google.sitebricks.headless.Reply;
import com.google.sitebricks.headless.Service;
import com.google.sitebricks.http.Delete;
import com.google.sitebricks.http.Get;
import com.google.sitebricks.http.Post;
import org.browsermob.core.har.Har;
import org.browsermob.proxy.ProxyManager;
import org.browsermob.proxy.ProxyServer;

@At("/proxy")
@Service
public class ProxyResource {
    private ProxyManager proxyManager;

    @Inject
    public ProxyResource(ProxyManager proxyManager) {
        this.proxyManager = proxyManager;
    }

    @Post
    public Reply<ProxyDescriptor> newProxy() throws Exception {
        ProxyServer proxy = proxyManager.create();
        int port = proxy.getPort();

        return Reply.with(new ProxyDescriptor(port)).as(Json.class);
    }

    @Get
    @At("/:port/har")
    public Reply<Har> getHar(@Named("port") int port) {
        ProxyServer proxy = proxyManager.get(port);
        Har har = proxy.getHar();

        return Reply.with(proxy.getHar()).as(Json.class);
    }

    @Delete
    @At("/:port")
    public void delete(@Named("port") int port) throws Exception {
        proxyManager.delete(port);
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
