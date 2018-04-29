package com.cacaosd.cachedthread.handler;

import com.cacaosd.cachedthread.thread.ThreadConfiguration;

public interface PoolHandler {

    String getPoolName();

    int getAwaitMillisTime();

    ThreadConfiguration getThreadConfiguration();
}
