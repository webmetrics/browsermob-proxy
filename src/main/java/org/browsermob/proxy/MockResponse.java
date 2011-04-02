package org.browsermob.proxy;

import org.browsermob.proxy.util.Log;

import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MockResponse {
    private static final Log LOG = new Log();

    private boolean enabled;
    private Pattern compiledPattern;
    private String pattern;
    private int responseCode = 200;
    private String content = "Mock Response";
    private String contentType = "text/html";
    private long time = 1000;

    public MockResponse() {
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public String getPattern() {
        return pattern;
    }

    public synchronized boolean matches(URL url) {
        if (!enabled || pattern == null) {
            return false;
        }

        if (compiledPattern == null) {
            try {
                compiledPattern = Pattern.compile(pattern);
            } catch (Exception e) {
                LOG.warn("Could not parse regular expression %s", pattern);
                // uh oh
                return false;
            }
        }

        Matcher matcher = compiledPattern.matcher(url.toExternalForm());

        return matcher.matches();
    }
    
    public int getResponseCode() {
        return responseCode;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
