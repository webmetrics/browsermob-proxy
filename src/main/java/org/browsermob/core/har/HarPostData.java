package org.browsermob.core.har;

import org.codehaus.jackson.annotate.JsonWriteNullProperties;

import java.util.List;

@JsonWriteNullProperties(value=false)
public class HarPostData {
    private String mimeType;
    private List<HarPostDataParam> params;
    private String text;

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public List<HarPostDataParam> getParams() {
        return params;
    }

    public void setParams(List<HarPostDataParam> params) {
        this.params = params;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
