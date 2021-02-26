package com.xxbb.simplemvc.handler.returnvalue;

import com.xxbb.simplemvc.handler.ModelAndViewContainer;
import org.springframework.core.MethodParameter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HandlerMethodReturnValueHandlerComposite implements HandlerMethodReturnValueHandler{

    private final List<HandlerMethodReturnValueHandler> returnValueHandlers = new ArrayList<>();

    @Override
    public boolean supportsReturnType(MethodParameter returnType) {
        return true;
    }

    @Override
    public void handleReturnValue(Object returnValue, MethodParameter returnType, ModelAndViewContainer mavContainer, HttpServletRequest request, HttpServletResponse response) throws Exception {
        for (HandlerMethodReturnValueHandler handler : returnValueHandlers) {
            if (handler.supportsReturnType(returnType)) {
                handler.handleReturnValue(returnValue, returnType, mavContainer, request, response);
                return;
            }
        }
        throw new IllegalArgumentException("Unexcepted return type: [" +
                returnType.getParameterType().getName() + "]. supportsParameter should be called first");
    }

    public void clear() {
        this.returnValueHandlers.clear();
    }

    public void addReturnValueHandler(HandlerMethodReturnValueHandler... handlers) {
        Collections.addAll(this.returnValueHandlers, handlers);
    }

    public void addReturnValueHandler(List<HandlerMethodReturnValueHandler> handlers) {
        this.returnValueHandlers.addAll(handlers);
    }
}
