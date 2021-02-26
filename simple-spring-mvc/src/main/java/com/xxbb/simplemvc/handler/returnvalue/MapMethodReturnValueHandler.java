package com.xxbb.simplemvc.handler.returnvalue;

import com.xxbb.simplemvc.handler.ModelAndViewContainer;
import org.springframework.core.MethodParameter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

public class MapMethodReturnValueHandler implements HandlerMethodReturnValueHandler{
    @Override
    public boolean supportsReturnType(MethodParameter returnType) {
        return Map.class.isAssignableFrom(returnType.getParameterType());
    }

    @Override
    @SuppressWarnings("unchecked")
    public void handleReturnValue(Object returnValue, MethodParameter returnType, ModelAndViewContainer mavContainer, HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (returnValue instanceof Map) {
            mavContainer.getModel().addAllAttributes((Map)returnValue);
        } else if (returnValue != null) {
            throw new UnsupportedOperationException("Unexcepted return type: " +
                    returnType.getParameterType().getName() + "in method: " + returnType.getMethod());
        }
    }
}
