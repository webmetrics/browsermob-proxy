package org.browsermob.proxy.http;

import org.browsermob.core.har.HarTimings;
import org.browsermob.proxy.util.Log;

import java.util.Date;

public class RequestInfo {
    private static final Log LOG = new Log();

    private static ThreadLocal<RequestInfo> instance = new ThreadLocal<RequestInfo>() {
        @Override
        protected RequestInfo initialValue() {
            return new RequestInfo();
        }
    };

    public static RequestInfo get() {
        return instance.get();
    }

    public static void clear(String url) {
        clear();
        RequestInfo info = get();
        info.url = url;
    }

    public static void clear() {
        RequestInfo info = get();
        info.blocked = null;
        info.dns = null;
        info.connect = null;
        info.ssl = null;
        info.send = null;
        info.wait = null;
        info.receive = null;
        info.resolvedAddress = null;
        info.start = null;
        info.end = null;
    }

    private Long blocked;
    private Long dns;
    private Long connect;
    private Long ssl;
    private Long send;
    private Long wait;
    private Long receive;
    private String resolvedAddress;
    private Date start;
    private Date end;
    private String url;

    private Long ping(Date start, Date end) {
        if (this.start == null) {
            this.start = start;
        } else if (this.start.after(start)) {
            LOG.severe("Saw a later start time that was before the first start time for URL %s", url);
        }

        return end.getTime() - start.getTime();
    }

    public Long getBlocked() {
        // return blocked;
        // purposely not sending back blocked timings for now until we know it's reliable
        return null;
    }

    public Long getDns() {
        return dns;
    }

    public Long getConnect() {
        return connect;
    }

    public Long getSsl() {
        return ssl;
    }

    public Long getSend() {
        return send;
    }

    public Long getWait() {
        return wait;
    }

    public Long getReceive() {
        return receive;
    }

    public String getResolvedAddress() {
        return resolvedAddress;
    }

    public void blocked(Date start, Date end) {
        // blocked is special - we don't record this start time as we don't want it to count towards receive time and
        // total time
        blocked = end.getTime() - start.getTime();
    }

    public void dns(Date start, Date end, String resolvedAddress) {
        dns = ping(start, end);
        this.resolvedAddress = resolvedAddress;
    }

    public void connect(Date start, Date end) {
        connect = ping(start, end);
    }

    public void ssl(Date start, Date end) {
        ssl = ping(start, end);
    }

    public void send(Date start, Date end) {
        send = ping(start, end);
    }

    public void wait(Date start, Date end) {
        wait = ping(start, end);
    }

    public void finish() {
        end = new Date();

        if (start == null) {
            start = new Date();
        }

        long totalTime = end.getTime() - start.getTime();

        receive = totalTime - norm(wait) - norm(send) - norm(ssl) - norm(connect) - norm(dns);

        // as per the Har 1.2 spec (to maintain backwards compatibility with 1.1) the connect time should actually
        // include the ssl handshaking time, so doing that here after everything has been calculated
        if (norm(ssl) > 0) {
            connect += ssl;
        }

        if (receive < 0) {
            LOG.severe("Got a negative receiving time (%d) for URL %s", receive, url);
            receive = 0L;
        }
    }

    private long norm(Long val) {
        if (val == null) {
            return 0;
        } else {
            return val;
        }
    }

    public Date getStart() {
        return start;
    }

    public Date getEnd() {
        return end;
    }

    public long getTotalTime() {
        if (end == null || start == null) {
            return -1;
        }

        return end.getTime() - start.getTime();
    }

    @Override
    public String toString() {
        long totalTime = end.getTime() - start.getTime();

        return "RequestInfo{" +
                "blocked=" + blocked +
                ", dns=" + dns +
                ", connect=" + connect +
                ", ssl=" + ssl +
                ", send=" + send +
                ", wait=" + wait +
                ", receive=" + receive +
                ", total=" + totalTime +
                ", resolvedAddress='" + resolvedAddress + '\'' +
                '}';
    }

    public HarTimings getTimings() {
        return new HarTimings(blocked, dns, connect, send, wait, receive);
    }
}
