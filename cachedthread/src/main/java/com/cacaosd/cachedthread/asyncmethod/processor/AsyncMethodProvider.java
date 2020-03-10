package com.cacaosd.cachedthread.asyncmethod.processor;

import androidx.annotation.NonNull;

import com.cacaosd.cachedthread.asyncmethod.annotation.AsyncMethod;
import com.cacaosd.cachedthread.asyncmethod.exception.DuplicatedMethodException;
import com.cacaosd.cachedthread.asyncmethod.model.MethodContainer;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class AsyncMethodProvider {

    private AsyncMethodCaller mAsyncMethodCaller;
    private HashMap<Integer, MethodContainer> mMethodCache = new HashMap<>();
    private Class<?> mObject;

    public AsyncMethodProvider(@NonNull AsyncMethodCaller mAsyncMethodCaller, @NonNull Class<?> mObject) {
        this.mAsyncMethodCaller = mAsyncMethodCaller;
        this.mObject = mObject;
    }

    public AsyncMethodCaller init() {
        Method[] methods = mObject.getDeclaredMethods();
        CheckMethod.isAnnotatedMethodExist(methods);
        for (Method method : methods) {
            Annotation[] declaredAnnotations = method.getDeclaredAnnotations();
            for (Annotation annotation : declaredAnnotations) {
                if (AsyncMethod.class.equals(annotation.annotationType())) {
                    AsyncMethod task = method.getAnnotation(AsyncMethod.class);
                    if (task == null || task.id() == -1)
                        continue;
                    MethodContainer methodContainer = new MethodContainer();
                    methodContainer.setId(task.id());
                    methodContainer.setMethod(method);
                    if (CheckMethod.hasParameters(method)) {
                        Class[] params = method.getParameterTypes();
                        List<Class> parameterTypes = Arrays.asList(params);
                        methodContainer.setParameterTypes(parameterTypes);
                    }
                    if (!mMethodCache.containsKey(methodContainer.getId())) {
                        mMethodCache.put(methodContainer.getId(), methodContainer);
                    } else {
                        throw new DuplicatedMethodException("Method id was exist.");
                    }
                }
            }
        }
        return mAsyncMethodCaller;
    }

    public HashMap<Integer, MethodContainer> getMethodCache() {
        return mMethodCache;
    }
}
