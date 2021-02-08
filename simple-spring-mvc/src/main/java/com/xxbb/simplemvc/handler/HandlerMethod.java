package com.xxbb.simplemvc.handler;

import org.springframework.core.MethodParameter;
import org.springframework.util.Assert;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class HandlerMethod {
    private final Object bean;
    private final Class<?> beanType;
    private final Method method;

    private final List<MethodParameter> parameters;

    public HandlerMethod(Object bean, Method method) {
        this.bean = bean;
        this.method = method;
        this.beanType = bean.getClass();

        this.parameters = new ArrayList<>();
        for (int i = 0; i < method.getParameterCount(); i++) {
            parameters.add(new MethodParameter(method, i));
        }
    }

    public HandlerMethod(HandlerMethod handlerMethod) {
        Assert.notNull(handlerMethod, "HandlerMethod cannot be empty!");
        this.bean = handlerMethod.bean;
        this.method = handlerMethod.method;
        this.beanType = handlerMethod.beanType;
        this.parameters = handlerMethod.parameters;
    }

    public Object getBean() {
        return bean;
    }

    public Class<?> getBeanType() {
        return beanType;
    }

    public Method getMethod() {
        return method;
    }

    public List<MethodParameter> getParameters() {
        return parameters;
    }
}
