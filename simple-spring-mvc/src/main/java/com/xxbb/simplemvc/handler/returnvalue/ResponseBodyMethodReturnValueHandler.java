package com.xxbb.simplemvc.handler.returnvalue;

import com.google.gson.Gson;
import com.xxbb.simplemvc.annotation.ResponseBody;
import com.xxbb.simplemvc.handler.ModelAndViewContainer;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.AnnotatedElementUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class ResponseBodyMethodReturnValueHandler implements HandlerMethodReturnValueHandler {

    @Override
    public boolean supportsReturnType(MethodParameter returnType) {
        return (AnnotatedElementUtils.hasAnnotation(returnType.getContainingClass(), ResponseBody.class))
                || returnType.hasMethodAnnotation(ResponseBody.class);
    }

    @Override
    public void handleReturnValue(Object returnValue, MethodParameter returnType, ModelAndViewContainer mavContainer, HttpServletRequest request, HttpServletResponse response) throws Exception {
        mavContainer.setRequestHandled(true);
        outPutMessage(response, new Gson().toJson(returnValue));
    }
    private void outPutMessage(HttpServletResponse response, String message) throws IOException {
        try (PrintWriter out = response.getWriter()) {
            out.write(message);
        }
    }
}
