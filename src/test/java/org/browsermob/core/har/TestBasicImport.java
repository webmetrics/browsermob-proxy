package com.browsermob.core.har;

import java.io.IOException;
import java.io.InputStream;
import java.lang.RuntimeException;

import org.codehaus.jackson.map.ObjectMapper;

import org.apache.commons.io.IOUtils;

import org.junit.*;
import static org.junit.Assert.*;


/** Test the basic importing of HAR as json.  No attempt at completeness is made here. */
public class TestBasicImport {
    private String harBlob;
    private final ObjectMapper mapper = new ObjectMapper();

    
    @Before
    public void setUp() throws IOException {
        InputStream harStream = this.getClass().getResourceAsStream("/org/browsermob/core/har/ysearch.har");
        harBlob = IOUtils.toString(harStream, "UTF-8");
    }


    private HarLog extractHarLog() {
        try {
            Har har = mapper.readValue(harBlob, Har.class);
            return har.getLog();
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void browser() {
        HarLog ysearch = extractHarLog();
        String harBrowser = ysearch.getBrowser().getName();
        assertEquals("browser name", "Firefox", harBrowser);
    }
}