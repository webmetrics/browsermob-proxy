package org.browsermob.proxy.http;

public interface RequestInterceptor {
    void process(BrowserMobHttpRequest request);
}
