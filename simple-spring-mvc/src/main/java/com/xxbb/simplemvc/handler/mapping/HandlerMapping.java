package com.xxbb.simplemvc.handler.mapping;

import com.xxbb.simplemvc.handler.HandlerExecutionChain;

import javax.servlet.http.HttpServletRequest;

public interface HandlerMapping {
    HandlerExecutionChain getHandler(HttpServletRequest request) throws Exception;
}
