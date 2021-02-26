package com.xxbb.simplemvc;

import com.xxbb.simplemvc.handler.HandlerMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface HandlerAdapter {
    ModelAndView handle(HttpServletRequest request, HttpServletResponse response, HandlerMethod handler) throws Exception;
}
