package com.xxbb.simplemvc.view.resolver;

import com.xxbb.simplemvc.view.View;

public interface ViewResolver {
    View resolveViewName(String viewName) throws Exception;
}
