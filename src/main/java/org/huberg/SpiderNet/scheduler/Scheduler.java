package org.huberg.SpiderNet.scheduler;
import org.huberg.SpiderNet.Request;

/**
 * Created by admin on 2015/6/3.
 */
public interface Scheduler {
    /**
     * push new request to the pool
     * @param request
     * @param thread
     */
    public void push(Request request, Thread thread);
    /**
     * get one request to a given thread
     * @param thread
     * @return
     */
    public Request poll(Thread thread);
}
