package org.browsermob.proxy.http;

import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
import org.apache.http.client.CredentialsProvider;

import java.util.HashMap;
import java.util.Map;

public class WildcardMatchingCredentialsProvider implements CredentialsProvider {
    private final HashMap<AuthScope, Credentials> credMap = new HashMap<AuthScope, Credentials>();

    @Override
    public synchronized void setCredentials(AuthScope authscope, Credentials credentials) {
        credMap.put(authscope, credentials);
    }

    @Override
    public synchronized Credentials getCredentials(AuthScope authscope) {
        for (Map.Entry<AuthScope, Credentials> entry : credMap.entrySet()) {
            if (entry.getKey().getHost() == null) {
                continue;
            }
            if (authscope.getHost().contains(entry.getKey().getHost())) {
                return entry.getValue();
            }
        }

        return null;
    }

    @Override
    public synchronized void clear() {
        credMap.clear();
    }
}
