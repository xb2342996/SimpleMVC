package com.xxbb.simplemvc.handler.returnvalue;

import com.xxbb.simplemvc.handler.ModelAndViewContainer;
import org.springframework.core.MethodParameter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ViewMethodReturnValueHandler implements HandlerMethodReturnValueHandler{
    @Override
    public boolean supportsReturnType(MethodParameter returnType) {
        Class<?> paramType = returnType.getParameterType();
        return View.class.isAssignableFrom(paramType);
    }

    @Override
    public void handleReturnValue(Object returnValue, MethodParameter returnType, ModelAndViewContainer mavContainer, HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (returnValue instanceof View) {
            View view = (View)returnValue;
            mavContainer.setView(view);
        } else if (returnValue != null) {
            throw new UnsupportedOperationException("Unexcepted return type: " +
                    returnType.getParameterType().getName() + "in method: " + returnType.getMethod());
        }
    }
}
