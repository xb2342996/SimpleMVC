package com.xxbb.simplemvc.interceptor;

import com.xxbb.simplemvc.ModelAndView;
import com.xxbb.simplemvc.handler.interceptor.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Test2HandlerInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandler(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        System.out.println("test2HandlerInterceptor => preHandler");
        return false;
    }

    @Override
    public void postHandler(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        System.out.println("test2HandlerInterceptor => postHandler");
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        System.out.println("Test2HandlerInterceptor => afterCompletion");
    }
}
