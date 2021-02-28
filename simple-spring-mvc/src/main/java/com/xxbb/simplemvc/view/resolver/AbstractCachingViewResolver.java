package com.xxbb.simplemvc.view.resolver;

import com.xxbb.simplemvc.view.View;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public abstract class AbstractCachingViewResolver implements ViewResolver{
    private final Object lock = new Object();
    private static final View UNRESOLVED_VIEW = ((model, request, response) -> {});
    private final Map<String, View> cacheViews = new HashMap<>();

    @Override
    public View resolveViewName(String viewName) throws Exception {
        View view = cacheViews.get(viewName);
        if (Objects.nonNull(view)) {
            return (view != UNRESOLVED_VIEW ? view : null);
        }

        synchronized (lock) {
            view = cacheViews.get(viewName);
            if (Objects.nonNull(view)) {
                return (view != UNRESOLVED_VIEW ? view : null);
            }

            view = createView(viewName);
            if (Objects.isNull(view)) {
                view = UNRESOLVED_VIEW;
            }
            cacheViews.put(viewName, view);
        }
        return (view != UNRESOLVED_VIEW ? view : null);
    }

    protected abstract View createView(String viewName);
}
