package org.browsermob.proxy;

import java.io.UnsupportedEncodingException;
import java.lang.RuntimeException;
import java.net.MalformedURLException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import org.junit.*;
import static org.junit.Assert.*;

public class TestHttpObject {

    @Test
    public void testSetContent() throws UnsupportedEncodingException {
        HttpObject obj = new HttpObject();
        obj.setResponseContent(new String("hello").getBytes("UTF-8"));
        assertEquals("hello echo", "hello", obj.getResponseString());
    }
}