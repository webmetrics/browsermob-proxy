package com.browsermob.core.har;

import com.browsermob.core.json.ISO8601DateFormatter;
import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.util.Date;

@JsonAutoDetect
public class HarEntry {
    private String pageref;
    private Date startedDateTime;
    private long time;
    private HarRequest request;
    private HarResponse response;
    private HarCache cache;
    private HarTimings timings;

    public String getPageref() {
        return pageref;
    }

    public void setPageref(String pageref) {
        this.pageref = pageref;
    }

    @JsonSerialize(using = ISO8601DateFormatter.class)
    public Date getStartedDateTime() {
        return startedDateTime;
    }

    public void setStartedDateTime(Date startedDateTime) {
        this.startedDateTime = startedDateTime;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public HarRequest getRequest() {
        return request;
    }

    public void setRequest(HarRequest request) {
        this.request = request;
    }

    public HarResponse getResponse() {
        return response;
    }

    public void setResponse(HarResponse response) {
        this.response = response;
    }

    public HarCache getCache() {
        return cache;
    }

    public void setCache(HarCache cache) {
        this.cache = cache;
    }

    public HarTimings getTimings() {
        return timings;
    }

    public void setTimings(HarTimings timings) {
        this.timings = timings;
    }
}
