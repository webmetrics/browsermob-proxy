package org.browsermob.proxy.http;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Transaction implements Serializable {
    protected Date start;
    protected Date end;
    protected long bytes;
    protected boolean success;
    protected String sourceIPAddress;
    protected int objectCount;
    protected int stepCount;
    protected List<TransactionStep> steps = new CopyOnWriteArrayList<TransactionStep>();
    protected long timeActive;

    public Transaction() {
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

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public int getStepCount() {
        return stepCount;
    }

    public List<TransactionStep> getSteps() {
        return steps;
    }

    public void setSteps(List<TransactionStep> steps) {
        this.steps = steps;
    }

    public synchronized void addStep(TransactionStep step) {
        stepCount++;
        if (step.getStep() == 0) step.setStep(stepCount); // Honor BrowserMobControl.beginStep().setStep(n)
        steps.add(step);
    }

    public long getTimeActive() {
        return timeActive;
    }

    public void setTimeActive(long timeActive) {
        this.timeActive = timeActive;
    }

    public void addBytes(long bytes) {
        this.bytes += bytes;
    }

    public String getSourceIPAddress() {
        return sourceIPAddress;
    }

    public void setSourceIPAddress(String sourceIPAddress) {
        this.sourceIPAddress = sourceIPAddress;
    }

    public int getObjectCount() {
        return objectCount;
    }

    public void setObjectCount(int objectCount) {
        this.objectCount = objectCount;
    }
}
