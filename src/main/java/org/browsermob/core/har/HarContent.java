package org.browsermob.core.har;

import org.codehaus.jackson.annotate.JsonWriteNullProperties;

@JsonWriteNullProperties(value=false)
public class HarContent {
    private long size;
    private Long compression;
    private String mimeType;
    private String text;

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public Long getCompression() {
        return compression;
    }

    public void setCompression(Long compression) {
        this.compression = compression;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
