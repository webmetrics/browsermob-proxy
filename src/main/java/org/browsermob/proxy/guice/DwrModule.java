package org.browsermob.proxy.guice;

import org.browsermob.proxy.*;
import org.directwebremoting.guice.AbstractDwrModule;
import org.directwebremoting.guice.DwrScopes;
import org.directwebremoting.guice.ParamName;

public class DwrModule extends AbstractDwrModule {
    @Override
    protected void configure() {
        bindRemoted(ProxyController.class).to(ProxyController.class).in(DwrScopes.APPLICATION);
        bindRemoted(ProxyServer.class).to(ProxyServer.class).in(DwrScopes.APPLICATION);
        bindAnnotatedClasses(HttpObject.class, Block.class, MockResponse.class);

        bindParameter(ParamName.DEBUG).to(true);
        bindParameter(ParamName.ACTIVE_REVERSE_AJAX_ENABLED).to(true);
        bindParameter(ParamName.MAX_WAIT_AFTER_WRITE).to(10000L);

        bindDwrScopes(false);
    }
}
