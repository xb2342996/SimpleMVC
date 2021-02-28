package com.xxbb.simplemvc.config;

import com.xxbb.simplemvc.handler.argument.HandlerMethodArgumentResolver;
import com.xxbb.simplemvc.handler.interceptor.InterceptorRegistry;
import com.xxbb.simplemvc.handler.returnvalue.HandlerMethodReturnValueHandler;
import com.xxbb.simplemvc.view.View;
import com.xxbb.simplemvc.view.resolver.ViewResolver;
import org.springframework.format.FormatterRegistry;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class WebMvcConfigurerComposite implements WebMvcConfigurer{
    private final List<WebMvcConfigurer> delegates = new ArrayList<>();

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        delegates.forEach(configurer -> configurer.addInterceptors(registry));
    }

    @Override
    public void addFormatters(FormatterRegistry registry) {
        delegates.forEach(configurer -> configurer.addFormatters(registry));
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        delegates.forEach(configurer -> configurer.addArgumentResolvers(argumentResolvers));
    }

    @Override
    public void addReturnValueHandlers(List<HandlerMethodReturnValueHandler> returnValueHandlers) {
        delegates.forEach(configurer -> configurer.addReturnValueHandlers(returnValueHandlers));
    }

    @Override
    public void addViewResolvers(List<ViewResolver> viewResolvers) {
        delegates.forEach(configurer -> configurer.addViewResolvers(viewResolvers));
    }

    @Override
    public void addDefaultViews(List<View> views) {
        delegates.forEach(configurer -> configurer.addDefaultViews(views));
    }

    public WebMvcConfigurerComposite addWebMvcConfigurers(WebMvcConfigurer... webMvcConfigurers) {
        Collections.addAll(this.delegates, webMvcConfigurers);
        return this;
    }

    public WebMvcConfigurerComposite addWebMvcConfigurers(List<WebMvcConfigurer> configurers) {
        delegates.addAll(configurers);
        return this;
    }
}
