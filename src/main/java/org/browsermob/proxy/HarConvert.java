package org.browsermob.proxy;

import java.util.Date;
import java.net.URL;
import java.net.MalformedURLException;
import java.lang.RuntimeException;
import java.lang.IllegalArgumentException;
import java.net.HttpCookie;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.browsermob.core.har.Har;
import com.browsermob.core.har.HarLog;
import com.browsermob.core.har.HarEntry;
import com.browsermob.core.har.HarCookie;
import com.browsermob.core.har.HarContent;
import com.browsermob.core.har.HarNameValuePair;
import com.browsermob.core.har.HarNameVersion;
import com.browsermob.core.har.HarPageTimings;
import com.browsermob.core.har.HarRequest;
import com.browsermob.core.har.HarResponse;
import org.browsermob.proxy.util.Log;

public class HarConvert {
    private static final Log LOG = new Log();

    /** Turn an old list of blocks to a Har Archieve. Much left incomplete. */
    public static Har blocksToHar(List<Block> blocks) {
        List<HttpObject> httpObjects = ProxyBlockUtil.collapseBlocks(blocks);
        HarLog harLog = new HarLog();
        harLog.setCreator(new HarNameVersion("browsermob-proxy", "1.0-SNAPSHOT")); // TODO: autoversion
        harLog.setBrowser(new HarNameVersion(null, null)); // TODO: (optional) Can this reasonably be extracted?
        List<HarEntry> entries = new ArrayList<HarEntry>();
        for (HttpObject obj : httpObjects) { // time sort reverse?
            entries.add(httpObjectToHarEntry(obj));
        }
        harLog.setEntries(entries);
        Har har = new Har();
        har.setLog(harLog);
        return har;
    }


    public static HarEntry httpObjectToHarEntry(HttpObject obj) {
        HarEntry entry = new HarEntry();

        entry.setStartedDateTime(obj.getStart());
        entry.setTime(obj.getTimeToLastByte());
        entry.setRequest(makeHarRequest(obj));
        entry.setResponse(makeHarResponse(obj));
        //entry.setPageTimings(new HarPageTimings(Long.valueOf(-1), Long.valueOf(-1))); // neither is available

        return entry;
    }


    protected static HarCookie makeHarCookie(HttpCookie cookie) {
        HarCookie hc = new HarCookie();

        hc.setName(cookie.getName());
        hc.setValue(cookie.getValue());
        hc.setPath(cookie.getPath());
        hc.setDomain(cookie.getDomain());
        hc.setExpires(new Date(cookie.getMaxAge()));
        hc.setHttpOnly(!cookie.getSecure());

        return hc;
    }


    // TODO: move to constructor?
    protected static HarCookie makeHarCookie(String name, String value) {
        HarCookie hc = new HarCookie();

        hc.setName(name);
        hc.setValue(value);

        return hc;
    }


    // TODO: looks like these two methods could be generalized.
    protected static List<HarCookie> parseRequesetCookieHeader(String cookieHeader) {
        List<HarCookie> harCookies = new ArrayList<HarCookie>();
        if (cookieHeader == null || cookieHeader.equals("")) {
            return harCookies;
        }
        String[] pairs = cookieHeader.split(";");
        for (String pair : pairs) {
            if (pair.equals("")) {
                continue;
            }
            String[] kv = pair.split("=");
            if (kv.length == 2) {
                harCookies.add(makeHarCookie(kv[0].trim(), kv[1].trim()));
            }
            else {
                harCookies.add(makeHarCookie(pair.trim(), ""));
            }
        }
        return harCookies;
    }


    protected static List<HarNameValuePair> splitQueryString(String qString) {
        List<HarNameValuePair> harPairs = new ArrayList<HarNameValuePair>();
        if (qString == null) {
            return harPairs;
        }
        String[] params = qString.split("&");
        for (String param : params) {
            if (param.equals("")) {
                continue;
            }
            String[] kv = param.split("=");
            if (kv.length == 2) {
                harPairs.add(new HarNameValuePair(kv[0], kv[1]));
            }
            else {
                harPairs.add(new HarNameValuePair(param, ""));
            }
        }
        return harPairs;
    }


    protected static HarRequest makeHarRequest(HttpObject obj) {
        HarRequest req = new HarRequest();
        req.setMethod(obj.getMethod());
        req.setUrl(obj.getUrl());
        req.setHttpVersion(obj.getProtocolVersion());
        List<HarNameValuePair> harHeaders = new ArrayList<HarNameValuePair>();
        long headerSize = 0;
        for (String headerName: obj.getRequestHeaders().keySet()) {
            String headerVal = obj.getRequestHeaders().get(headerName);
            harHeaders.add(new HarNameValuePair(headerName, headerVal));
            headerSize += headerName.length() + headerVal.length();
        }
        req.setHeaders(harHeaders);
        req.setHeadersSize(headerSize); // TODO: CL/LF and other details

        // TODO: only pickups first cookie, revist after request is checked
        List<HarCookie> harCookies = new ArrayList<HarCookie>(); // correct use of optional and nulls?
        Map<String,String> reqHeaders = obj.getRequestHeaders();
        if (reqHeaders.containsKey("Cookie")) {
            harCookies = parseRequesetCookieHeader(reqHeaders.get("Cookie"));
        }
        req.setCookies(harCookies);
        URL url = null;
        try {
            url = new URL(obj.getUrl());
        }
        catch (MalformedURLException e) {
            throw new RuntimeException(e); // TODO: checked exceptions are dumb, figure out how to deal with this
        }
        String qString = url.getQuery();
        req.setQueryString(splitQueryString(qString));
        //setPostData(HarPostData postData); // TODO (optional)
        req.setBodySize(-1); // TODO (only for post)
        return req;
    }


    protected static HarResponse makeHarResponse(HttpObject obj) {
        HarResponse resp = new HarResponse();

        resp.setStatus(obj.getResponseCode());
        resp.setStatusText(obj.getResponseMessage());
        resp.setHttpVersion(obj.getProtocolVersion());
        List<HarNameValuePair> harHeaders = new ArrayList<HarNameValuePair>();
        long headerSize = 0;
        for (String headerName: obj.getResponseHeaders().keySet()) {
            String headerVal = obj.getResponseHeaders().get(headerName);
            harHeaders.add(new HarNameValuePair(headerName, headerVal));
            headerSize += headerName.length() + headerVal.length();
        }
        resp.setHeaders(harHeaders);
        resp.setHeadersSize(headerSize);

        List<HarCookie> harCookies = new ArrayList<HarCookie>();
        Map<String,String> respHeaders = obj.getResponseHeaders();
        if (respHeaders.containsKey("Set-Cookie")) {
            try {
                List<HttpCookie> cookies = HttpCookie.parse(respHeaders.get("Set-Cookie"));
                for (HttpCookie cookie : cookies) {
                    harCookies.add(makeHarCookie(cookie));
                }
            }
            catch (IllegalArgumentException e) {
                LOG.warn("Failed to parse response header cookies: " + respHeaders.get("Set-Cookie"));
            }
        }
        resp.setCookies(harCookies);

        // TODO: dummy for now, stream fun
        resp.setContent(new HarContent());
        resp.setBodySize(-1);


        if (respHeaders.containsKey("Location")) {
            resp.setRedirectURL(respHeaders.get("Location"));
        }


        return resp;
    }

}
