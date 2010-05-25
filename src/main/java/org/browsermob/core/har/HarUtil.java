package com.browsermob.core.har;

/** Handy things for working with har objects */
public class HarUtil {

    public static HarEntry findEntryByUrl(Har har, String toFind) {
        for (HarEntry entry: har.getLog().getEntries()) {
            if (entry.getRequest().getUrl().indexOf(toFind) != -1) {
                return entry;
            }
        }
        return null;
    }
}