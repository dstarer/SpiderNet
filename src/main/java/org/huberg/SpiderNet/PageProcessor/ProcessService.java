package org.huberg.SpiderNet.PageProcessor;

import org.huberg.SpiderNet.Context;
import org.huberg.SpiderNet.Exception.UnInitializedException;
import org.huberg.SpiderNet.Page;
import org.huberg.SpiderNet.scheduler.Scheduler;

import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by admin on 2015/6/16.
 */
public class ProcessService{

    private static int THREAD_SIZE = 10;

    private ArrayList<Filter> filters = new ArrayList<Filter>();
    private Scheduler scheduler = null;

    private ExecutorService executors = null;
    private BlockingQueue<Page> pageBlockingQueue = null;
    private AtomicInteger threadCount = new AtomicInteger(0);

    private DispatcherThread dispatcherThread = new DispatcherThread();

    public ProcessService(Context context, Scheduler scheduler) {
        this.scheduler = scheduler;
        executors = Executors.newFixedThreadPool(THREAD_SIZE);
        pageBlockingQueue = context.getPages();
    }

    private class ProcessThread implements Runnable{
        private final Page page;
        public ProcessThread(Page page) {
            this.page = page;
        }

        public void run() {

            for(Filter filter: filters) {
                filter.filter(page);
            }
            threadCount.incrementAndGet();
        }
    }

    private class DispatcherThread extends Thread {
        public void run() {
            int count = 0;
            while(true) {
                while(count - threadCount.get() >= THREAD_SIZE) {
                    try{
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                // start one thread to process one page
                Page page = pageBlockingQueue.poll();
                executors.execute(new ProcessThread(page));
                count += 1;
            }
        }
    }

    public ProcessService add(Filter filter) {
        filters.add(filter);
        return this;
    }

    public void serviceStart() throws UnInitializedException{
        for(Filter filter: filters) {
            if ( !filter.checkParameter() ) {
                throw new UnInitializedException(filter.getClass().getName() + " object uninitialized properly");
            }
        }
        dispatcherThread.start();
    }
}
