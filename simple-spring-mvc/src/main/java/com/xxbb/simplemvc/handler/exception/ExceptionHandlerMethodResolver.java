package com.xxbb.simplemvc.handler.exception;

import com.xxbb.simplemvc.annotation.ExceptionHandler;
import org.springframework.core.MethodIntrospector;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.util.Assert;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ExceptionHandlerMethodResolver {
    public static final ReflectionUtils.MethodFilter EXCEPTHON_HANDLER_METHODS = method -> AnnotatedElementUtils.hasAnnotation(method, ExceptionHandler.class);
    private final Map<Class<? extends Throwable>, Method> mappedMethods = new ConcurrentHashMap<>(16);

    public ExceptionHandlerMethodResolver(Class<?> handlerType) {
        for (Method method : MethodIntrospector.selectMethods(handlerType, EXCEPTHON_HANDLER_METHODS)) {
            for (Class<? extends Throwable> exceptionType : detectExceptionMappings(method)){
                this.mappedMethods.put(exceptionType, method);
            }
        }
    }

    private List<Class<? extends Throwable>> detectExceptionMappings(Method method) {
        ExceptionHandler ann = AnnotatedElementUtils.findMergedAnnotation(method, ExceptionHandler.class);
        Assert.state(ann != null, "No ExceptionHandler annotation");
        return Arrays.asList(ann.value());
    }

    public Map<Class<? extends Throwable>, Method> getMappedMethods() {
        return mappedMethods;
    }

    public boolean hasExceptionMappings() {
        return !mappedMethods.isEmpty();
    }

    public Method resolveMethod(Exception exception) {
        Method method = resolveMethodByExceptionType(exception.getClass());
        if (method == null) {
            Throwable cause = exception.getCause();
            if (cause != null) {
                method = resolveMethodByExceptionType(cause.getClass());
            }
        }
        return method;
    }

    private Method resolveMethodByExceptionType(Class<? extends Throwable> exceptionClass) {
        return mappedMethods.get(exceptionClass);
    }
}
