package com.xxbb.simplemvc.handler;

import com.xxbb.simplemvc.handler.argument.HandlerMethodArgumentResolverComposite;
import com.xxbb.simplemvc.handler.returnvalue.HandlerMethodReturnValueHandlerComposite;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.MethodParameter;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.core.convert.ConversionService;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
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

    public InvocableHandlerMethod(Object bean, Method method,
                                  HandlerMethodArgumentResolverComposite argumentResolver,
                                  HandlerMethodReturnValueHandlerComposite returnValueHandler,
                                  ConversionService conversionService) {
        super(bean, method);
        this.argumentResolver = argumentResolver;
        this.returnValueHandler = returnValueHandler;
        this.conversionService = conversionService;
    }

    public void invokeAndHandle(HttpServletRequest request, HttpServletResponse response, ModelAndViewContainer mavContainer, Object... providedArgs) throws Exception {
        List<Object> args = this.getMethodArgumentValues(request, response, mavContainer, providedArgs);
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
                                                   ModelAndViewContainer mavContainer,
                                                   Object... providedArgs) throws Exception {
        Assert.notNull(argumentResolver, "HandlerMethodArgumentResolver cannot be empty");

        List<MethodParameter> parameters = this.getParameters();
        List<Object> args = new ArrayList<>(parameters.size());
        for (MethodParameter methodParameter : parameters) {
            methodParameter.initParameterNameDiscovery(parameterNameDiscoverer);
            Object arg = findProvidedArgument(methodParameter, providedArgs);
            if (Objects.nonNull(arg)) {
                args.add(arg);
                continue;
            }
            args.add(argumentResolver.resolveArgument(methodParameter, request, response, mavContainer, conversionService));
        }
        return args;
    }

    protected static Object findProvidedArgument(MethodParameter parameter, Object... providedArgs) {
        if (!ObjectUtils.isEmpty(providedArgs)) {
            for (Object providedArg : providedArgs) {
                if (parameter.getParameterType().isInstance(providedArg)) {
                    return providedArg;
                }
            }
        }
        return null;
    }

    public void setParameterNameDiscoverer(ParameterNameDiscoverer parameterNameDiscoverer) {
        this.parameterNameDiscoverer = parameterNameDiscoverer;
    }
}
