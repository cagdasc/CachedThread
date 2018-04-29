package com.cacaosd.cachedthread.thread;

import java.util.concurrent.ThreadPoolExecutor;

public class PoolExecutor extends ThreadPoolExecutor {

    public PoolExecutor(ThreadConfiguration threadConfiguration) {
        super(threadConfiguration.getNumberOfCores(), threadConfiguration.getMaximumPoolSize(),
                threadConfiguration.getKeepAliveTime(), threadConfiguration.getKeepAliveTimeUnit(),
                threadConfiguration.getBlockingQueue(), threadConfiguration.getThreadFactory(),
                threadConfiguration.getRejectedExecutionHandler());
    }
}
