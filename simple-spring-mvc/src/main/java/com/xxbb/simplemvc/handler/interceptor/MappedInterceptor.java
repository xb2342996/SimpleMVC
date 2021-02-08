package com.xxbb.simplemvc.handler.interceptor;

import com.xxbb.simplemvc.ModelAndView;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MappedInterceptor implements HandlerInterceptor {

    private final List<String> includePatterns = new ArrayList<>();
    private final List<String> excludePatterns = new ArrayList<>();

    private HandlerInterceptor interceptor;

    public MappedInterceptor(HandlerInterceptor interceptor) {
        this.interceptor = interceptor;
    }

    public MappedInterceptor addIncludePatterns(String... patterns) {
        this.includePatterns.addAll(Arrays.asList(patterns));
        return this;
    }

    public MappedInterceptor addExcludePatterns(String... patterns) {
        this.excludePatterns.addAll(Arrays.asList(patterns));
        return this;
    }

    public boolean matches(String lookupPath) {
        if (!CollectionUtils.isEmpty(this.excludePatterns)) {
            if (excludePatterns.contains(lookupPath)) {
                return false;
            }
        }
        if (ObjectUtils.isEmpty(this.includePatterns)) {
            return true;
        }
        return includePatterns.contains(lookupPath);
    }

    @Override
    public boolean preHandler(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        return this.interceptor.preHandler(request, response, handler);
    }

    @Override
    public void postHandler(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        this.interceptor.postHandler(request, response, handler, modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        this.interceptor.afterCompletion(request, response, handler, ex);
    }

    public HandlerInterceptor getInterceptor() {
        return interceptor;
    }

    public void setInterceptor(HandlerInterceptor interceptor) {
        this.interceptor = interceptor;
    }
}
