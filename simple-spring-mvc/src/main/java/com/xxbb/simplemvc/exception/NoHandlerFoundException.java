package com.xxbb.simplemvc.exception;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

public class NoHandlerFoundException extends ServletException {
    private final String httpMethod;
    private final String requestUrl;

    public NoHandlerFoundException(HttpServletRequest request) {
        this.httpMethod = request.getMethod();
        this.requestUrl = request.getRequestURL().toString();
    }

    public String getHttpMethod() {
        return httpMethod;
    }

    public String getRequestUrl() {
        return requestUrl;
    }
}
