package com.cacaosd.cachedthread.capsule.processor;

import com.cacaosd.cachedthread.capsule.exception.AnnotatedMethodException;
import com.cacaosd.cachedthread.capsule.exception.MethodNotExistError;
import com.cacaosd.cachedthread.capsule.exception.MethodParameterMisMatchException;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.List;

public class CheckMethod {

    public static void isAnnotatedMethodExist(Method[] methods) {
        if (methods == null || methods.length == 0) {
            throw  new MethodNotExistError("Method not exist");
        }
        boolean anymethodFound = false;
        int i = 0;
        while (i < methods.length && !anymethodFound) {
            Annotation[] declaredAnnotations = methods[i].getDeclaredAnnotations();
            if (declaredAnnotations != null && declaredAnnotations.length > 0) {
                anymethodFound = true;
            }
            i++;
        }

        if (!anymethodFound) {
            throw new AnnotatedMethodException("Annotated methods not exist");
        }
    }

    public static boolean hasParameters(Method method) {
        return !(method.getParameterTypes() == null || method.getParameterTypes().length == 0);
    }

    public static void checkParametersType(Method method, List<Object> parameters) {
        Class[] types = method.getParameterTypes();
        if (parameters.size() != types.length)
            throw new MethodParameterMisMatchException("Method parameters length mismatch!");
        int length = parameters.size();
        for (int i = 0; i < length; i++) {
            if (!parameters.get(i).getClass().equals(types[i])) {
                throw new MethodParameterMisMatchException("Method parameters type mismatch!");
            }
        }
    };
}
