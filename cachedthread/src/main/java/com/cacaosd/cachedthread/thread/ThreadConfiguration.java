package com.cacaosd.cachedthread.thread;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

public class ThreadConfiguration {
    
    private int mNumberOfCores;
    private int mMaximumPoolSize;
    private int mKeepAliveTime;
    private TimeUnit mKeepAliveTimeUnit;
    private BlockingQueue<Runnable> mBlockingQueue;
    private ThreadFactory mThreadFactory;
    private RejectedExecutionHandler mRejectedExecutionHandler;

    private ThreadConfiguration(Builder builder) {
        mNumberOfCores = builder.mNumberOfCores;
        mMaximumPoolSize = builder.mMaximumPoolSize;
        mKeepAliveTime = builder.mKeepAliveTime;
        mKeepAliveTimeUnit = builder.mKeepAliveTimeUnit;
        mBlockingQueue = builder.mBlockingQueue;
        mThreadFactory = builder.mThreadFactory;
        mRejectedExecutionHandler = builder.mRejectedExecutionHandler;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public int getNumberOfCores() {
        return mNumberOfCores;
    }

    public int getMaximumPoolSize() {
        return mMaximumPoolSize;
    }

    public int getKeepAliveTime() {
        return mKeepAliveTime;
    }

    public TimeUnit getKeepAliveTimeUnit() {
        return mKeepAliveTimeUnit;
    }

    public BlockingQueue<Runnable> getBlockingQueue() {
        return mBlockingQueue;
    }

    public ThreadFactory getThreadFactory() {
        return mThreadFactory;
    }

    public RejectedExecutionHandler getRejectedExecutionHandler() {
        return mRejectedExecutionHandler;
    }


    public static class Builder {
        private int mNumberOfCores;
        private int mMaximumPoolSize;
        private int mKeepAliveTime;
        private TimeUnit mKeepAliveTimeUnit;
        private BlockingQueue<Runnable> mBlockingQueue;
        private ThreadFactory mThreadFactory;
        private RejectedExecutionHandler mRejectedExecutionHandler;

        private Builder() {
        }

        public Builder setNumberOfCores(int mNumberOfCores) {
            this.mNumberOfCores = mNumberOfCores;
            return this;
        }

        public Builder setMaximumPoolSize(int mMaximumPoolSize) {
            this.mMaximumPoolSize = mMaximumPoolSize;
            return this;
        }

        public Builder setKeepAliveTime(int mKeepAliveTime) {
            this.mKeepAliveTime = mKeepAliveTime;
            return this;
        }

        public Builder setKeepAliveTimeUnit(TimeUnit mKeepAliveTimeUnit) {
            this.mKeepAliveTimeUnit = mKeepAliveTimeUnit;
            return this;
        }

        public Builder setBlockingQueue(BlockingQueue<Runnable> mBlockingQueue) {
            this.mBlockingQueue = mBlockingQueue;
            return this;
        }

        public Builder setThreadFactory(ThreadFactory mThreadFactory) {
            this.mThreadFactory = mThreadFactory;
            return this;
        }

        public Builder setRejectedExecutionHandler(RejectedExecutionHandler mRejectedExecutionHandler) {
            this.mRejectedExecutionHandler = mRejectedExecutionHandler;
            return this;
        }

        public ThreadConfiguration build() {
            return new ThreadConfiguration(this);
        }
    }
}
