package com.cacaosd.cachedthread.capsule.processor;

import android.support.annotation.NonNull;

import com.cacaosd.cachedthread.capsule.annotation.Task;
import com.cacaosd.cachedthread.capsule.exception.DuplicatedMethodException;
import com.cacaosd.cachedthread.capsule.model.MethodContainer;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class CacheMethodProvider {

    private CacheMethod mCacheMethod;
    private HashMap<Integer, MethodContainer> mMethodCache = new HashMap<>();
    private Object mObject;

    public CacheMethodProvider(@NonNull CacheMethod mCacheMethod, @NonNull Object mObject) {
        this.mCacheMethod = mCacheMethod;
        this.mObject = mObject;
    }

    public CacheMethod init() {
        Method[] methods = mObject.getClass().getDeclaredMethods();
        CheckMethod.isAnnotatedMethodExist(methods);
        for (Method method : methods) {
            Annotation[] declaredAnnotations = method.getDeclaredAnnotations();
            for (Annotation annotation : declaredAnnotations) {
                if (Task.class.equals(annotation.annotationType())) {
                    Task task = method.getAnnotation(Task.class);
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
        return mCacheMethod;
    }

    public HashMap<Integer, MethodContainer> getMethodCache() {
        return mMethodCache;
    }
}
