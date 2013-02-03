package org.browsermob.proxy;

import org.browsermob.proxy.jetty.http.HttpContext;
import org.browsermob.proxy.jetty.http.HttpListener;
import org.browsermob.proxy.jetty.http.SocketListener;
import org.browsermob.proxy.jetty.http.handler.ResourceHandler;
import org.browsermob.proxy.jetty.jetty.Server;
import org.browsermob.proxy.jetty.jetty.servlet.ServletHttpContext;
import org.browsermob.proxy.jetty.util.InetAddrPort;
import org.browsermob.proxy.jetty.util.Resource;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class DummyServer {
    private int port;
    private Server server = new Server();
    private ResourceHandler handler;

    public DummyServer(int port) {
        this.port = port;
    }

    public void start() throws Exception {
        HttpListener listener = new SocketListener(new InetAddrPort(port));
        
        server.addListener(listener);
        ServletHttpContext servletContext = new ServletHttpContext();
        servletContext.setContextPath("/jsonrpc/");
        servletContext.addServlet("/", "org.browsermob.proxy.DummyServer$JsonServlet");
        server.addContext(servletContext);

        HttpContext context = new HttpContext();
        context.setContextPath("/");
        context.setBaseResource(Resource.newResource("src/test/dummy-server"));
        server.addContext(context);
        handler = new ResourceHandler();
        context.addHandler(handler);

        server.start();
    }
    
    public ResourceHandler getHandler() {
        return handler;
    }

    public void stop() throws InterruptedException {
        server.stop();
    }
    
    @SuppressWarnings("serial")
	public static class JsonServlet extends HttpServlet
	{
		@Override
		protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException
		{
			doPost(request, response);
		}

		@Override
		protected void doPost(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws IOException
	    {
	        httpServletResponse.setContentType("application/json-rpc");
	        PrintWriter out = httpServletResponse.getWriter();
	        out.println("{\"jsonrpc\":\"2.0\",\"id\":1,\"result\":{}}");
	        out.close();
	    }

	}

}
