package org.browsermob.proxy.http;

import org.apache.http.client.CookieStore;
import org.apache.http.cookie.Cookie;
import org.browsermob.core.har.HarCookie;

import java.util.Collections;
import java.util.Date;
import java.util.List;

public class BlankCookieStore implements CookieStore {
    @Override
    public void addCookie(Cookie cookie) {
        HarCookie hc = new HarCookie();
        hc.setDomain(cookie.getDomain());
        hc.setExpires(cookie.getExpiryDate());
        hc.setName(cookie.getName());
        hc.setValue(cookie.getValue());
        hc.setPath(cookie.getPath());
        RequestInfo.get().getEntry().getResponse().getCookies().add(hc);
    }

    @Override
    public List<Cookie> getCookies() {
        return Collections.emptyList();
    }

    @Override
    public boolean clearExpired(Date date) {
        return false;
    }

    @Override
    public void clear() {
    }
}
