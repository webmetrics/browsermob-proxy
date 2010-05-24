package com.browsermob.core.har;

public final class HarNameValuePair {
    private String name;
    private String value;

    public HarNameValuePair() {
    }

    public HarNameValuePair(String name, String value) {
        this.name = name;
        this.value = value;
    }

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

    // TODO: Perhaps these should be done the right way
    public boolean equals(Object o) {
        HarNameValuePair obj = (HarNameValuePair)o;
        return obj.getName().equals(this.getName()) && obj.getValue().equals(this.getValue());

    }

    public int hashCode() {
        return toString().hashCode();
    }

    public String toString() {
        return name + "=" + value;
    }
}
