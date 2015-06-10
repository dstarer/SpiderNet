package org.huberg.SpiderNet;

import org.huberg.SpiderNet.utils.Configuration;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;

/**
 * Created by admin on 2015/6/9.
 */
public class Context {

    private BlockingQueue<Page> pages = null;

    private PriorityBlockingQueue<Request> requests = null;

    private Configuration configuration = null;

    private int retryTimes = 0;

    public int getRetryTimes() {
        return this.retryTimes;
    }

    public void setRetryTimes(int retryTimes) {
        this.retryTimes = retryTimes;
    }

    public BlockingQueue<Page> getPages() {
        return pages;
    }

    public void setPages(BlockingQueue<Page> pages) {
        this.pages = pages;
    }

    public PriorityBlockingQueue<Request> getRequests() {
        return requests;
    }

    public void setRequests(PriorityBlockingQueue<Request> requests) {
        this.requests = requests;
    }

    public Configuration getConfiguration() {
        return configuration;
    }

    public void setConfiguration(Configuration configuration) {
        this.configuration = configuration;
    }
}
