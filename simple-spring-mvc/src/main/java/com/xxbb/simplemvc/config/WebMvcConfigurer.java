package com.xxbb.simplemvc.config;

import com.xxbb.simplemvc.handler.argument.HandlerMethodArgumentResolver;
import com.xxbb.simplemvc.handler.interceptor.InterceptorRegistry;
import com.xxbb.simplemvc.handler.returnvalue.HandlerMethodReturnValueHandler;
import com.xxbb.simplemvc.view.View;
import com.xxbb.simplemvc.view.resolver.ViewResolver;
import org.springframework.format.FormatterRegistry;

import java.util.List;

public interface WebMvcConfigurer {
    default void addInterceptors(InterceptorRegistry registry) {

    }

    default void addFormatters(FormatterRegistry registry) {

    }

    default void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {

    }

    default void addReturnValueHandlers(List<HandlerMethodReturnValueHandler> returnValueHandlers) {

    }

    default void addViewResolvers(List<ViewResolver> viewResolvers) {

    }

    default void addDefaultViews(List<View> views) {

    }
}
