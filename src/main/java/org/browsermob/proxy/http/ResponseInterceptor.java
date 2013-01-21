package org.browsermob.proxy.http;

public interface ResponseInterceptor {
    void process(BrowserMobHttpResponse response);
}
