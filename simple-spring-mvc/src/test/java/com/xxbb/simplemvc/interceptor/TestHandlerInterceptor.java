package com.xxbb.simplemvc.interceptor;

import com.xxbb.simplemvc.ModelAndView;
import com.xxbb.simplemvc.handler.interceptor.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class TestHandlerInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandler(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        System.out.println("testHandlerInterceptor => preHandler");
        return true;
    }

    @Override
    public void postHandler(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        System.out.println("testHandlerInterceptor => postHandler");
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        System.out.println("TestHandlerInterceptor => afterCompletion");
    }
}
