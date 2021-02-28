package com.xxbb.simplemvc.config;

import com.xxbb.simplemvc.handler.argument.HandlerMethodArgumentResolver;
import com.xxbb.simplemvc.handler.interceptor.InterceptorRegistry;
import com.xxbb.simplemvc.handler.returnvalue.HandlerMethodReturnValueHandler;
import com.xxbb.simplemvc.view.View;
import com.xxbb.simplemvc.view.resolver.ViewResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Configuration
public class DelegatingWebMvcConfiguration extends WebMvcConfigurationSupport{

    private WebMvcConfigurerComposite configurers = new WebMvcConfigurerComposite();

    @Autowired(required = false)
    public void setConfigurers(List<WebMvcConfigurer> configurers) {
        if (!CollectionUtils.isEmpty(configurers)) {
            this.configurers.addWebMvcConfigurers(configurers);
        }
    }

    @Override
    protected void addInterceptors(InterceptorRegistry registry) {
        configurers.addInterceptors(registry);
    }

    @Override
    protected void addFormatters(FormatterRegistry registry) {
        configurers.addFormatters(registry);
    }

    @Override
    protected void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        configurers.addArgumentResolvers(argumentResolvers);
    }

    @Override
    protected void addReturnValueHandlers(List<HandlerMethodReturnValueHandler> returnValueHandlers) {
        configurers.addReturnValueHandlers(returnValueHandlers);
    }

    @Override
    protected void addViewResolvers(List<ViewResolver> viewResolvers) {
        configurers.addViewResolvers(viewResolvers);
    }

    @Override
    protected void addDefaultViews(List<View> views) {
        configurers.addDefaultViews(views);
    }
}
