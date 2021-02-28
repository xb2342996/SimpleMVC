package com.xxbb.simplemvc.handler.exception;

import com.xxbb.simplemvc.annotation.ControllerAdvice;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ControllerAdviceBean {
    private String beanName;
    private Class<?> beanType;
    private Object bean;

    public ControllerAdviceBean(String beanName, Object bean) {
        Assert.notNull(bean, "Bean must be not null");
        this.beanName = beanName;
        this.beanType = bean.getClass();
        this.bean = bean;
    }

    public static List<ControllerAdviceBean> findAnnotatedBeans(ApplicationContext context) {
        Map<String, Object> beanMap = BeanFactoryUtils.beansOfTypeIncludingAncestors(context, Object.class);
        return beanMap.entrySet().stream()
                .filter(entry -> hasControllerAdvice(entry.getValue()))
                .map(entry -> new ControllerAdviceBean(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
    }

    private static boolean hasControllerAdvice(Object bean) {
        Class<?> beanType = bean.getClass();
        return AnnotatedElementUtils.hasAnnotation(beanType, ControllerAdvice.class);
    }

    public String getBeanName() {
        return beanName;
    }

    public void setBeanName(String beanName) {
        this.beanName = beanName;
    }

    public Class<?> getBeanType() {
        return beanType;
    }

    public void setBeanType(Class<?> beanType) {
        this.beanType = beanType;
    }

    public Object getBean() {
        return bean;
    }

    public void setBean(Object bean) {
        this.bean = bean;
    }
}
