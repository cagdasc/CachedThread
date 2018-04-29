package com.cacaosd.cachedthread.capsule.processor;

import android.support.annotation.NonNull;

import com.cacaosd.cachedthread.capsule.exception.MethodCallException;
import com.cacaosd.cachedthread.capsule.exception.MethodNotExistError;
import com.cacaosd.cachedthread.capsule.model.MethodContainer;
import com.cacaosd.cachedthread.handler.PoolHandler;
import com.cacaosd.cachedthread.model.output.BaseRestOutput;
import com.cacaosd.cachedthread.task.CallablePoolTask;
import com.cacaosd.cachedthread.task.TaskWrapper;
import com.cacaosd.cachedthread.thread.CachedThreadPool;
import com.cacaosd.cachedthread.thread.ThreadConfiguration;
import com.cacaosd.cachedthread.thread.factory.BackgroundThreadFactory;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class CacheMethod {

    private static CacheMethodProvider sCacheMethodProvider;
    private ExecutorService mExecutorService;

    protected CacheMethod(ExecutorService mExecutorService){
        this.mExecutorService = mExecutorService;

    }

    public static CacheMethodProvider of(@NonNull Object object, @NonNull ExecutorService mExecutorService){
        CacheMethod cacheMethod = new CacheMethod(mExecutorService);
        sCacheMethodProvider = new CacheMethodProvider(cacheMethod, object);
        return sCacheMethodProvider;
    }

    public TaskWrapper<BaseRestOutput> callMethod(@NonNull Object object, int id) {
        return callMethod(object, id);
    }

    public TaskWrapper<BaseRestOutput> callMethod(@NonNull final Object object, final int id, @NonNull final List<Object> parameterList) {
        CallablePoolTask<BaseRestOutput> callablePoolTask = new CallablePoolTask<BaseRestOutput>() {
            @Override
            public BaseRestOutput onProcess() throws Exception {
                return callableMethod(object, id, parameterList);
            }
        };
        return new TaskWrapper<BaseRestOutput>(mExecutorService, callablePoolTask);
    }

    public BaseRestOutput callableMethod(@NonNull final Object object, final int id, @NonNull final List<Object> parameterList) throws Exception {
        MethodContainer methodContainer = sCacheMethodProvider.getMethodCache().get(id);
        if (methodContainer == null)
            throw new MethodNotExistError("Method not exist. Id: " + id);

        if (parameterList.size() > 0) {
            CheckMethod.checkParametersType(methodContainer.getMethod(), parameterList);
            Object[] args = new Object[parameterList.size()];
            args = parameterList.toArray(args);
            try {
                return (BaseRestOutput) methodContainer.getMethod().invoke(object, args);
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new MethodCallException("Method call error.");
            }
        } else {
            try {
                return (BaseRestOutput) methodContainer.getMethod().invoke(object);
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new MethodCallException("Method call error.");
            }
        }
    }

    public void destroy() {
        sCacheMethodProvider.getMethodCache().clear();
    }
}
