package com.xxbb.simplemvc.handler.argument;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xxbb.simplemvc.annotation.RequestBody;
import com.xxbb.simplemvc.exception.MissingServletRequestParameterException;
import com.xxbb.simplemvc.handler.ModelAndViewContainer;
import com.xxbb.simplemvc.http.HttpInputMessage;
import com.xxbb.simplemvc.http.server.ServletServerHttpRequest;
import org.springframework.core.MethodParameter;
import org.springframework.core.convert.ConversionService;
import org.springframework.util.Assert;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

public class RequestBodyMethodArgumentResolver implements HandlerMethodArgumentResolver{
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(RequestBody.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, HttpServletRequest request, HttpServletResponse response, ModelAndViewContainer container, ConversionService conversionService) throws Exception {
        Assert.state(request != null, "No HttpservletRequest");
        HttpInputMessage message = new ServletServerHttpRequest(request);
        ObjectMapper mapper = new ObjectMapper();
        Object body = mapper.readValue(message.getBody(), parameter.getParameterType());
        if (!body.equals(new Object())) {
            return body;
        }
        RequestBody requestBody = parameter.getParameterAnnotation(RequestBody.class);
        if (Objects.isNull(requestBody)) {
            return null;
        }
        if (requestBody.required()) {
            throw new MissingServletRequestParameterException(parameter.getParameterName(), parameter.getParameterType().getName());
        }
        return null;
    }
}
