package org.browsermob.proxy.http;

import org.apache.http.conn.ssl.X509HostnameVerifier;

import javax.net.ssl.SSLException;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;
import java.io.IOException;
import java.security.cert.X509Certificate;

/**
 * Our own implementation of the AllowAllHostnameVerifier class.  The one that ships with HttpClient doesn't actually
 * allow all host names.  In particular, it fails to work when an upstream proxy is present.
 *
 * http://javaskeleton.blogspot.com/2010/07/avoiding-peer-not-authenticated-with.html was a very helpful resource in
 * tracking down SSL problems with HttpClient.
 */
public class AllowAllHostnameVerifier implements X509HostnameVerifier {
    @Override
    public void verify(String string, SSLSocket ssls) throws IOException {
    }

    @Override
    public void verify(String string, X509Certificate xc) throws SSLException {
    }

    @Override
    public void verify(String string, String[] strings, String[] strings1) throws SSLException {
    }

    @Override
    public boolean verify(String string, SSLSession ssls) {
        return true;
    }
}
