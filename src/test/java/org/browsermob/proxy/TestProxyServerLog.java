package org.browsermob.proxy;

import java.util.Date;
import java.net.URL;
import java.util.List;
import java.net.MalformedURLException;

import org.junit.*;
import static org.junit.Assert.*;


public class TestProxyServerLog {
    private ProxyServer ps;
    private String fooUrl = "http://www.foo.foo";
    private String barUrl = "http://www.bar.bar";
    private long someTime = 1274377224;
    private HttpObject foo;
    private HttpObject bar;

    @Before
    public void fooAndBar() throws MalformedURLException {// Name is too good, may need to switch from metasyntatic variables.
        ps = new ProxyServer();
        foo = new HttpObject(new Date(1274377224), new URL(fooUrl), "GET");
        foo.setEnd(new Date(someTime + 1));
        bar = new HttpObject(new Date(1274377224 + 2), new URL(barUrl), "GET");
        bar.setEnd(new Date(someTime + 3));
    }


    @Test
    public void singleRecord() {
        ps.record(foo);
        List<HttpObject> objs = ProxyBlockUtil.collapseBlocks(ps.getBlocks());
        assertEquals("There is only foo", 1, objs.size());
        assertEquals("foo is foo", fooUrl, objs.get(0).getUrl().toString());
    }

    @Test
    public void singleNamedRecord() {
        ProxyServerLog serverLog = ps.getServerLog();
        serverLog.startNewNamedSession("mysession");
        ps.record(foo);
        List<HttpObject> objs = ProxyBlockUtil.collapseBlocks(ps.getBlocks());
        assertEquals("foo is foo", fooUrl, objs.get(0).getUrl().toString());
        serverLog.stopSession("mysession");
        List<HttpObject> myobjs = ProxyBlockUtil.collapseBlocks(serverLog.getNamedSession("mysession"));
        assertEquals("foo is still foo", fooUrl, myobjs.get(0).getUrl().toString());
    }


    @Test
    public void multiNamedRecord() {
        ProxyServerLog serverLog = ps.getServerLog();
        serverLog.startNewNamedSession("mysession");
        ps.record(foo);
        serverLog.stopSession("mysession");
        ps.record(bar);
        List<HttpObject> objs = ProxyBlockUtil.collapseBlocks(ps.getBlocks());
        assertEquals("recent includes all recorded", 2, objs.size());
        List<HttpObject> myobjs = ProxyBlockUtil.collapseBlocks(serverLog.getNamedSession("mysession"));
        assertEquals("only one in named session", 1, myobjs.size());
        assertEquals("named session has foo", fooUrl, myobjs.get(0).getUrl().toString());
    }


    //    public HttpObject(Date start, URL url, String method) {


}