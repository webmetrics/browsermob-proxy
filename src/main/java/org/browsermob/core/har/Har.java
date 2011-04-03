package org.browsermob.core.har;

public class Har {
    private HarLog log;

    public Har() {
    }

    public Har(HarLog log) {
        this.log = log;
    }

    public HarLog getLog() {
        return log;
    }

    public void setLog(HarLog log) {
        this.log = log;
    }
}
