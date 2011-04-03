package org.browsermob.proxy.http;

import org.apache.http.client.CookieStore;
import org.apache.http.cookie.Cookie;

import java.util.Collections;
import java.util.Date;
import java.util.List;

public class BlankCookieStore implements CookieStore {
    @Override
    public void addCookie(Cookie cookie) {
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
