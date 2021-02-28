package com.xxbb.simplemvc.util;

import org.springframework.core.NamedInheritableThreadLocal;

import javax.servlet.http.HttpServletRequest;

public abstract class RequestContextHolder {
    private static final ThreadLocal<HttpServletRequest> inheritableRequestHodler =
            new NamedInheritableThreadLocal<>("Request context");

    public static void resetRequest() {
        inheritableRequestHodler.remove();
    }

    public static void setRequest(HttpServletRequest request) {
        inheritableRequestHodler.set(request);
    }

    public static HttpServletRequest getRequest() {
        return inheritableRequestHodler.get();
    }
}
