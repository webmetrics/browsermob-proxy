package org.browsermob.proxy;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.sitebricks.SitebricksModule;
import com.google.sitebricks.client.Web;
import com.google.sitebricks.client.WebResponse;
import com.google.sitebricks.client.transport.Json;
import org.browsermob.core.har.Har;

public class Test {
    public static void main(String[] args) {
        Injector injector = Guice.createInjector(new SitebricksModule());
        WebResponse response = injector.getInstance(Web.class)
                .clientOf("http://localhost:8080/proxy/9091/har")
                .transports(Har.class)
                .over(Json.class).get();
        Har har = response.to(Har.class).using(Json.class);
        System.out.println(har.getLog().getBrowser().getName());
    }
}
