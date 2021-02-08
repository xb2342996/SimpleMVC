package com.xxbb.simplemvc.handler.mapping;

import com.xxbb.simplemvc.annotation.RequestMapping;
import com.xxbb.simplemvc.http.RequestMethod;

public class RequestMappingInfo {
    private String path;
    private RequestMethod httpMethod;

    public RequestMappingInfo(String prefix, RequestMapping requestMapping) {
        this.path = prefix + requestMapping.path();
        this.httpMethod = requestMapping.method();
    }

    public String getPath() {
        return path;
    }

    public RequestMethod getHttpMethod() {
        return httpMethod;
    }
}
