package org.browsermob.proxy;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.zip.GZIPInputStream;

import org.apache.commons.io.IOUtils;
import org.directwebremoting.annotations.DataTransferObject;

@DataTransferObject
public class HttpObject {
    private int objectNum;
    private Date start;
    // TODO: Bad things will happen in Blocks if this is not set.
    // General problem of mutable object whose constructor does not
    // fully construct.  FIX.
    private Date end;
    private long timeToFirstByte;
    private long timeToLastByte;
    private long bytes;
    private String url;
    private int responseCode;
    private String responseMessage;
    private String method;
    private String protocol;
    private String protocalVersion;
    private String host;
    private String path;
    private String queryString;
    // headers, cookies
    // List<Map<String,String>> ?? best way to do it? all are ugly
    private Map<String,String> requestHeaders;
    //private Map<String,String> requestCookies; // Redundant with headers
    private Map<String,String> responseHeaders;
    private byte[] responseBytes;
    private String responseString;


    public HttpObject() {
    }

    public HttpObject(Date start, URL url, String method) {
        this.start = start;
        this.url = url.toExternalForm();
        this.method = method;
        this.protocol = url.getProtocol();
        this.host = url.getHost();
        this.path = url.getPath();
        this.queryString = url.getQuery();
    }

    public void setObjectNum(int objectNum) {
        this.objectNum = objectNum;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

    public void setResponseMessage(String responseMessage) {
        this.responseMessage = responseMessage;
    }

    public String getResponseMessage() {
        return responseMessage;
    }

    public void setBytes(long bytes) {
        this.bytes = bytes;
    }

    public void setTimeToLastByte(long timeToLastByte) {
        this.timeToLastByte = timeToLastByte;
    }

    public void setTimeToFirstByte(long timeToFirstByte) {
        this.timeToFirstByte = timeToFirstByte;
    }

    public int getObjectNum() {
        return objectNum;
    }

    public Date getStart() {
        return start;
    }

    public long getTimeToFirstByte() {
        return timeToFirstByte;
    }

    public long getTimeToLastByte() {
        return timeToLastByte;
    }

    public long getBytes() {
        return bytes;
    }

    public String getUrl() {
        return url;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public String getMethod() {
        return method;
    }

    public String getProtocol() {
        return protocol;
    }

    public String getProtocolVersion() {
        return protocalVersion;
    }

    public void setProtocolVersion(String protocalVersion) {
        this.protocalVersion = protocalVersion;
    }

    public String getHost() {
        return host;
    }

    public String getPath() {
        return path;
    }

    public String getQueryString() {
        return queryString;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    public Date getEnd() {
        return end;
    }

    public void setResponseHeaders(Map<String,String> responseHeaders) {
        this.responseHeaders = responseHeaders;
    }

    public Map<String,String> getResponseHeaders() {
        if (responseHeaders == null) {
            return new HashMap<String,String>();
        }
        return responseHeaders;
    }

    public void setRequestHeaders(Map<String,String> requestHeaders) {
        this.requestHeaders = requestHeaders;
    }

    public Map<String,String> getRequestHeaders() {
        if (requestHeaders == null) {
            return new HashMap<String,String>();
        }
        return requestHeaders;
    }

    public byte[] getResponseBytes() {
        return responseBytes;
    }

    public String getResponseString() {
        return responseString;
    }

    public void setResponseContent(byte[] bytes) {
        responseBytes = bytes;
        try {
            responseString = new String(bytes, "UTF-8");
        }
        catch (UnsupportedEncodingException e) {
            // leave null
        }
        try {
            // java is ridiculous.  Attempt to unzip content.  Ignore deflate.
            responseString = new String(IOUtils.toByteArray(new GZIPInputStream(new ByteArrayInputStream(responseBytes))), "UTF-8");
        }
        catch (IOException e) {
            //e.printStackTrace();
            // gunzip failed, left in original form
        }
    }


}
