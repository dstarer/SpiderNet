package org.huberg.SpiderNet.scheduler;

import org.huberg.SpiderNet.Context;
import org.huberg.SpiderNet.Exception.UnInitializedException;
import org.huberg.SpiderNet.Request;
import org.huberg.SpiderNet.utils.HashFunctionFactory;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by admin on 2015/6/17.
 * this only support small size urls
 */
public class QueueBasedScheduler implements Scheduler {

    private BlockingQueue<Request> blockingQueue = null;

    private BloomFilter bloomFilter = null;

    public QueueBasedScheduler(Context context) {
        bloomFilter = BloomFilter.getInstance();
        bloomFilter.setHashFunctions(HashFunctionFactory.getBKDRHashFunctions());
        blockingQueue = new LinkedBlockingQueue<Request>();
    }

    public Request poll(Thread thread) {
        Request request = null;
        try {
            request = blockingQueue.take();
        }catch (InterruptedException e) {
            e.printStackTrace();
            System.exit(-1);
        }
        return request;
    }

    public void push(Request request, Thread thread) {
        if(request == null) {
            return;
        }
        synchronized (bloomFilter) {
            try {
                if(!bloomFilter.exist(request.getUrl())){
                    bloomFilter.add(request.getUrl());
                    blockingQueue.put(request);
                }
            } catch (UnInitializedException e) {
                e.printStackTrace();
                System.exit(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
                System.exit(-1);
            }
        }
    }
}
