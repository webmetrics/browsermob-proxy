package org.browsermob.proxy;

import org.browsermob.proxy.util.GUID;
import org.directwebremoting.annotations.DataTransferObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@DataTransferObject
public class Block {
    private String id = GUID.generateGUID();
    private Date start = new Date(Long.MAX_VALUE);
    private Date end = new Date(0);
    private long responseTime;
    private long bytes;
    private List<HttpObject> objects = new ArrayList<HttpObject>();

    // TODO: Why would this alone by synchronized?
    public synchronized boolean add(HttpObject object) {
        if (!allowed(object)) {
            return false;
        }

        if (object.getStart().before(start)) {
            start = object.getStart();
        }

        if (object.getEnd().after(end)) {
            end = object.getEnd();
        }

        objects.add(object);
        object.setObjectNum(objects.size());

        bytes += object.getBytes();
        responseTime += object.getTimeToLastByte();

        return true;
    }

    private boolean allowed(HttpObject object) {
        // if we don't have any objects, don't reject anyone since we want some!
        if (objects.isEmpty()) {
            return true;
        }

        // a range of +- 1 second
        long start = this.start.getTime() - 1000;
        long end = this.end.getTime() + 1000;

        // todo: this doesn't seem right
        if (object.getStart().getTime() >= start && object.getStart().getTime() <= end) {
            return true;
        } else {
            return false;
        }
    }

    public Date getStart() {
        return start;
    }

    public Date getEnd() {
        return end;
    }

    public List<HttpObject> getObjects() {
        return objects;
    }

    public long getResponseTime() {
        return responseTime;
    }

    public long getBytes() {
        return bytes;
    }

    public String getId() {
        return id;
    }
}
