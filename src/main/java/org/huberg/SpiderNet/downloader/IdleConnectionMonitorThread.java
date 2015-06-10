package org.huberg.SpiderNet.downloader;

import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;

import java.util.concurrent.TimeUnit;

/**
 * Created by admin on 2015/6/9.
 */
public class IdleConnectionMonitorThread extends Thread{
    private volatile boolean shutdown;
    private PoolingHttpClientConnectionManager manger = null;
    public IdleConnectionMonitorThread(PoolingHttpClientConnectionManager manger) {
        if(manger==null) {
            throw new IllegalArgumentException("IdleConnectionMonitorThread constructor parameter error! " + manger);
        }
        this.manger = manger;
    }
    public void run() {
        try{
            while(!shutdown) {
                synchronized (this) {
                    wait(5000);
                    this.manger.closeExpiredConnections();
                    this.manger.closeIdleConnections(30, TimeUnit.SECONDS);
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void shutdown() {
        shutdown = true;
        synchronized (this) {
            notifyAll();
        }
    }
}
