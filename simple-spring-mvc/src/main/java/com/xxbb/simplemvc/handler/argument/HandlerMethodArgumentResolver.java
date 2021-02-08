package com.xxbb.simplemvc.handler.argument;

import com.xxbb.simplemvc.handler.ModelAndViewContainer;
import org.springframework.core.MethodParameter;
import org.springframework.core.convert.ConversionService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface HandlerMethodArgumentResolver {

    boolean supportsParameter(MethodParameter parameter);

    Object resolveArgument(MethodParameter parameter, HttpServletRequest request, HttpServletResponse response,
                           ModelAndViewContainer container, ConversionService conversionService) throws Exception;

}
