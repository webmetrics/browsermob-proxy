package org.browsermob.core.har;

import org.codehaus.jackson.map.annotate.JsonSerialize;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class HarCache {
    private HarCacheStatus beforeRequest;
    private HarCacheStatus afterRequest;

    public HarCacheStatus getBeforeRequest() {
        return beforeRequest;
    }

    public void setBeforeRequest(HarCacheStatus beforeRequest) {
        this.beforeRequest = beforeRequest;
    }

    public HarCacheStatus getAfterRequest() {
        return afterRequest;
    }

    public void setAfterRequest(HarCacheStatus afterRequest) {
        this.afterRequest = afterRequest;
    }
}
