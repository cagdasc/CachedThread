package com.cacaosd.cachedthread.thread;

import com.cacaosd.cachedthread.handler.PoolHandler;

import java.util.HashMap;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class CachedThreadPool {

    private final HashMap<String, ExecutorService> mPoolMap = new HashMap<>();
    private static final CachedThreadPool ourInstance = new CachedThreadPool();

    public static CachedThreadPool getInstance() {
        return ourInstance;
    }

    private CachedThreadPool() {
    }

    public void openNewPool(PoolHandler poolHandler) {
        if (!mPoolMap.containsKey(poolHandler.getPoolName())) {
            ThreadConfiguration threadConfiguration = poolHandler.getThreadConfiguration();
            ExecutorService executorService = new PoolExecutor(threadConfiguration);
            mPoolMap.put(poolHandler.getPoolName(), executorService);
        }
    }

    public boolean closePool(PoolHandler poolHandler) {
        ExecutorService executorService = mPoolMap.remove(poolHandler.getPoolName());
        if (executorService != null) {
            boolean terminate = false;
            try {
                if (poolHandler.getAwaitMillisTime() > 0) {
                    terminate = executorService.awaitTermination(poolHandler.getAwaitMillisTime(), TimeUnit.MILLISECONDS);
                }
                if (!terminate) {
                    executorService.shutdownNow();
                    return executorService.isShutdown();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
                return executorService.isShutdown();
            }
        }
        return true;
    }

    public boolean isClose(PoolHandler poolHandler) {
        ExecutorService executorService = mPoolMap.get(poolHandler.getPoolName());
        return executorService != null && executorService.isShutdown();
    }

    public ExecutorService getExecutorService(PoolHandler poolHandler) {
        return mPoolMap.get(poolHandler.getPoolName());
    }

    public void executeTask(Runnable runnable, PoolHandler poolHandler) {
        ExecutorService executorService = getExecutorService(poolHandler);
        if (executorService != null && !executorService.isShutdown()) {
            executorService.execute(runnable);
        } else {
            throw new IllegalStateException("Pool not exist or shutdown");
        }
    }

    public Future executeTask(Callable callable, PoolHandler poolHandler) {
        ExecutorService executorService = getExecutorService(poolHandler);
        if (executorService != null && !executorService.isShutdown()) {
            return executorService.submit(callable);
        } else {
            throw new IllegalStateException("Pool not exist or shutdown");
        }
    }

}
