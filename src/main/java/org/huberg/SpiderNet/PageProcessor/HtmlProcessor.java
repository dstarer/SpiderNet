package org.huberg.SpiderNet.PageProcessor;

import org.huberg.SpiderNet.Context;
import org.huberg.SpiderNet.Page;
import org.huberg.SpiderNet.Request;
import org.huberg.SpiderNet.scheduler.Scheduler;
import org.huberg.SpiderNet.utils.HttpConstant;
import org.huberg.SpiderNet.utils.Tools;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by admin on 2015/6/10.
 */
public class HtmlProcessor extends Thread{
    private ExecutorService processorPool = null;
    private BlockingQueue<Page> queue = null;
    private Scheduler scheduler = null;

    public HtmlProcessor(Context context, Scheduler scheduler) {
        queue = context.getPages();
        processorPool = Executors.newFixedThreadPool(10);
        this.scheduler = scheduler;
    }

    @Override
    public void run() {
        while(true) {
            Page page = queue.poll();
        }
    }
    public class ProcessThread implements Runnable {
        private final Page page;
        public ProcessThread(Page page) {
            this.page = page;
        }
        public void run() {
            Document document = Jsoup.parse(page.getContent(), page.getRequest().toString());
            for (Element element: document.select("a[href]")) {
                String url = element.attr("abs:href");
                if(Tools.validUrl(url)) {
                    pushRequest(url, page);
                }
            }
        }
    }
    public void pushRequest(String url, Page page) {
        Request request = new Request();
        request.setMethod(HttpConstant.Method.GET);
        request.setUrl(url);
        request.setDepth(page.getRequest().getDepth() + 1);
        request.setPriority(page.getRequest().getPriority() + 1);
        scheduler.push(request, this);
    }
}
