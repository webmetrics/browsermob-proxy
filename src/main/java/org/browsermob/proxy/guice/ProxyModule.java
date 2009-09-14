package org.browsermob.proxy.guice;

import com.google.inject.Binder;
import com.google.inject.Module;

public class ProxyModule implements Module {
    @Override
    public void configure(Binder binder) {
        binder.install(new DwrModule());
    }
}
