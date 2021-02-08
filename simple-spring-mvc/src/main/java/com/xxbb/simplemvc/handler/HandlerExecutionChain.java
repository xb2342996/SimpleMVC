package com.xxbb.simplemvc.handler;

import com.xxbb.simplemvc.ModelAndView;
import com.xxbb.simplemvc.handler.interceptor.HandlerInterceptor;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

public class HandlerExecutionChain {

    private final HandlerMethod handler;
    private List<HandlerInterceptor> interceptors = new ArrayList<>();
    private int interceptorIndex = -1;

    public HandlerExecutionChain(HandlerMethod handler, List<HandlerInterceptor> interceptors) {
        this.handler = handler;
        if (!CollectionUtils.isEmpty(interceptors)) {
            this.interceptors = interceptors;
        }
    }

    public boolean applyPreHandle(HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (CollectionUtils.isEmpty(interceptors)) {
            return true;
        }

        for (int i = 0; i < interceptors.size(); i++) {
            HandlerInterceptor interceptor = interceptors.get(i);
            if (!interceptor.preHandler(request, response, this.handler)) {
                triggerAfterCompletion(request, response, null);
                return false;
            }
            this.interceptorIndex = i;
        }
        return true;
    }

    public void applyPostHandle(HttpServletRequest request, HttpServletResponse response, ModelAndView modelAndView) throws Exception {
        if (CollectionUtils.isEmpty(interceptors)) {
            return;
        }
        for (int i = interceptorIndex; i >=0 ; i--) {
            HandlerInterceptor interceptor = interceptors.get(i);
            interceptor.postHandler(request, response, handler, modelAndView);
        }
    }

    public void triggerAfterCompletion(HttpServletRequest request, HttpServletResponse response, Exception ex) throws Exception {
        if (CollectionUtils.isEmpty(interceptors)) {
            return;
        }
        for (int i = interceptorIndex; i >= 0; i--) {
            HandlerInterceptor interceptor = interceptors.get(i);
            interceptor.afterCompletion(request, response, this.handler, ex);
        }
    }

    public HandlerMethod getHandler() {
        return handler;
    }

    public List<HandlerInterceptor> getInterceptors() {
        return interceptors;
    }
}
