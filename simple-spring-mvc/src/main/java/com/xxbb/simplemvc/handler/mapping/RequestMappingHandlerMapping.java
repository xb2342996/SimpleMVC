package com.xxbb.simplemvc.handler.mapping;

import com.xxbb.simplemvc.annotation.RequestMapping;
import com.xxbb.simplemvc.exception.NoHandlerFoundException;
import com.xxbb.simplemvc.handler.HandlerExecutionChain;
import com.xxbb.simplemvc.handler.HandlerMethod;
import com.xxbb.simplemvc.handler.interceptor.HandlerInterceptor;
import com.xxbb.simplemvc.handler.interceptor.MappedInterceptor;
import org.aopalliance.intercept.MethodInterceptor;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.support.ApplicationObjectSupport;
import org.springframework.core.MethodIntrospector;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.stereotype.Controller;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class RequestMappingHandlerMapping extends ApplicationObjectSupport implements HandlerMapping, InitializingBean {

    private final MappingRegistry mappingRegistry = new MappingRegistry();
    private List<MappedInterceptor> interceptors = new ArrayList<>();

    public void setInterceptors(List<MappedInterceptor> interceptors) {
        this.interceptors = interceptors;
    }

    public MappingRegistry getMappingRegistry() {
        return mappingRegistry;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        initialHandlerMapping();
    }

    private void initialHandlerMapping() {
        Map<String, Object> beansOfMap = BeanFactoryUtils.
                beansOfTypeIncludingAncestors(obtainApplicationContext(), Object.class);
        beansOfMap.entrySet().stream()
                .filter(entry -> isHandler(entry.getValue()))
                .forEach(entry -> detectHandlerMethods(entry.getKey(), entry.getValue()));
    }

    private boolean isHandler(Object handler) {
        Class<?> beanType = handler.getClass();
        return (AnnotatedElementUtils.hasAnnotation(beanType, Controller.class));
    }

    private void detectHandlerMethods(String beanName, Object handler) {
        Class<?> beanType = handler.getClass();
        Map<Method, RequestMappingInfo> methodsOfMap = MethodIntrospector
                .selectMethods(beanType, (MethodIntrospector.MetadataLookup<RequestMappingInfo>) method ->
                        getMappingForMethod(method, beanType));
        methodsOfMap.forEach(((method, requestMappingInfo) -> mappingRegistry.register(requestMappingInfo, handler, method)));
    }

    private RequestMappingInfo getMappingForMethod(Method method, Class<?> beanType) {
        RequestMapping requestMapping = AnnotatedElementUtils.findMergedAnnotation(method, RequestMapping.class);
        if (Objects.isNull(requestMapping)) {
            return null;
        }
        String prefix = getPathPrefix(beanType);
        return new RequestMappingInfo(prefix, requestMapping);
    }

    private String getPathPrefix(Class<?> beanType) {
        RequestMapping requestMapping = AnnotatedElementUtils.findMergedAnnotation(beanType, RequestMapping.class);
        if (Objects.isNull(requestMapping)) {
            return "";
        }
        return requestMapping.path();
    }

    private HandlerExecutionChain createhandlerExecutionChain(String lookupPath, HandlerMethod handler) {
        List<HandlerInterceptor> interceptors = this.interceptors.stream()
                .filter(mappedInterceptor -> mappedInterceptor.matches(lookupPath)).collect(Collectors.toList());
        return new HandlerExecutionChain(handler, interceptors);
    }

    @Override
    public HandlerExecutionChain getHandler(HttpServletRequest request) throws Exception {
        String lookupPath = request.getRequestURI();
        HandlerMethod handler = mappingRegistry.getHandlerMethodByPath(lookupPath);
        if (Objects.isNull(handler)) {
            throw new NoHandlerFoundException(request);
        }
        return createhandlerExecutionChain(lookupPath, handler);
    }
}

