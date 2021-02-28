package com.xxbb.simplemvc.handler.exception;

import com.xxbb.simplemvc.ModelAndView;
import com.xxbb.simplemvc.handler.InvocableHandlerMethod;
import com.xxbb.simplemvc.handler.ModelAndViewContainer;
import com.xxbb.simplemvc.handler.argument.*;
import com.xxbb.simplemvc.handler.returnvalue.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.convert.ConversionService;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.*;

public class ExceptionHandlerExceptionResolver implements HandlerExceptionResolver, ApplicationContextAware, InitializingBean {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private ApplicationContext applicationContext;
    private final Map<ControllerAdviceBean, ExceptionHandlerMethodResolver> exceptionHandlerAdviceCache = new LinkedHashMap<>();
    private List<HandlerMethodArgumentResolver> customerArgumentResolvers;
    private HandlerMethodArgumentResolverComposite argumentResolvers;
    private List<HandlerMethodReturnValueHandler> customerReturnValueHandlers;
    private HandlerMethodReturnValueHandlerComposite returnValueHandlers;
    private ConversionService conversionService;

    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(conversionService, "conversionService cannot be null");
        initExceptionHandlerAdviceCache();
        if (argumentResolvers == null) {
            List<HandlerMethodArgumentResolver> resolvers = getDefaultArgumentResolvers();
            argumentResolvers = new HandlerMethodArgumentResolverComposite();
            argumentResolvers.addResolver(resolvers);
        }

        if (returnValueHandlers == null) {
            List<HandlerMethodReturnValueHandler> handlers = getDefaultReturnValueHandlers();
            returnValueHandlers = new HandlerMethodReturnValueHandlerComposite();
            returnValueHandlers.addReturnValueHandler(handlers);
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Exception ex) {
        InvocableHandlerMethod exceptionHandlerMethod = getExceptionHandlerMethod(ex);
        if (exceptionHandlerMethod == null) {
            return null;
        }

        ModelAndViewContainer mavContainer = new ModelAndViewContainer();
        try {
            Throwable cause = ex.getCause();
            if (Objects.nonNull(cause)) {
                exceptionHandlerMethod.invokeAndHandle(request, response, mavContainer, cause);
            } else {
                exceptionHandlerMethod.invokeAndHandle(request, response, mavContainer, ex);
            }
        } catch (Exception e) {
            logger.error("exceptionHandlerMethod.invokeAndHandle fail", e);
            return null;
        }

        if (mavContainer.isRequestHandled()) {
            return null;
        }

        ModelAndView mav = new ModelAndView();
        mav.setStatus(mavContainer.getStatus());
        mav.setModel(mavContainer.getModel());
        mav.setView(mavContainer.getView());
        return mav;
    }

    private InvocableHandlerMethod getExceptionHandlerMethod(Exception exception) {
        for (Map.Entry<ControllerAdviceBean, ExceptionHandlerMethodResolver> entry : exceptionHandlerAdviceCache.entrySet()) {
            ControllerAdviceBean advice = entry.getKey();
            ExceptionHandlerMethodResolver resolver = entry.getValue();
            Method method = resolver.resolveMethod(exception);
            if (method != null) {
                return new InvocableHandlerMethod(advice.getBean(), method, argumentResolvers, returnValueHandlers, conversionService);
            }
        }
        return null;
    }

    private void initExceptionHandlerAdviceCache() {
        List<ControllerAdviceBean> adviceBeans = ControllerAdviceBean.findAnnotatedBeans(applicationContext);
        for (ControllerAdviceBean adviceBean : adviceBeans) {
            Class<?> beanType = adviceBean.getBeanType();
            if (beanType == null) {
                throw new IllegalStateException("Unresolvable type for ControllerAdviceBean: " + adviceBean);
            }
            ExceptionHandlerMethodResolver resolver = new ExceptionHandlerMethodResolver(beanType);
            if (resolver.hasExceptionMappings()) {
                exceptionHandlerAdviceCache.put(adviceBean, resolver);
            }
        }
    }

    private List<HandlerMethodArgumentResolver> getDefaultArgumentResolvers() {
        List<HandlerMethodArgumentResolver> resolvers = new ArrayList<>();

        resolvers.add(new ModelMethodArgumentResolver());
        resolvers.add(new RequestParamMethodArgumentResolver());
        resolvers.add(new RequestBodyMethodArgumentResolver());
        resolvers.add(new ServletRequestMethodArgumentResolver());
        resolvers.add(new ServletResponseMethodArgumentResolver());

        if (!CollectionUtils.isEmpty(getCustomerArgumentResolvers())) {
            resolvers.addAll(getCustomerArgumentResolvers());
        }
        return resolvers;
    }

    private List<HandlerMethodReturnValueHandler> getDefaultReturnValueHandlers() {
        List<HandlerMethodReturnValueHandler> handlers = new ArrayList<>();

        handlers.add(new MapMethodReturnValueHandler());
        handlers.add(new ModelMethodReturnValueHandler());
        handlers.add(new ViewNameMethodReturnValueHandler());
        handlers.add(new ViewMethodReturnValueHandler());
        handlers.add(new ResponseBodyMethodReturnValueHandler());

        if (!CollectionUtils.isEmpty(getCustomerReturnValueHandlers())) {
            handlers.addAll(getCustomerReturnValueHandlers());
        }
        return handlers;
    }

    public List<HandlerMethodArgumentResolver> getCustomerArgumentResolvers() {
        return customerArgumentResolvers;
    }

    public void setCustomerArgumentResolvers(List<HandlerMethodArgumentResolver> customerArgumentResolvers) {
        this.customerArgumentResolvers = customerArgumentResolvers;
    }

    public List<HandlerMethodReturnValueHandler> getCustomerReturnValueHandlers() {
        return customerReturnValueHandlers;
    }

    public void setCustomerReturnValueHandlers(List<HandlerMethodReturnValueHandler> customerReturnValueHandlers) {
        this.customerReturnValueHandlers = customerReturnValueHandlers;
    }

    public ConversionService getConversionService() {
        return conversionService;
    }

    public void setConversionService(ConversionService conversionService) {
        this.conversionService = conversionService;
    }
}
