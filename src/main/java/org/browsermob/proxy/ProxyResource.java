package org.browsermob.proxy;

import com.google.inject.Inject;
import org.browsermob.proxy.http.Transaction;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.xml.bind.annotation.XmlRootElement;

@Path("/proxy")
public class ProxyResource {
    private ProxyManager proxyManager;

    @Inject
    public ProxyResource(ProxyManager proxyManager) {
        this.proxyManager = proxyManager;
    }

    @POST
    @Produces({MediaType.APPLICATION_JSON})
    public ProxyDescriptor newProxy() throws Exception {
        ProxyServer proxy = proxyManager.create();
        int port = proxy.getPort();

        return new ProxyDescriptor(port);
    }

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @Path("/{port}/har")
    public Transaction getHar(@PathParam("port") int port) {
        ProxyServer proxy = proxyManager.get(port);
        return proxy.getTransaction();
    }

    @DELETE
    @Path("/{port}")
    public void delete(@PathParam("port") int port) throws Exception {
        proxyManager.delete(port);
    }

    @XmlRootElement
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
