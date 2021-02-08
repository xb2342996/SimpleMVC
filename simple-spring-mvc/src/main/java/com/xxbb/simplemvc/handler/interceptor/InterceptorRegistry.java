package com.xxbb.simplemvc.handler.interceptor;

import java.util.ArrayList;
import java.util.List;

public class InterceptorRegistry {
    private final List<MappedInterceptor> mappedInterceptors = new ArrayList<>();

    public MappedInterceptor addInterceptor(HandlerInterceptor interceptor) {
        MappedInterceptor mappedInterceptor = new MappedInterceptor(interceptor);
        mappedInterceptors.add(mappedInterceptor);
        return mappedInterceptor;
    }

    public List<MappedInterceptor> getMappedInterceptors() {
        return mappedInterceptors;
    }
}
