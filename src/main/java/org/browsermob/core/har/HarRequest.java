package org.browsermob.core.har;

import org.codehaus.jackson.annotate.JsonWriteNullProperties;

import java.util.List;

@JsonWriteNullProperties(value=false)
public class HarRequest {
    private String method;
    private String url;
    private String httpVersion;
    private List<HarCookie> cookies;
    private List<HarNameValuePair> headers;
    private List<HarNameValuePair> queryString;
    private HarPostData postData;
    private long headersSize; // Odd grammar in spec
    private long bodySize;

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getHttpVersion() {
        return httpVersion;
    }

    public void setHttpVersion(String httpVersion) {
        this.httpVersion = httpVersion;
    }

    public List<HarCookie> getCookies() {
        return cookies;
    }

    public void setCookies(List<HarCookie> cookies) {
        this.cookies = cookies;
    }

    public List<HarNameValuePair> getHeaders() {
        return headers;
    }

    public void setHeaders(List<HarNameValuePair> headers) {
        this.headers = headers;
    }

    public List<HarNameValuePair> getQueryString() {
        return queryString;
    }

    public void setQueryString(List<HarNameValuePair> queryString) {
        this.queryString = queryString;
    }

    public HarPostData getPostData() {
        return postData;
    }

    public void setPostData(HarPostData postData) {
        this.postData = postData;
    }

    public long getHeadersSize() {
        return headersSize;
    }

    public void setHeadersSize(long headersSize) {
        this.headersSize = headersSize;
    }

    public long getBodySize() {
        return bodySize;
    }

    public void setBodySize(long bodySize) {
        this.bodySize = bodySize;
    }
}
