package org.browsermob.proxy;

import java.util.Date;
import java.net.URL;
import java.net.MalformedURLException;
import java.lang.RuntimeException;
import java.net.MalformedURLException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

import com.browsermob.core.har.Har;
import com.browsermob.core.har.HarLog;
import com.browsermob.core.har.HarEntry;
import com.browsermob.core.har.HarCookie;
import com.browsermob.core.har.HarNameValuePair;
import com.browsermob.core.har.HarNameVersion;
import com.browsermob.core.har.HarRequest;
import com.browsermob.core.har.HarResponse;

import org.codehaus.jackson.map.ObjectMapper;


import org.junit.*;
import static org.junit.Assert.*;

public class TestHarConvert {

    @Test
    public void splitQueryStringEmpty() {
        List<HarNameValuePair> empty = HarConvert.splitQueryString("");
        assertEquals("empty string produces empty List", 0, empty.size()); // isEMpty
    }


    @Test
    public void splitQueryStringBasic() {
        List<HarNameValuePair> foo1 = new ArrayList<HarNameValuePair>();
        foo1.add(new HarNameValuePair("foo", "1"));
        assertEquals("foo=1", foo1, HarConvert.splitQueryString("foo=1"));
    }

    @Test
    public void splitQueryStringTwo() {
        List<HarNameValuePair> foobar = new ArrayList<HarNameValuePair>();
        foobar.add(new HarNameValuePair("foo", "1"));
        foobar.add(new HarNameValuePair("bar", "qux"));
        assertEquals("case 2", foobar, HarConvert.splitQueryString("foo=1&bar=qux"));
    }


    @Test
    public void splitQueryStringFunny() {
        List<HarNameValuePair> foobar = new ArrayList<HarNameValuePair>();
        foobar.add(new HarNameValuePair("foo", "1"));
        foobar.add(new HarNameValuePair("bar", "qux"));
        // funny formed &
        assertEquals("funnyformed &", foobar, HarConvert.splitQueryString("&foo=1&&&bar=qux&"));
        //funny formed rand
        foobar.add(new HarNameValuePair("rand", ""));
        assertEquals("funnyformed rand", foobar, HarConvert.splitQueryString("foo=1&bar=qux&rand"));
    }


    // TODO write a bunch of assertions.
    @Test
    public void parseRequesetCookieHeaderBasic() throws java.io.IOException {
        ObjectMapper mapper = new ObjectMapper(); // can reuse, share globally

        String cookieHeader = "foo=bar; phpbb3_bkwcz_k=; style_cookie=null;;";
        //List<HarCookie> expt = new ArrayList<HarCookie>();
        //expt.add(HarConvert.makeHarCookie("foo", "bar"));
        //expt.add(HarConvert.makeHarCookie("phpbb3_bkwcz_k", ""));
        //expt.add(HarConvert.makeHarCookie("style_cookie", "null"));
        List<HarCookie> cookies = HarConvert.parseRequesetCookieHeader(cookieHeader);
        System.out.println(mapper.writeValueAsString(cookies));
        //assertEquals("right number of cookies", expt, 
        //assertEquals("foo", cookies.
    }


    @Test
    public void makeHarRequestMinimal() throws MalformedURLException {
        HttpObject reqObj = new HttpObject(new Date(1274377224), new URL("http://foo.foo/foo"), "GET");
        reqObj.setEnd(new Date(1274377224 + 1));
        HarRequest hreq = HarConvert.makeHarRequest(reqObj);
        assertEquals("url mirror", "http://foo.foo/foo", hreq.getUrl());
    }


    @Test
    public void makeHarRequestInteresting() throws MalformedURLException, java.io.IOException {
        ObjectMapper mapper = new ObjectMapper(); // can reuse, share globally
        HttpObject reqObj = new HttpObject(new Date(1274377224), new URL("http://foo.foo/foo"), "GET");
        reqObj.setEnd(new Date(1274377224 + 1));
        Map<String,String> headers = new HashMap<String,String>();
        headers.put("foo", "bar");
        headers.put("Accept-Language", "en-us,en;q=0.5");
        headers.put("Cookie", "B=drqdv896b&b=3&s=6e; HP=1; CRZY1m=t=6");
        reqObj.setRequestHeaders(headers);
        HarRequest hreq = HarConvert.makeHarRequest(reqObj);
        System.out.println(mapper.writeValueAsString(hreq));
        assertEquals("url mirror", "http://foo.foo/foo", hreq.getUrl());
    }


    @Test
    public void makeHarResponseMinimal() throws MalformedURLException {
        HttpObject obj = new HttpObject(new Date(1274377224), new URL("http://foo.foo/foo"), "GET");
        obj.setEnd(new Date(1274377224 + 1));
        obj.setResponseCode(200);
        HarResponse hresp = HarConvert.makeHarResponse(obj);
        assertEquals("resp status", 200, hresp.getStatus());
    }


    @Test
    public void makeHarResponseInteresting() throws MalformedURLException, java.io.IOException {
        ObjectMapper mapper = new ObjectMapper(); // can reuse, share globally
        HttpObject obj = new HttpObject(new Date(1274377224), new URL("http://foo.foo/foo"), "GET");
        obj.setEnd(new Date(1274377224 + 1));
        obj.setResponseCode(200);
        Map<String,String> headers = new HashMap<String,String>();
        headers.put("foo", "bar");
        headers.put("Set-Cookie", "GROUPS_SID=sid; Domain=groups.google.com; Path=/; HttpOnly");
        obj.setResponseHeaders(headers);
        HarResponse hresp = HarConvert.makeHarResponse(obj);
        System.out.println(mapper.writeValueAsString(hresp));
        assertEquals("resp status", 200, hresp.getStatus());
    }


}