package org.browsermob.proxy;

import org.browsermob.proxy.jetty.http.HttpFields;
import org.browsermob.proxy.jetty.http.HttpRequest;
import org.browsermob.proxy.jetty.http.HttpMessage;
import org.browsermob.proxy.jetty.http.HttpResponse;
import org.browsermob.proxy.selenium.SeleniumProxyHandler;
import org.browsermob.proxy.util.BandwidthSimulator;
import org.browsermob.proxy.util.IOUtils;
import org.browsermob.proxy.util.Log;
import org.browsermob.proxy.util.TrustEverythingSSLTrustManager;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLHandshakeException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Date;
import java.util.Enumeration;
import java.util.Map;
import java.util.HashMap;

public class BrowserMobProxyHandler extends SeleniumProxyHandler {
    private static final Log LOG = new Log();

    private int simulatedBps;
    private ProxyServer proxyServer;
    private MockResponse mockResponse = new MockResponse();

    public BrowserMobProxyHandler() {
        super(true, "", "", false, false);
    }

    private IOStats customProxyPlainTextRequest(URL url, String pathInContext, String pathParams, HttpRequest request, HttpResponse response, boolean controlRequest) throws IOException {
        if (mockResponse.matches(url)) {
            Date now = new Date();

            try {
                Thread.sleep(mockResponse.getTime());
            } catch (InterruptedException e) {
                // ok, fine whatever
            }

            response.setStatus(mockResponse.getResponseCode());
            response.setContentType(mockResponse.getContentType());
            byte[] bytes = mockResponse.getContent().getBytes("UTF-8");
            response.getOutputStream().write(bytes);
            request.setHandled(true);

            return new IOStats(null, new IOUtils.Stats(bytes.length, now, null));
        }

        URLConnection connection = url.openConnection();
        connection.setAllowUserInteraction(false);

        // Set method
        HttpURLConnection http = null;
        if (connection instanceof HttpURLConnection) {
            http = (HttpURLConnection) connection;
            http.setRequestMethod(request.getMethod());
            http.setInstanceFollowRedirects(false);
            if (connection instanceof HttpsURLConnection) {
                TrustEverythingSSLTrustManager.trustAllSSLCertificates((HttpsURLConnection) connection);
            }
        }

        // copy headers
        boolean isGet = "GET".equals(request.getMethod());
        boolean hasContent = false;
        Enumeration enm = request.getFieldNames();
        while (enm.hasMoreElements()) {
            String hdr = (String) enm.nextElement();

            if (!isGet && HttpFields.__ContentType.equals(hdr)) {
                hasContent = true;
            }

            Enumeration vals = request.getFieldValues(hdr);
            while (vals.hasMoreElements()) {
                String val = (String) vals.nextElement();
                if (val != null) {
                    if ("User-Agent".equals(hdr)) {
                        val = updateUserAgent(val);
                    }

                    // don't proxy Referer headers if the referer is Selenium!
                    if ("Referer".equals(hdr) && (-1 != val.indexOf("/selenium-server/"))) {
                        continue;
                    }
                    if (!isGet && HttpFields.__ContentLength.equals(hdr) && Integer.parseInt(val) > 0) {
                        hasContent = true;
                    }

                    connection.addRequestProperty(hdr, val);
                }
            }
        }

        // a little bit of cache control
        String cache_control = request.getField(HttpFields.__CacheControl);
        if (cache_control != null && (cache_control.indexOf("no-cache") >= 0 || cache_control.indexOf("no-store") >= 0))
            connection.setUseCaches(false);

        // customize Connection
        customizeConnection(pathInContext, pathParams, request, connection);

        IOUtils.Stats inputStats = null;

        try {
            connection.setDoInput(true);

            // do input thang!
            InputStream in = request.getInputStream();
            if (hasContent) {
                connection.setDoOutput(true);
                inputStats = IOUtils.copyWithStats(in, connection.getOutputStream(), new BandwidthSimulator(controlRequest ? Integer.MAX_VALUE : simulatedBps), false);
            }

            // Connect
            connection.connect();
        }
        catch (Exception e) {
            LOG.fine(e.getMessage(), e);
        }

        InputStream proxy_in = null;

        // handler status codes etc.
        int code;
        if (http != null) {
            proxy_in = http.getErrorStream();

            try {
                try {
                    code = http.getResponseCode();
                } catch (SSLHandshakeException e) {
                    throw new RuntimeException("Couldn't establish SSL handshake.  Try using trustAllSSLCertificates.\n" + e.getLocalizedMessage(), e);
                }
                response.setStatus(code);
                response.setReason(http.getResponseMessage());
            }
            catch (Exception e) {
                LOG.fine(e.getMessage(), e);
                code = 0;
                response.setStatus(code);
                response.setReason("ERROR");
            }
        }

        if (proxy_in == null) {
            try {
                proxy_in = connection.getInputStream();
            }
            catch (Exception e) {
                LOG.fine(e.getMessage(), e);
                proxy_in = http.getErrorStream();
            }
        }

        // clear response defaults.
        response.removeField(HttpFields.__Date);
        response.removeField(HttpFields.__Server);

        // set response headers
        int h = 0;
        String hdr = connection.getHeaderFieldKey(h);
        String val = connection.getHeaderField(h);
        while (hdr != null || val != null) {
            if (hdr != null && val != null && !_DontProxyHeaders.containsKey(hdr) && !_ProxyAuthHeaders.containsKey(hdr))
                response.addField(hdr, val);
            h++;
            hdr = connection.getHeaderFieldKey(h);
            val = connection.getHeaderField(h);
        }


        // Handled
        request.setHandled(true);
        IOUtils.Stats outputStats = null;
        if (proxy_in != null) {
            outputStats = IOUtils.copyWithStats(proxy_in, response.getOutputStream(), new BandwidthSimulator(controlRequest ? Integer.MAX_VALUE : simulatedBps), true);
        }

        return new IOStats(inputStats, outputStats);
    }

