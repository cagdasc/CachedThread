package com.cacaosd.cachedthread.capsule.model;

import java.lang.reflect.Method;
import java.util.List;

public class MethodContainer {
    private int id;
    private Method method;
    private List<Class> parameterTypes;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public List<Class> getParameterTypes() {
        return parameterTypes;
    }

    public void setParameterTypes(List<Class> parameterTypes) {
        this.parameterTypes = parameterTypes;
    }
}
