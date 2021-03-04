package com.xxbb.simplemvc.configure;

import com.xxbb.simplemvc.DispatcherServlet;
import com.xxbb.simplemvc.configure.servlet.SimpleMvcDispatcherServletRegistrationBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnClass(DispatcherServlet.class)
@EnableConfigurationProperties(WebMvcProperties.class)
public class SimpleMvcDispatcherServletAutoConfiguration {
    public static final String DEFAULT_DISPATCHER_SERVLET_BEAN_NAME = "simpleMvcDispatcherServlet";

    @Bean
    @ConditionalOnMissingBean(value = DispatcherServlet.class)
    public DispatcherServlet dispatcherServlet() {
        return new DispatcherServlet();
    }

    @Bean
    @ConditionalOnClass(value = DispatcherServlet.class)
    public SimpleMvcDispatcherServletRegistrationBean dispatcherServletRegistrationBean(
            DispatcherServlet dispatcherServlet, WebMvcProperties webMvcProperties) {
        SimpleMvcDispatcherServletRegistrationBean registration = new SimpleMvcDispatcherServletRegistrationBean(
                dispatcherServlet, webMvcProperties.getServlet().getPath());
        registration.setName(DEFAULT_DISPATCHER_SERVLET_BEAN_NAME);
        registration.setLoadOnStartup(webMvcProperties.getServlet().getLoadOnStartup());
        return registration;
    }
}
