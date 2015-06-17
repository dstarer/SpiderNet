package org.huberg.SpiderNet.PageProcessor;

import org.huberg.SpiderNet.Page;
import org.huberg.SpiderNet.Request;
import org.huberg.SpiderNet.scheduler.Scheduler;
import org.huberg.SpiderNet.utils.Tools;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

/**
 * Created by admin on 2015/6/17.
 */
public class HtmlLinkProcessor implements Filter {

    private static Scheduler scheduler = null;

    public static void setScheduler(Scheduler scheduler) {
        HtmlLinkProcessor.scheduler = scheduler;
    }

    public static Scheduler getScheduler() {
        return scheduler;
    }

    public Page filter(Page page) {
        Document document = page.getDocument();
        for(Element element : document.select("a[href]")) {
            String link = element.attr("abs:href");
            if (Tools.validUrl(link)) {
                page.addLink(link);
            }
        }
        for(String link : page.getLinkTable()) {
            Request request = new Request();
            request.setUrl(link);
            request.setDepth(page.getRequest().getDepth() + 1);
            request.setPriority(page.getRequest().getPriority() + 1);
            scheduler.push(request, Thread.currentThread());
        }
        return page;
    }

    public boolean checkParameter() {
        if (scheduler == null) {
            return  false;
        }
        return true;
    }
}
