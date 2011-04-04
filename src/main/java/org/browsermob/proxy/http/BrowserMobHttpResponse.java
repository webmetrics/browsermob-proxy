package org.browsermob.proxy.http;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpRequestBase;
import org.browsermob.core.har.HarEntry;

public class BrowserMobHttpResponse {
    private HarEntry entry;
    private HttpRequestBase method;
    private HttpResponse response;
    private boolean contentMatched;
    private String verificationText;
    private String errorMessage;
    private String body;
    private String contentType;
    private String charSet;

    public BrowserMobHttpResponse(HarEntry entry, HttpRequestBase method, HttpResponse response, boolean contentMatched, String verificationText, String errorMessage, String body, String contentType, String charSet) {
        this.entry = entry;
        this.method = method;
        this.response = response;
        this.contentMatched = contentMatched;
        this.verificationText = verificationText;
        this.errorMessage = errorMessage;
        this.body = body;
        this.contentType = contentType;
        this.charSet = charSet;
    }

    public boolean isContentMatched() {
        return contentMatched;
    }

    public String getBody() {
        return body;
    }

    public String getContentType() {
        return contentType;
    }

    public String getCharSet() {
        return charSet;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public String getHeader(String name) {
        Header header = response.getFirstHeader(name);
        if (header == null) {
            return null;
        }

        return header.getValue();
    }

    public HttpResponse getRawResponse() {
        return response;
    }

    public void checkContentMatched(String info) {
        if (!isContentMatched()) {
            throw new RuntimeException("Content match failure. Expected '" + verificationText + "'." + (info != null ? " " + info : ""));
        }
    }

    public HarEntry getEntry() {
        return entry;
    }
}
