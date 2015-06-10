package org.huberg.SpiderNet;

import java.util.*;

/**
 * Created by admin on 2015/6/3.
 */
public class Site {

    private String domain = null;

    private String userAgent = null;

    private String charset = null;

    private int sleepTime;

    private int timeOut = 5000;

    private static final Set<Integer> DEFAULT_STATUS_CODE_SET = new HashSet<Integer>();

    private Map<String, String> defaultCookies = new LinkedHashMap<String, String>();

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void addHeaders(String key, String value) {
        headers.put(key, value);
    }

    private Map<String, String> headers = new HashMap<String, String>();

    static {
        DEFAULT_STATUS_CODE_SET.add(200);
    }

    private Site() {}

    public static Site getInstance() {
        return new Site();
    }

    public Site addCookies(String name, String value) {
        defaultCookies.put(name, value);
        return this;
    }

    public Site setUserAgent(String userAgent) {
        this.userAgent = userAgent;
        return this;
    }

    public Map<String, String> getCookies() {
        return defaultCookies;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public String getDomain() {
        return domain;
    }

    public Site setDomain() {
        this.domain = domain;
        return this;
    }

    public Site setCharset(String charset) {
        this.charset = charset;
        return this;
    }

    public String getCharset() {
        return this.charset;
    }

    public Site setTimeOut(int timeOut) {
        this.timeOut = timeOut;
        return this;
    }

    public Site setSleepTime(int sleepTime) {
        this.sleepTime = sleepTime;
        return this;
    }

    public int getSleepTime() {
        return this.sleepTime;
    }

    public int getTimeOut() {
        return this.timeOut;
    }

}


