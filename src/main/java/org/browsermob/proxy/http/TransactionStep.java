package org.browsermob.proxy.http;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class TransactionStep implements Serializable {
    private int step;
    private Date start;
    private Date end;
    private long bytes;
    private int objectCount;
    private List<TransactionStepObject> objects = new CopyOnWriteArrayList<TransactionStepObject>();
    private long timeActive;

    public TransactionStep() {
        start = new Date();
    }

    public int getStep() {
        return step;
    }

    public void setStep(int step) {
        this.step = step;
    }

    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    public long getBytes() {
        return bytes;
    }

    public void setBytes(long bytes) {
        this.bytes = bytes;
    }

    public int getObjectCount() {
        return objectCount;
    }

    public void setObjectCount(int objectCount) {
        this.objectCount = objectCount;
    }

    public void setObjects(List<TransactionStepObject> objects) {
        this.objects = objects;
    }

    public List<TransactionStepObject> getObjects() {
        return objects;
    }

    public synchronized void addObject(TransactionStepObject object) {
        objectCount++;

        object.setObjectNum(objectCount);
        objects.add(object);
    }

    public long getTimeActive() {
        return timeActive;
    }

    public void setTimeActive(long timeActive) {
        this.timeActive = timeActive;
    }

    public synchronized void addBytes(long bytes) {
        this.bytes += bytes;
    }
}