    @SuppressWarnings({"unchecked"})
    protected long proxyPlainTextRequest(URL url, String pathInContext, String pathParams, HttpRequest request, HttpResponse response) throws IOException {
        // this is for the control interface, so just do a vanilla proxy and don't simulate bandwidth, track objects, etc
        boolean controlRequest = url.getPort() == 8081 && url.getHost().equals("localhost");
        Date start = new Date();
        HttpObject httpObject = new HttpObject(start, url, request.getMethod());

        try {
            String urlStr = url.toString();

            // we also don't URLs that Firefox always loads on startup showing up, or even wasting bandwidth.
            // so for these we just nuke them right on the spot!
            if (urlStr.startsWith("https://sb-ssl.google.com:443/safebrowsing")
                    || urlStr.startsWith("http://en-us.fxfeeds.mozilla.com/en-US/firefox/headlines.xml")
                    || urlStr.startsWith("http://fxfeeds.mozilla.com/firefox/headlines.xml")
                    || urlStr.startsWith("http://newsrss.bbc.co.uk/rss/newsonline_world_edition/front_page/rss.xml")) {
                // don't even xfer these!
                request.setHandled(true);
                return -1;
            }

            IOStats stats = customProxyPlainTextRequest(url, pathInContext, pathParams, request, response, controlRequest);

            long bytes = 0;
            long ttfb = 0;
            Date end = new Date();
            long ttlb = end.getTime() - start.getTime();
            if (stats.getOutputStats() != null) {
                bytes = stats.getOutputStats().getBytesCopied();
                ttfb = stats.getOutputStats().getTimeToFirstByte().getTime() - start.getTime();
            }

            httpObject.setBytes(bytes);
            httpObject.setResponseCode(response.getStatus());
            httpObject.setTimeToFirstByte(ttfb);
            httpObject.setTimeToLastByte(ttlb);
            httpObject.setEnd(end);
            httpObject.setProtocolVersion(request.getVersion());
            httpObject.setResponseMessage(response.getReason());

            httpObject.setRequestHeaders(httpFieldsAsMap(request));
            httpObject.setResponseHeaders(httpFieldsAsMap(response));

            try {
                // Class name conflict.
                byte[] respBytes =  org.apache.commons.io.IOUtils.toByteArray(stats.getOutputStats().getCopy());
                httpObject.setResponseContent(respBytes);
            }
            catch (IOException e) {
                // TODO
            }

            return bytes;
        } catch (Exception e) {
            LOG.info("Exception while proxying data", e);
            throw new RuntimeException("Exception while proxying data", e);
        }
        finally {
            if (!controlRequest) {
                proxyServer.record(httpObject);
            }
        }
    }


    private Map<String,String> httpFieldsAsMap(HttpMessage fields) {
        Map<String,String> headers = new HashMap<String,String>();
        Enumeration<String> names = fields.getFieldNames();
        while(names.hasMoreElements()) {
            String name = names.nextElement();
            // todo: some fields have multiple values?
            String value = fields.getField(name);
            headers.put(name, value);
        }
        return headers;
    }

    //private void setHeaders

    public void setSimulatedBps(int simulatedBps) {
        this.simulatedBps = simulatedBps;
    }

    private static String updateUserAgent(String ua) {
        int start = ua.indexOf(")");
        if (start > -1) {
            ua = ua.substring(0, start) + "; BrowserMob.com Load Test" + ua.substring(start);
        }

        return ua;
    }

    public void setProxyServer(ProxyServer proxyServer) {
        this.proxyServer = proxyServer;
    }

    public MockResponse getMockResponse() {
        return mockResponse;
    }

    public void setMockResponse(MockResponse mockResponse) {
        this.mockResponse = mockResponse;
    }


    private class IOStats {
        private IOUtils.Stats inputStats;
        private IOUtils.Stats outputStats;

        private IOStats(IOUtils.Stats inputStats, IOUtils.Stats outputStats) {
            this.inputStats = inputStats;
            this.outputStats = outputStats;
        }

        public IOUtils.Stats getInputStats() {
            return inputStats;
        }

        public IOUtils.Stats getOutputStats() {
            return outputStats;
        }
    }
}
