package org.browsermob.proxy.http;

import java.io.IOException;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.Header;
import org.apache.http.HeaderIterator;
import org.apache.http.HttpException;
import org.apache.http.HttpResponse;
import org.apache.http.HttpResponseInterceptor;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.cookie.Cookie;
import org.apache.http.cookie.CookieOrigin;
import org.apache.http.cookie.CookieSpec;
import org.apache.http.cookie.MalformedCookieException;
import org.apache.http.cookie.SM;
import org.apache.http.protocol.HttpContext;

import com.google.common.collect.Lists;

/**
 * Modified version of org.apache.http.client.protocol.ResponseProcessCookies
 * Performs the same functionality used to parse the Set-Cookie header and saves response cookies to 
 * the context with key "browsermob.http.response.cookies".
 * 
 * @author dgomez
 *
 */
public class ResponseCookiesInterceptor implements HttpResponseInterceptor {

    private final Log log = LogFactory.getLog(getClass());
    
    public ResponseCookiesInterceptor() {
        super();
    }
    
    public void process(final HttpResponse response, final HttpContext context) 
            throws HttpException, IOException {
        if (response == null) {
            throw new IllegalArgumentException("HTTP request may not be null");
        }
        if (context == null) {
            throw new IllegalArgumentException("HTTP context may not be null");
        }
        
        // Obtain actual CookieSpec instance
        CookieSpec cookieSpec = (CookieSpec) context.getAttribute(
                ClientContext.COOKIE_SPEC);
        if (cookieSpec == null) {
            return;
        }

        // Obtain actual CookieOrigin instance
        CookieOrigin cookieOrigin = (CookieOrigin) context.getAttribute(
                ClientContext.COOKIE_ORIGIN);
        if (cookieOrigin == null) {
            this.log.info("CookieOrigin not available in HTTP context");
            return;
        }
        HeaderIterator it = response.headerIterator(SM.SET_COOKIE);
        List<Cookie> responseCookies = processCookies(it, cookieSpec, cookieOrigin);
        
        // see if the cookie spec supports cookie versioning.
        if (cookieSpec.getVersion() > 0) {
            // process set-cookie2 headers.
            // Cookie2 will replace equivalent Cookie instances
            it = response.headerIterator(SM.SET_COOKIE2);
            responseCookies.addAll(processCookies(it, cookieSpec, cookieOrigin));
        }
        
        context.setAttribute("browsermob.http.response.cookies", responseCookies);
    }
     
    private List<Cookie> processCookies(
            final HeaderIterator iterator, 
            final CookieSpec cookieSpec,
            final CookieOrigin cookieOrigin) {
    	
    	List<Cookie> valid = Lists.newLinkedList();
        while (iterator.hasNext()) {
            Header header = iterator.nextHeader();
            try {
                List<Cookie> cookies = cookieSpec.parse(header, cookieOrigin);                
                for (Cookie cookie : cookies) {
                    try {
                        cookieSpec.validate(cookie, cookieOrigin);
                        valid.add(cookie);

                        if (this.log.isDebugEnabled()) {
                            this.log.debug("Cookie accepted: \""
                                    + cookie + "\". ");
                        }
                    } catch (MalformedCookieException ex) {
                        if (this.log.isWarnEnabled()) {
                            this.log.warn("Cookie rejected: \""
                                    + cookie + "\". " + ex.getMessage());
                        }
                    }
                }
                
            } catch (MalformedCookieException ex) {
                if (this.log.isWarnEnabled()) {
                    this.log.warn("Invalid cookie header: \""
                            + header + "\". " + ex.getMessage());
                }
            }
        }
        return valid;
    }

}
