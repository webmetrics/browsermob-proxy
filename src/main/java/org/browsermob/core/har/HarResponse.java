package com.browsermob.core.har;

import java.util.List;

import org.codehaus.jackson.annotate.JsonWriteNullProperties;

@JsonWriteNullProperties(value=false)
public class HarResponse {
    private int status;
    private String statusText;
    private String httpVersion;
    private List<HarCookie> cookies;
    private List<HarNameValuePair> headers;
    private HarContent content;
    private String redirectURL;
    private long headersSize;
    private long bodySize;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getStatusText() {
        return statusText;
    }

    public void setStatusText(String statusText) {
        this.statusText = statusText;
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

    public HarContent getContent() {
        return content;
    }

    public void setContent(HarContent content) {
        this.content = content;
    }

    public String getRedirectURL() {
        return redirectURL;
    }

    public void setRedirectURL(String redirectURL) {
        this.redirectURL = redirectURL;
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
