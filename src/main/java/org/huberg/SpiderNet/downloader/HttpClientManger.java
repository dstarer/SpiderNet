package org.huberg.SpiderNet.downloader;

import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.protocol.HttpContext;
import org.huberg.SpiderNet.Context;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.UnknownHostException;

/**
 * Created by admin on 2015/6/9.
 */
public class HttpClientManger {
    private static HttpClientManger instance = null;
    private static PoolingHttpClientConnectionManager connectionManager = null;

    private Context context = null;

    public static HttpClientManger getInstance(Context context) {
        if (instance == null) {
            synchronized (HttpClientManger.class) {
                if (instance == null) {
                    instance = new HttpClientManger(context);
                }
            }
        }
        return instance;
    }

    public static PoolingHttpClientConnectionManager getConnectionManager() {
        return connectionManager;
    }

    private HttpClientManger(Context context) {
        Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create()
                .register("http", PlainConnectionSocketFactory.INSTANCE)
                .register("https", SSLConnectionSocketFactory.getSocketFactory())
                .build();
        this.context = context;
        connectionManager = new PoolingHttpClientConnectionManager(registry);
        connectionManager.setDefaultMaxPerRoute(100);
        connectionManager.setMaxTotal(100);
        HttpHost localhost = new HttpHost("localhost", 80);
        connectionManager.setMaxPerRoute(new HttpRoute(localhost), 50);
    }

    public synchronized CloseableHttpClient getHttpClient() {
        return HttpClients.custom()
                .setConnectionManager(connectionManager)
                .setRetryHandler(new RetryHandler(context.getRetryTimes()))
                .build();
    }

    private static class RetryHandler implements HttpRequestRetryHandler{
        private int retryTimes;

        public RetryHandler(int retryTimes) {
            this.retryTimes = retryTimes;
        }
        public boolean retryRequest(IOException exception, int executionCount, HttpContext context) {
            if (executionCount >= retryTimes) {
                return false;
            }
            if (exception instanceof InterruptedIOException) {
                return false;
            }
            if (exception instanceof UnknownHostException) {
                return false;
            }
            if (exception instanceof ConnectTimeoutException) {
                return false;
            }
            HttpClientContext clientContext = HttpClientContext.adapt(context);
            HttpRequest request = clientContext.getRequest();
            boolean idempotent = !(request instanceof HttpEntityEnclosingRequest);
            if (idempotent) {
                return true;
            }
            return false;
        }
    }
}
