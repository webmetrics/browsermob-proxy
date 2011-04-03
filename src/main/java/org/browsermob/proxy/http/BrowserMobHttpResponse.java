package org.browsermob.proxy.http;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpRequestBase;

public class BrowserMobHttpResponse {
    private HttpRequestBase method;
    private HttpResponse response;
    private TransactionStepObject info;
    private boolean contentMatched;
    private String verificationText;
    private String errorMessage;
    private String body;
    private String contentType;
    private String charSet;

    public BrowserMobHttpResponse(HttpRequestBase method, HttpResponse response, TransactionStepObject info, boolean contentMatched, String verificationText, String errorMessage, String body, String contentType, String charSet) {
        this.method = method;
        this.response = response;
        this.info = info;
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

    public TransactionStepObject getInfo() {
        return info;
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
}
