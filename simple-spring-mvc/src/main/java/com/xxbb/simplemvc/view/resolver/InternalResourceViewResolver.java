package com.xxbb.simplemvc.view.resolver;

import com.xxbb.simplemvc.view.InternalResourceView;
import com.xxbb.simplemvc.view.View;

public class InternalResourceViewResolver extends UrlBasedViewResolver{
    @Override
    protected View buildView(String viewName) {
        String url = getPrefix() + viewName + getSuffix();
        return new InternalResourceView(url);
    }
}
