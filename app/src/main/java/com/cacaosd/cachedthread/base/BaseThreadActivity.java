package com.cacaosd.cachedthread.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.cacaosd.cachedthread.handler.PoolHandler;
import com.cacaosd.cachedthread.thread.CachedThreadPool;
import com.cacaosd.cachedthread.thread.ThreadConfiguration;
import com.cacaosd.cachedthread.thread.factory.BackgroundThreadFactory;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public abstract class BaseThreadActivity extends AppCompatActivity implements PoolHandler {

    private CachedThreadPool mCachedThreadPool = CachedThreadPool.getInstance();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCachedThreadPool.openNewPool(this);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mCachedThreadPool.closePool(this);
    }

    public CachedThreadPool getCachedThreadPool() {
        return mCachedThreadPool;
    }

    @Override
    public String getPoolName() {
        return this.getClass().getSimpleName(); // Uniqe pool name.
    }

    @Override
    public int getAwaitMillisTime() {
        return 300; // Await time before close pool.
    }

    @Override
    public ThreadConfiguration getThreadConfiguration() {
        // Your thread configuration.
        BlockingQueue<Runnable> taskQueue = new LinkedBlockingQueue<>();
        int numberOfCores = Runtime.getRuntime().availableProcessors();
        return ThreadConfiguration.newBuilder()
                .setNumberOfCores(numberOfCores)
                .setMaximumPoolSize(numberOfCores * 2)
                .setKeepAliveTime(2)
                .setKeepAliveTimeUnit(TimeUnit.SECONDS)
                .setBlockingQueue(taskQueue)
                .setThreadFactory(new BackgroundThreadFactory())
                .setRejectedExecutionHandler(new ThreadPoolExecutor.AbortPolicy())
                .build();
    }
}
