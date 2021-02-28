package com.xxbb.simplemvc.config;

import com.xxbb.simplemvc.DispatcherServlet;
import com.xxbb.simplemvc.HandlerAdapter;
import com.xxbb.simplemvc.annotation.RequestMapping;
import com.xxbb.simplemvc.handler.RequestMappingHandlerAdapter;
import com.xxbb.simplemvc.handler.interceptor.InterceptorRegistry;
import com.xxbb.simplemvc.handler.mapping.HandlerMapping;
import com.xxbb.simplemvc.handler.mapping.RequestMappingHandlerMapping;
import com.xxbb.simplemvc.interceptor.Test2HandlerInterceptor;
import com.xxbb.simplemvc.interceptor.TestHandlerInterceptor;
import com.xxbb.simplemvc.view.resolver.ContentNegotiatingViewResolver;
import com.xxbb.simplemvc.view.resolver.InternalResourceViewResolver;
import com.xxbb.simplemvc.view.resolver.ViewResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.format.datetime.DateFormatter;
import org.springframework.format.support.DefaultFormattingConversionService;

import java.text.DateFormat;
import java.util.Collections;

@Configuration
@ComponentScan(basePackages = "com.xxbb.simplemvc")
public class AppConfig {
    @Bean
    public HandlerMapping handlerMapping() {
        return new RequestMappingHandlerMapping();
    }

//    @Bean
//    public RequestMappingHandlerMapping interceptorHandlerMapping() {
//        InterceptorRegistry interceptorRegistry = new InterceptorRegistry();
//
//        TestHandlerInterceptor interceptor = new TestHandlerInterceptor();
//        interceptorRegistry.addInterceptor(interceptor).addExcludePatterns("/ex_test").addIncludePatterns("/in_test1");
//
//        Test2HandlerInterceptor interceptor2 = new Test2HandlerInterceptor();
//        interceptorRegistry.addInterceptor(interceptor2).addIncludePatterns("/in_test2", "/in_test3");
//
//        RequestMappingHandlerMapping mappingHandlerMapping = new RequestMappingHandlerMapping();
//        mappingHandlerMapping.setInterceptors(interceptorRegistry.getMappedInterceptors());
//        return mappingHandlerMapping;
//    }

    @Bean
    public HandlerAdapter handlerAdapter(ConversionService conversionService) {
        RequestMappingHandlerAdapter handlerAdapter = new RequestMappingHandlerAdapter();
        handlerAdapter.setConversionService(conversionService);
        return handlerAdapter;
    }

    @Bean
    public ConversionService conversionService() {
        DefaultFormattingConversionService conversionService = new DefaultFormattingConversionService();
        DateFormatter dateFormatter = new DateFormatter();
        dateFormatter.setPattern("yyyy-MM-dd HH:mm:ss");
        conversionService.addFormatter(dateFormatter);
        return conversionService;
    }

    @Bean
    public ViewResolver viewResolver() {
        ContentNegotiatingViewResolver negotiatingViewResolver = new ContentNegotiatingViewResolver();
        negotiatingViewResolver.setViewResolvers(Collections.singletonList(new InternalResourceViewResolver()));
        return negotiatingViewResolver;
    }

    @Bean
    public DispatcherServlet dispatcherServlet() {
        return new DispatcherServlet();
    }
}
