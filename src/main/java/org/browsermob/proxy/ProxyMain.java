package org.browsermob.proxy;

public class ProxyMain {
    public static void main(String[] args) throws Exception {
        ProxyServer.start();
        WebServer.start();
    }
}
