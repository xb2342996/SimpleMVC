package com.xxbb.simplemvc.config;

import com.xxbb.simplemvc.annotation.RequestMapping;
import com.xxbb.simplemvc.handler.interceptor.InterceptorRegistry;
import com.xxbb.simplemvc.handler.mapping.RequestMappingHandlerMapping;
import com.xxbb.simplemvc.interceptor.Test2HandlerInterceptor;
import com.xxbb.simplemvc.interceptor.TestHandlerInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = "com.xxbb.simplemvc")
public class AppConfig {
    @Bean
    public RequestMappingHandlerMapping handlerMapping() {
        return new RequestMappingHandlerMapping();
    }

    @Bean
    public RequestMappingHandlerMapping interceptorHandlerMapping() {
        InterceptorRegistry interceptorRegistry = new InterceptorRegistry();

        TestHandlerInterceptor interceptor = new TestHandlerInterceptor();
        interceptorRegistry.addInterceptor(interceptor).addExcludePatterns("/ex_test").addIncludePatterns("/in_test1");

        Test2HandlerInterceptor interceptor2 = new Test2HandlerInterceptor();
        interceptorRegistry.addInterceptor(interceptor2).addIncludePatterns("/in_test2", "/in_test3");

        RequestMappingHandlerMapping mappingHandlerMapping = new RequestMappingHandlerMapping();
        mappingHandlerMapping.setInterceptors(interceptorRegistry.getMappedInterceptors());
        return mappingHandlerMapping;
    }
}
