package org.browsermob.proxy;

import com.google.inject.Inject;
import org.eclipse.jetty.server.Server;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.xml.bind.annotation.XmlRootElement;

@Path("/sample/{who}")
public class SampleResource {

    Server server;

    @Inject
    public SampleResource(Server server) {
        this.server = server;
    }

    @GET
//    @Produces("text/plain")
    @Produces({MediaType.APPLICATION_JSON})
    public Greeting sayGreeting(@PathParam("who") String name) {
//        return "Greetings, " + name + ", you're listening on port " + server.getConnectors()[0].getPort() + "!";
        return new Greeting(name);
    }

    /**
     * A static inner class result object; it's only "static inner" for
     * simplicity - really, this could be any POJO bean that you desire.
     */
    @XmlRootElement
    public static class Greeting {
        private String name;

        public Greeting() {
        }

        public Greeting(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

}
