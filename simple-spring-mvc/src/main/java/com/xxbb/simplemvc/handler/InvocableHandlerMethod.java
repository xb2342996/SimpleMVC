package com.xxbb.simplemvc.handler;

import com.xxbb.simplemvc.handler.argument.HandlerMethodArgumentResolverComposite;
import com.xxbb.simplemvc.handler.returnvalue.HandlerMethodReturnValueHandlerComposite;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.MethodParameter;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.core.convert.ConversionService;
import org.springframework.util.Assert;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class InvocableHandlerMethod extends HandlerMethod{

    private ParameterNameDiscoverer parameterNameDiscoverer = new DefaultParameterNameDiscoverer();
    private HandlerMethodArgumentResolverComposite argumentResolver;
    private HandlerMethodReturnValueHandlerComposite returnValueHandler;
    private ConversionService conversionService;

    public InvocableHandlerMethod(HandlerMethod handlerMethod,
                                  HandlerMethodArgumentResolverComposite argumentResolver,
                                  HandlerMethodReturnValueHandlerComposite returnValueHandler,
                                  ConversionService conversionService) {
        super(handlerMethod);
        this.argumentResolver = argumentResolver;
        this.conversionService = conversionService;
        this.returnValueHandler = returnValueHandler;
    }

    public void invokeAndHandle(HttpServletRequest request, HttpServletResponse response, ModelAndViewContainer mavContainer) throws Exception {
        List<Object> args = this.getMethodArgumentValues(request, response, mavContainer);
        Object object = doInvoke(args);
        if (Objects.isNull(object)) {
            if (response.isCommitted()) {
                mavContainer.setRequestHandled(true);
                return;
            } else {
                throw new IllegalStateException("Controller handler return value is null");
            }
        }
        mavContainer.setRequestHandled(false);
        Assert.state(this.returnValueHandler != null, "No return value handler");
        MethodParameter returnType = new MethodParameter(this.getMethod(), -1);
        this.returnValueHandler.handleReturnValue(object, returnType, mavContainer, request, response);
    }

    public Object doInvoke(List<Object> args) throws Exception {
        return this.getMethod().invoke(this.getBean(), args.toArray());
    }

    protected List<Object> getMethodArgumentValues(HttpServletRequest request,
                                                   HttpServletResponse response,
                                                   ModelAndViewContainer mavContainer) throws Exception {
        Assert.notNull(argumentResolver, "HandlerMethodArgumentResolver cannot be empty");

        List<MethodParameter> parameters = this.getParameters();
        List<Object> args = new ArrayList<>(parameters.size());
        for (MethodParameter methodParameter : parameters) {
            methodParameter.initParameterNameDiscovery(parameterNameDiscoverer);
            args.add(argumentResolver.resolveArgument(methodParameter, request, response, mavContainer, conversionService));
        }
        return args;
    }

    public void setParameterNameDiscoverer(ParameterNameDiscoverer parameterNameDiscoverer) {
        this.parameterNameDiscoverer = parameterNameDiscoverer;
    }
}
