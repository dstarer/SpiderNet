package org.huberg.SpiderNet.downloader;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.huberg.SpiderNet.Context;
import org.huberg.SpiderNet.Page;
import org.huberg.SpiderNet.Request;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by admin on 2015/6/9.
 */
public class Downloader extends Thread{

    private String threadName = null;

    private BlockingQueue<Page> pages = null;

    private PriorityBlockingQueue<Request> requests = null;

    private static HttpClientManger httpClientGenerator = null;

    private CloseableHttpClient httpClient = null;

    private ExecutorService fixedThreadPool = null;

    private AtomicLong accum = null;

    public Downloader(Context context, String threadName) {
        httpClientGenerator = HttpClientManger.getInstance(context);
        pages = context.getPages();
        requests = context.getRequests();
        fixedThreadPool = Executors.newFixedThreadPool(5);
        accum = new AtomicLong(0);
        this.threadName = threadName;
    }

    @Override
    public void run(){
        // first get request
        long count = 0;
        while(true){
            Request request = requests.poll();
            //wrap the request into httpGet and start one thread do the things
//            HttpGet httpGet = new HttpGet(request.getUrl());
            GetThread getThread = new GetThread(getHttpClient(), request);
            fixedThreadPool.execute(getThread);
            count += 1;
            try{
                Thread.sleep(1000);
            }catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public CloseableHttpClient getHttpClient() {
        if (httpClient == null) {
            if(httpClientGenerator==null) {
                throw new InternalError("httpClientGenerator in Downloader does not initialize properly");
            }
            httpClient = httpClientGenerator.getHttpClient();
        }
        return httpClient;
    }

    public void handleResponse(Request request, CloseableHttpResponse httpResponse) throws IOException{
        String content = getContent(httpResponse);
        Page page = new Page();
        page.setRawText(content);
        page.setRequest(request);
        page.setDownloadThread(this.threadName);
        // put the page into queue, and then process the other request
        try {
            pages.put(page);
        }catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public String getContent(CloseableHttpResponse httpResponse) throws IOException {
        byte[] contentBytes = IOUtils.toByteArray(httpResponse.getEntity().getContent());
        String charset = getHtmlCharset(httpResponse, contentBytes);
        return new String(contentBytes, charset);
    }

    public String getHtmlCharset(CloseableHttpResponse httpResponse, byte[] contentBytes) throws IOException {

        //encoding in http header
        String value = httpResponse.getEntity().getContentEncoding().getValue();
        if(StringUtils.isNotBlank(value) && StringUtils.isNotEmpty(value)) {
            return value;
        }
        String content = new String(contentBytes, "utf-8");
        if (StringUtils.isNotEmpty(content)) {
            Document document = Jsoup.parse(content);
            Elements links = document.select("meta");
            for(Element link: links) {
                String metaContent = link.attr("content");
                String metaCharset = link.attr("charset");
                if(metaContent.indexOf("charset") != -1) {
                    metaContent = metaContent.substring(metaContent.indexOf("charset"), metaContent.length());
                    metaCharset = metaContent.split("=")[1];
                    return metaCharset;
                }
                else if (StringUtils.isNotEmpty(metaCharset)) {
                    return metaCharset;
                }
            }
        }
        return "utf-8";
    }

    public class GetThread implements Runnable {
        private final HttpGet httpGet;
        private final CloseableHttpClient httpClient;
        private final Request request;

        public GetThread(CloseableHttpClient httpClient, Request request) {
            this.httpClient = httpClient;
            this.httpGet = new HttpGet(request.getUrl());
            this.request = request;
        }
        public void run() {
            try{
                CloseableHttpResponse response = httpClient.execute(httpGet);
                //save the response to web page
                handleResponse(request, response);
                response.close();
            }catch (IOException e) {
                e.printStackTrace();
            }
            accum.incrementAndGet();
        }
    }
}
