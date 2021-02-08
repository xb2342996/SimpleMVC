package com.xxbb.simplemvc.handler.mapping;

import com.xxbb.simplemvc.handler.HandlerMethod;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MappingRegistry {
    private final Map<String, RequestMappingInfo> pathMappingInfo = new ConcurrentHashMap<>();
    private final Map<String, HandlerMethod> pathHanderMethod = new ConcurrentHashMap<>();

    public void register(RequestMappingInfo mapping, Object handler, Method method) {
        pathMappingInfo.put(mapping.getPath(), mapping);
        HandlerMethod handlerMethod = new HandlerMethod(handler, method);
        pathHanderMethod.put(mapping.getPath(), handlerMethod);
    }


    public Map<String, RequestMappingInfo> getPathMappingInfo() {
        return pathMappingInfo;
    }

    public Map<String, HandlerMethod> getPathHanderMethod() {
        return pathHanderMethod;
    }

    public RequestMappingInfo getMappingByPath(String path) {
        return this.pathMappingInfo.get(path);
    }

    public HandlerMethod getHandlerMethodByPath(String path) {
        return this.pathHanderMethod.get(path);
    }
}
