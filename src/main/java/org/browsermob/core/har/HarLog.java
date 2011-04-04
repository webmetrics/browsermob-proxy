package org.browsermob.core.har;

import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.util.ArrayList;
import java.util.List;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class HarLog {
    private String version = "1.1";
    private HarNameVersion creator;
    private HarNameVersion browser;
    private List<HarPage> pages = new ArrayList<HarPage>();
    private List<HarEntry> entries = new ArrayList<HarEntry>();

    public HarLog() {
    }

    public HarLog(HarNameVersion creator) {
        this.creator = creator;
    }

    public void addPage(HarPage page) {
        if (pages == null) {
            pages = new ArrayList<HarPage>();
        }

        pages.add(page);
    }

    public void addEntry(HarEntry entry) {
        if (entries == null) {
            entries = new ArrayList<HarEntry>();
        }

        entries.add(entry);
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public HarNameVersion getCreator() {
        return creator;
    }

    public void setCreator(HarNameVersion creator) {
        this.creator = creator;
    }

    public HarNameVersion getBrowser() {
        return browser;
    }

    public void setBrowser(HarNameVersion browser) {
        this.browser = browser;
    }

    public List<HarPage> getPages() {
        return pages;
    }

    public void setPages(List<HarPage> pages) {
        this.pages = pages;
    }

    public List<HarEntry> getEntries() {
        return entries;
    }

    public void setEntries(List<HarEntry> entries) {
        this.entries = entries;
    }
}
