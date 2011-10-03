package org.browsermob.proxy.http;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.client.CookieStore;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.conn.ManagedClientConnection;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.cookie.Cookie;
import org.apache.http.cookie.CookieOrigin;
import org.apache.http.cookie.CookieSpec;
import org.apache.http.cookie.CookieSpecRegistry;
import org.apache.http.protocol.ExecutionContext;
import org.apache.http.protocol.HttpContext;
import org.browsermob.proxy.util.Log;

/**
 * Modified version of org.apache.http.client.protocol.RequestAddCookies
 * Performs the same functionality used to build the Cookie header and saves request cookies to 
 * the context with key "browsermob.http.request.cookies".
 * 
 * @author dgomez
 *
 */
public class RequestCookiesInterceptor implements HttpRequestInterceptor {
	
	private static final Log LOG = new Log(); 

	@Override
    public void process(HttpRequest request, HttpContext context) {    	    	
    	
        String method = request.getRequestLine().getMethod();
        if (method.equalsIgnoreCase("CONNECT")) {
            return;
        }

        // Obtain cookie store
        CookieStore cookieStore = (CookieStore) context.getAttribute(
                ClientContext.COOKIE_STORE);
        if (cookieStore == null) {
            LOG.fine("Cookie store not specified in HTTP context");
            return;
        }

        // Obtain the registry of cookie specs
        CookieSpecRegistry registry = (CookieSpecRegistry) context.getAttribute(
                ClientContext.COOKIESPEC_REGISTRY);
        if (registry == null) {
            LOG.fine("CookieSpec registry not specified in HTTP context");
            return;
        }

        // Obtain the target host (required)
        HttpHost targetHost = (HttpHost) context.getAttribute(
                ExecutionContext.HTTP_TARGET_HOST);
        if (targetHost == null) {
            LOG.fine("Target host not set in the context");
            return;
        }

        // Obtain the client connection (required)
        ManagedClientConnection conn = (ManagedClientConnection) context.getAttribute(
                ExecutionContext.HTTP_CONNECTION);
        if (conn == null) {
            LOG.fine("HTTP connection not set in the context");
            return;
        }

        String policy = HttpClientParams.getCookiePolicy(request.getParams());
        LOG.fine("CookieSpec selected: " + policy);

        URI requestURI;
        if (request instanceof HttpUriRequest) {
            requestURI = ((HttpUriRequest) request).getURI();
        } else {
            try {
                requestURI = new URI(request.getRequestLine().getUri());
            } catch (URISyntaxException ex) {
                LOG.fine("Invalid request URI: " + request.getRequestLine().getUri(), ex);
                return;
            }
        }

        String hostName = targetHost.getHostName();
        int port = targetHost.getPort();
        if (port < 0) {
        	
        	//Obtain the scheme registry
        	SchemeRegistry sr = (SchemeRegistry) context.getAttribute(
        			ClientContext.SCHEME_REGISTRY);
        	if (sr != null) {
        		Scheme scheme = sr.get(targetHost.getSchemeName());
        		port = scheme.resolvePort(port);
        	} 
        	else {
        		port = conn.getRemotePort();
        	}
        }

        CookieOrigin cookieOrigin = new CookieOrigin(
                hostName,
                port,
                requestURI.getPath(),
                conn.isSecure());

        // Get an instance of the selected cookie policy
        CookieSpec cookieSpec = registry.getCookieSpec(policy, request.getParams());
        // Get all cookies available in the HTTP state
        List<Cookie> cookies = new ArrayList<Cookie>(cookieStore.getCookies());
        // Find cookies matching the given origin
        List<Cookie> matchedCookies = new ArrayList<Cookie>();
        Date now = new Date();
        for (Cookie cookie : cookies) {
            if (!cookie.isExpired(now)) {
                if (cookieSpec.match(cookie, cookieOrigin)) {
                    LOG.fine("Cookie " + cookie + " match " + cookieOrigin);
                    matchedCookies.add(cookie);
                }
            } else {
            	LOG.fine("Cookie " + cookie + " expired");
            }
        }
        context.setAttribute("browsermob.http.request.cookies", matchedCookies);
    }

}
