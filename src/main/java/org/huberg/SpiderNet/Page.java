package org.huberg.SpiderNet;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2015/6/3.
 */
public class Page {

    private Request request;

    private String rawText;

    private List<Request> linkTable = new ArrayList<Request>();

    private String content;

    public Page() {}

    public void setRequest(Request request) {
        this.request = request;
    }

    public void setRawText(String rawText) {
        this.rawText = rawText;
    }

    public void addLink(Request e) {
        linkTable.add(e);
    }

    public void setContent(String content){
        this.content = content;
    }

    public Request getRequest() {
        return request;
    }

    public String getRawText() {
        return rawText;
    }

    public String getContent() {
        return content;
    }

    public List<Request> getLinkTable() {
        return linkTable;
    }
}
