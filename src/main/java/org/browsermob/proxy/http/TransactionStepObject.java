package org.browsermob.proxy.http;

import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.Map;

public class TransactionStepObject implements Serializable {
    private int objectNum;
    private Date start;
    private Date end;
    private long timeActive;
    private long dnsLookupTime;
    private long timeToFirstByte;
    private long sslHandshakeTime; // New
    private long connectTime;   // New
    private long blockedTime;   // New
    private long sendTime;      // New
    private long receiveTime;
    private long bytes;
    private String url;
    private int statusCode;
    private String method;
    private String protocol;
    private String host;
    private String resolvedIpAddress; // New
    private String path;
    private String queryString; // Removed
    private String errorMessage;

    // Collected only during validation
    private Map<String, String[]> postParams;
    private Map<String, String[]> requestHeaders;
    private Map<String, String[]> responseHeaders;
    private String file;

    public TransactionStepObject() {
    }

    public TransactionStepObject(String url, int statusCode, String method) {
        this.start = new Date();
        this.method = method;
        this.url = url;
        this.statusCode = statusCode;

        try {
            URL u = new URL(url);
            protocol = u.getProtocol();
            host = u.getHost();
            path = u.getPath();
            queryString = u.getQuery();
        } catch (MalformedURLException e) {
            // fine, ignore
        }
    }
    public int getObjectNum() {
        return objectNum;
    }

    public void setObjectNum(int objectNum) {
        this.objectNum = objectNum;
    }

    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    public long getBytes() {
        return bytes;
    }

    public void setBytes(long bytes) {
        this.bytes = bytes;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getQueryString() {
        return queryString;
    }

    public void setQueryString(String queryString) {
        this.queryString = queryString;
    }

    public long getTimeActive() {
        return timeActive;
    }

    public void setTimeActive(long timeActive) {
        this.timeActive = timeActive;
    }

    public long getTimeToFirstByte() {
        return timeToFirstByte;
    }

    public void setTimeToFirstByte(long timeToFirstByte) {
        this.timeToFirstByte = timeToFirstByte;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public Map<String, String[]> getPostParams() {
        return postParams;
    }

    public void setPostParams(Map<String, String[]> postParams) {
        this.postParams = postParams;
    }

    public Map<String, String[]> getRequestHeaders() {
        return requestHeaders;
    }

    public void setRequestHeaders(Map<String, String[]> requestHeaders) {
        this.requestHeaders = requestHeaders;
    }

    public Map<String, String[]> getResponseHeaders() {
        return responseHeaders;
    }

    public void setResponseHeaders(Map<String, String[]> responseHeaders) {
        this.responseHeaders = responseHeaders;
    }

    public String getFile() {
        if (file == null) {
            int slash = path.lastIndexOf('/');
            file = path.substring(slash + 1);

            if (file.isEmpty()) {
                file = host;
            }
        }

        return file;
    }

    public void setResolvedIpAddress(String resolvedIpAddress) {
        this.resolvedIpAddress = resolvedIpAddress;
    }

    
    public String getResolvedIpAddress() {
        return resolvedIpAddress;
    }

    public long getDnsLookupTime() {
        return dnsLookupTime;
    }

    public void setDnsLookupTime(long dnsLookupTime) {
        this.dnsLookupTime = dnsLookupTime;
    }
    public long getSslHandshakeTime() {
        return sslHandshakeTime;
    }

    public void setSslHandshakeTime(long sslHandshakeTime) {
        this.sslHandshakeTime = sslHandshakeTime;
    }

    public long getConnectTime() {
        return connectTime;
    }

    public void setConnectTime(long connectTime) {
        this.connectTime = connectTime;
    }

    
    public long getBlockedTime() {
        return blockedTime;
    }

    public void setBlockedTime(long blockedTime) {
        this.blockedTime = blockedTime;
    }

    
    public long getSendTime() {
        return sendTime;
    }

    public void setSendTime(long sendTime) {
        this.sendTime = sendTime;
    }

    
    public long getReceiveTime() {
        return receiveTime;
    }

    public void setReceiveTime(long receiveTime) {
        this.receiveTime = receiveTime;
    }
}
