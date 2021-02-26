package com.xxbb.simplemvc.handler.returnvalue;

import com.xxbb.simplemvc.handler.ModelAndViewContainer;
import org.springframework.core.MethodParameter;
import org.springframework.ui.Model;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ModelMethodReturnValueHandler implements HandlerMethodReturnValueHandler{
    @Override
    public boolean supportsReturnType(MethodParameter returnType) {
        return Model.class.isAssignableFrom(returnType.getParameterType());
    }

    @Override
    public void handleReturnValue(Object returnValue, MethodParameter returnType, ModelAndViewContainer mavContainer,
                                  HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (returnValue == null) {
        } else if (returnValue instanceof Model) {
            mavContainer.getModel().addAllAttributes(((Model) returnValue).asMap());
        } else {
            throw new UnsupportedOperationException("Unexcepted return type: " +
                    returnType.getParameterType().getName() + "in method: " + returnType.getMethod());
        }
    }
}
