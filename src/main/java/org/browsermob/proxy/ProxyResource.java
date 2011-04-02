package org.browsermob.proxy;

import com.google.inject.Inject;

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
        int port = proxyManager.create();
        return new ProxyDescriptor(port);
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
