package com.xxbb.simplemvc.handler;

import com.xxbb.simplemvc.HandlerAdapter;
import com.xxbb.simplemvc.ModelAndView;
import com.xxbb.simplemvc.handler.argument.*;
import com.xxbb.simplemvc.handler.returnvalue.*;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.convert.ConversionService;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class RequestMappingHandlerAdapter implements HandlerAdapter, InitializingBean {

    private List<HandlerMethodArgumentResolver> customerArgumentResolvers;
    private HandlerMethodArgumentResolverComposite argumentResolverComposite;

    private List<HandlerMethodReturnValueHandler> customerReturnValueHandlers;
    private HandlerMethodReturnValueHandlerComposite returnValueHandlerComposite;

    private ConversionService conversionService;

    @Override
    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response, HandlerMethod handler) throws Exception {
        InvocableHandlerMethod invocableHandlerMethod = createInvocableHandlerMethod(handler);
        ModelAndViewContainer mavContainer = new ModelAndViewContainer();
        invocableHandlerMethod.invokeAndHandle(request, response, mavContainer);
        return getModelAndView(mavContainer);
    }

    private ModelAndView getModelAndView(ModelAndViewContainer modelAndViewContainer) {
        if (modelAndViewContainer.isRequestHandled()) {
            return null;
        }
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setStatus(modelAndViewContainer.getStatus());
        modelAndView.setModel(modelAndViewContainer.getModel());
        modelAndView.setView(modelAndViewContainer.getView());
        return modelAndView;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(conversionService, "conversionService cannot be null");
        if (Objects.isNull(argumentResolverComposite)) {
            List<HandlerMethodArgumentResolver> resolvers = getDefaultArgumentResolvers();
            argumentResolverComposite = new HandlerMethodArgumentResolverComposite();
            argumentResolverComposite.addResolver(resolvers);
        }

        if (Objects.isNull(returnValueHandlerComposite)) {
            List<HandlerMethodReturnValueHandler> handlers = getDefaultReturnValueHandlers();
            returnValueHandlerComposite = new HandlerMethodReturnValueHandlerComposite();
            returnValueHandlerComposite.addReturnValueHandler(handlers);
        }
    }

    private InvocableHandlerMethod createInvocableHandlerMethod(HandlerMethod handlerMethod) {
        return new InvocableHandlerMethod(handlerMethod, argumentResolverComposite, returnValueHandlerComposite, conversionService);
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

    public HandlerMethodArgumentResolverComposite getArgumentResolverComposite() {
        return argumentResolverComposite;
    }

    public void setArgumentResolverComposite(HandlerMethodArgumentResolverComposite argumentResolverComposite) {
        this.argumentResolverComposite = argumentResolverComposite;
    }

    public List<HandlerMethodReturnValueHandler> getCustomerReturnValueHandlers() {
        return customerReturnValueHandlers;
    }

    public void setCustomerReturnValueHandlers(List<HandlerMethodReturnValueHandler> customerReturnValueHandlers) {
        this.customerReturnValueHandlers = customerReturnValueHandlers;
    }

    public HandlerMethodReturnValueHandlerComposite getReturnValueHandlerComposite() {
        return returnValueHandlerComposite;
    }

    public void setReturnValueHandlerComposite(HandlerMethodReturnValueHandlerComposite returnValueHandlerComposite) {
        this.returnValueHandlerComposite = returnValueHandlerComposite;
    }

    public ConversionService getConversionService() {
        return conversionService;
    }

    public void setConversionService(ConversionService conversionService) {
        this.conversionService = conversionService;
    }
}
