package com.xxbb.simplemvc.handler.argument;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.xxbb.simplemvc.annotation.RequestBody;
import com.xxbb.simplemvc.exception.MissingServletRequestParameterException;
import com.xxbb.simplemvc.handler.ModelAndViewContainer;
import com.xxbb.simplemvc.http.HttpMethod;
import com.xxbb.simplemvc.http.HttpRequest;
import com.xxbb.simplemvc.http.InvalidMediaTypeException;
import com.xxbb.simplemvc.http.MediaType;
import com.xxbb.simplemvc.http.converter.HttpMessageNotReadableException;
import com.xxbb.simplemvc.http.server.ServletServerHttpRequest;
import com.xxbb.simplemvc.web.HttpMediaTypeNotSupportedException;
import org.springframework.core.GenericTypeResolver;
import org.springframework.core.MethodParameter;
import org.springframework.core.ResolvableType;
import org.springframework.core.convert.ConversionService;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Type;

public class ServletRequestMethodArgumentResolver implements HandlerMethodArgumentResolver {

    private static final Object NO_VALUE = new Object();

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        Class<?> parameterType = parameter.getParameterType();
        return ServletRequest.class.isAssignableFrom(parameterType);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, HttpServletRequest request, HttpServletResponse response, ModelAndViewContainer container, ConversionService conversionService) throws Exception {
        Object arg = readMessageWithConverters(request, parameter, parameter.getParameterType());
        if (arg == null && checkRequired(parameter)) {
            throw new MissingServletRequestParameterException(parameter.getParameterName(), parameter.getParameterType().getName());
        }
        return arg;
    }

    protected boolean checkRequired(MethodParameter parameter) {
        RequestBody requestBody = parameter.getParameterAnnotation(RequestBody.class);
        return (requestBody != null) && requestBody.required() && !parameter.isOptional();
    }

    protected <T> Object readMessageWithConverters(HttpServletRequest servletRequest, MethodParameter parameter, Class<T> paramType) throws HttpMediaTypeNotSupportedException, HttpMessageNotReadableException {
        Assert.state(servletRequest != null, "No HttpServletRequest");
        ServletServerHttpRequest inputMessage = new ServletServerHttpRequest(servletRequest);
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(inputMessage.getBody(), paramType);
        } catch (IOException ex) {
            throw new HttpMessageNotReadableException("I/O error while reading input message", ex, inputMessage);
        }
    }
}
