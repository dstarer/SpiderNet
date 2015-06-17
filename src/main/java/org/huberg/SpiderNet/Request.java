package org.huberg.SpiderNet;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by admin on 2015/6/3.
 */
public class Request {

    private String url = null;

    private String method = null;

    private Map<String, Object> extras = null;

    private long priority = 0;

    private int depth = 0;

    public Request() {
    }

    public int getDepth() {
        return depth;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }

    public Request(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public Map<String, Object> getExtras() {
        return extras;
    }

    public void setExtras(Map<String, Object> extras) {
        this.extras = extras;
    }

    public long getPriority() {
        return priority;
    }

    public void setPriority(long priority) {
        this.priority = priority;
    }

    public void addExtra(String key, Object v) {
        if(extras == null) {
            extras = new HashMap<String, Object>();
        }
        extras.put(key, v);
    }
    public Object getExtra(String key) {
        if (extras == null) {
            return null;
        }
        return extras.get(key);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj != null && obj.getClass() != Request.class) {
            return false;
        }
        Request request = (Request) obj;
        if (url.equals(request.getUrl())) {
            return true;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return url.hashCode();
    }

    @Override
    public String toString() {
        return "Request {" + "url: " + url  + ", method: " + method + ", extras: " + extras + ", priority: "
                + priority + "}";
    }
}
