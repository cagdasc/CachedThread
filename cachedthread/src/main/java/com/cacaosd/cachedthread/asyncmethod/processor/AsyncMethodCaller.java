package com.cacaosd.cachedthread.asyncmethod.processor;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.cacaosd.cachedthread.asyncmethod.exception.MethodCallException;
import com.cacaosd.cachedthread.asyncmethod.exception.MethodNotExistError;
import com.cacaosd.cachedthread.asyncmethod.model.MethodContainer;
import com.cacaosd.cachedthread.model.output.BaseRestOutput;
import com.cacaosd.cachedthread.task.CallablePoolTask;
import com.cacaosd.cachedthread.task.TaskWrapper;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.concurrent.ExecutorService;

public class AsyncMethodCaller {

    private static AsyncMethodProvider sAsyncMethodProvider;
    private ExecutorService mExecutorService;

    protected AsyncMethodCaller(ExecutorService mExecutorService) {
        this.mExecutorService = mExecutorService;

    }

    public static AsyncMethodProvider of(@NonNull Class<?> object, @NonNull ExecutorService mExecutorService) {
        AsyncMethodCaller asyncMethodCaller = new AsyncMethodCaller(mExecutorService);
        sAsyncMethodProvider = new AsyncMethodProvider(asyncMethodCaller, object);
        return sAsyncMethodProvider;
    }

    public TaskWrapper<BaseRestOutput> callMethod(@NonNull Object object, int id) {
        return callMethod(object, id, null);
    }

    public TaskWrapper<BaseRestOutput> callMethod(@NonNull final Object object, final int id, @Nullable final List<Object> parameterList) {
        CallablePoolTask<BaseRestOutput> callablePoolTask = new CallablePoolTask<BaseRestOutput>() {
            @Override
            public BaseRestOutput onProcess() throws Exception {
                return callableMethod(object, id, parameterList);
            }
        };
        return new TaskWrapper<BaseRestOutput>(mExecutorService, callablePoolTask);
    }

    private BaseRestOutput callableMethod(@NonNull final Object object, final int id, final List<Object> parameterList) {
        MethodContainer methodContainer = sAsyncMethodProvider.getMethodCache().get(id);
        if (methodContainer == null)
            throw new MethodNotExistError("Method not exist. Id: " + id);

        if (parameterList != null && !parameterList.isEmpty()) {
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
        sAsyncMethodProvider.getMethodCache().clear();
    }
}
