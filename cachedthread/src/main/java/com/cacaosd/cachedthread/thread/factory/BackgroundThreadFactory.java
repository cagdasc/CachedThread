package com.cacaosd.cachedthread.thread.factory;

import android.support.annotation.NonNull;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

public class BackgroundThreadFactory implements ThreadFactory {
    private AtomicInteger counter = new AtomicInteger(0);

    @Override
    public Thread newThread(@NonNull Runnable r) {
        return new Thread(r, "#" + counter.getAndIncrement() + " task");
    }
}
