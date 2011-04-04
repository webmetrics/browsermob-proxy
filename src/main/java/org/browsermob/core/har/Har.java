package org.browsermob.core.har;

import org.codehaus.jackson.map.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;

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

    public void writeTo(Writer writer) throws IOException {
        ObjectMapper om = new ObjectMapper();
        om.writeValue(writer, this);
    }

    public void writeTo(OutputStream os) throws IOException {
        ObjectMapper om = new ObjectMapper();
        om.writeValue(os, this);
    }

    public void writeTo(File file) throws IOException {
        ObjectMapper om = new ObjectMapper();
        om.writeValue(file, this);
    }
}
