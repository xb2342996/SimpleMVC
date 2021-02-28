package com.xxbb.simplemvc.config;

import com.xxbb.simplemvc.HandlerAdapter;
import com.xxbb.simplemvc.handler.HandlerMethod;
import com.xxbb.simplemvc.handler.RequestMappingHandlerAdapter;
import com.xxbb.simplemvc.handler.argument.HandlerMethodArgumentResolver;
import com.xxbb.simplemvc.handler.exception.ExceptionHandlerExceptionResolver;
import com.xxbb.simplemvc.handler.exception.HandlerExceptionResolver;
import com.xxbb.simplemvc.handler.interceptor.InterceptorRegistry;
import com.xxbb.simplemvc.handler.interceptor.MappedInterceptor;
import com.xxbb.simplemvc.handler.mapping.HandlerMapping;
import com.xxbb.simplemvc.handler.mapping.RequestMappingHandlerMapping;
import com.xxbb.simplemvc.handler.returnvalue.HandlerMethodReturnValueHandler;
import com.xxbb.simplemvc.view.View;
import com.xxbb.simplemvc.view.resolver.ContentNegotiatingViewResolver;
import com.xxbb.simplemvc.view.resolver.InternalResourceViewResolver;
import com.xxbb.simplemvc.view.resolver.ViewResolver;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.core.convert.ConversionService;
import org.springframework.format.FormatterRegistry;
import org.springframework.format.support.DefaultFormattingConversionService;
import org.springframework.format.support.FormattingConversionService;
import org.springframework.lang.Nullable;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class WebMvcConfigurationSupport implements ApplicationContextAware {

    private ApplicationContext applicationContext;
    private List<MappedInterceptor> interceptors;
    @Nullable
    private List<HandlerMethodArgumentResolver> argumentResolvers;
    @Nullable
    private List<HandlerMethodReturnValueHandler> returnValueHandlers;

    public ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Bean
    public FormattingConversionService mvcConversion() {
        FormattingConversionService conversionService = new DefaultFormattingConversionService();
        addFormatters(conversionService);
        return conversionService;
    }

    @Bean
    public HandlerMapping handlerMapping(FormattingConversionService conversionService) {
        RequestMappingHandlerMapping mappingHandlerMapping = new RequestMappingHandlerMapping();
        mappingHandlerMapping.setInterceptors(getInterceptors(conversionService));
        return mappingHandlerMapping;
    }

    @Bean
    public HandlerAdapter handlerAdapter(ConversionService conversionService) {
        RequestMappingHandlerAdapter handlerAdapter = new RequestMappingHandlerAdapter();
        handlerAdapter.setConversionService(conversionService);
        handlerAdapter.setCustomerArgumentResolvers(getArgumentResolvers());
        handlerAdapter.setCustomerReturnValueHandlers(getReturnValueHandlers());
        return handlerAdapter;
    }

    @Bean
    public ViewResolver viewResolver() {
        ContentNegotiatingViewResolver resolver = new ContentNegotiatingViewResolver();

        List<ViewResolver> viewResolvers = new ArrayList<>();
        addViewResolvers(viewResolvers);
        if (CollectionUtils.isEmpty(viewResolvers)) {
            resolver.setViewResolvers(Collections.singletonList(new InternalResourceViewResolver()));
        } else {
            resolver.setViewResolvers(viewResolvers);
        }

        List<View> views = new ArrayList<>();
        addDefaultViews(views);
        if (!CollectionUtils.isEmpty(views)) {
            resolver.setDefaultViews(views);
        }
        return resolver;
    }

    @Bean
    HandlerExceptionResolver handlerExceptionResolver(FormattingConversionService conversionService) {
        ExceptionHandlerExceptionResolver resolver = new ExceptionHandlerExceptionResolver();
        resolver.setCustomerArgumentResolvers(getArgumentResolvers());
        resolver.setCustomerReturnValueHandlers(getReturnValueHandlers());
        resolver.setConversionService(conversionService);
        return resolver;
    }

    protected List<MappedInterceptor> getInterceptors(FormattingConversionService service) {
        if (interceptors == null) {
            InterceptorRegistry registry = new InterceptorRegistry();
            addInterceptors(registry);
            interceptors = registry.getMappedInterceptors();
        }
        return interceptors;
    }

    @Nullable
    public List<HandlerMethodArgumentResolver> getArgumentResolvers() {
        if (argumentResolvers == null) {
            argumentResolvers = new ArrayList<>();
            addArgumentResolvers(argumentResolvers);
        }
        return argumentResolvers;
    }

    @Nullable
    public List<HandlerMethodReturnValueHandler> getReturnValueHandlers() {
        if (returnValueHandlers == null) {
            returnValueHandlers = new ArrayList<>();
            addReturnValueHandlers(returnValueHandlers);
        }
        return returnValueHandlers;
    }

    protected void addInterceptors(InterceptorRegistry registry) {

    }

    protected void addFormatters(FormatterRegistry registry) {

    }

    protected void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {

    }

    protected void addReturnValueHandlers(List<HandlerMethodReturnValueHandler> returnValueHandlers) {

    }

    protected void addViewResolvers(List<ViewResolver> viewResolvers) {

    }

    protected void addDefaultViews(List<View> views) {

    }
}
