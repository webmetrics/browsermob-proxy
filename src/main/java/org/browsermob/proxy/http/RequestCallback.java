package org.browsermob.proxy.http;

import org.apache.http.Header;
import org.apache.http.StatusLine;

public interface RequestCallback {
    void handleStatusLine(StatusLine statusLine);

    void handleHeaders(Header[] headers);

    boolean reportHeader(Header header);

    void reportError(Exception e);
}
