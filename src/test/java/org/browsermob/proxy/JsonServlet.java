package org.browsermob.proxy;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@SuppressWarnings("serial")
public class JsonServlet extends HttpServlet
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
