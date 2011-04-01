package org.browsermob.core.har;

import org.codehaus.jackson.annotate.JsonWriteNullProperties;

@JsonWriteNullProperties(value=false)
public class HarPostDataParam {
    private String name;
    private String value;
    private String fileName;
    private String contentType;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }
}
