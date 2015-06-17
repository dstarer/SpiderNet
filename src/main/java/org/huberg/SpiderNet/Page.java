package org.huberg.SpiderNet;

import org.jsoup.nodes.Document;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by admin on 2015/6/3.
 */
public class Page {

    private Request request;

    private String rawText;

    private List<String> linkTable = new LinkedList<String>();

    private String content;

    private String downloadThread;

    private Document document = null;

    public String getDownloadThread() {
        return downloadThread;
    }

    public Page() {}

    public void setDownloadThread(String threadName) {
        downloadThread = threadName;
    }

    public void setRequest(Request request) {
        this.request = request;
    }

    public Request getRequest() {
        return request;
    }

    public void setRawText(String rawText) {
        this.rawText = rawText;
    }

    public String getRawText() {
        return rawText;
    }

    public void setContent(String content){
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void addLink(String e) {
        linkTable.add(e);
    }

    public List<String> getLinkTable() {
        return linkTable;
    }

    public Document getDocument() {
        return document;
    }

    public void setDocument(Document document) {
        this.document = document;
    }
}
