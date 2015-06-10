package org.huberg.SpiderNet.downloader;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.huberg.SpiderNet.Context;
import org.huberg.SpiderNet.Page;
import org.huberg.SpiderNet.Request;

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

    private BlockingQueue<Page> pages = null;

    private PriorityBlockingQueue<Request> requests = null;

    private static HttpClientManger httpClientGenerator = null;

    private CloseableHttpClient httpClient = null;

    private ExecutorService fixedThreadPool = null;

    private AtomicLong accum = null;

    public Downloader(Context context) {
        httpClientGenerator = HttpClientManger.getInstance(context);
        pages = context.getPages();
        requests = context.getRequests();
        fixedThreadPool = Executors.newFixedThreadPool(5);
        accum = new AtomicLong(0);
    }

    @Override
    public void run(){
        // first get request
        long count = 0;
        while(true){
            Request request = requests.poll();
            //wrap the request into httpGet and start one thread do the things
            HttpGet httpGet = new HttpGet(request.getUrl());
            GetThread getThread = new GetThread(getHttpClient(), httpGet);
            fixedThreadPool.execute(getThread);
            count += 1;

            if(accum.get() - count >= 10) {
                try{
                    Thread.sleep(5000);
                }catch (InterruptedException e) {

                }
            }
        }
    }

    public CloseableHttpClient getHttpClient() {
        if (httpClient == null) {
            httpClient = httpClientGenerator.getHttpClient();
        }
        return httpClient;
    }

    public class GetThread implements Runnable {
        private final HttpGet httpGet;
        private final CloseableHttpClient httpClient;
        public GetThread(CloseableHttpClient httpClient, HttpGet httpGet) {
            this.httpClient = httpClient;
            this.httpGet = httpGet;
        }
        public void run() {
            try{
                CloseableHttpResponse response = httpClient.execute(httpGet);
                //save the response to web page
                response.close();
            }catch (IOException e) {

            }
            accum.incrementAndGet();
        }
    }
}
