package com.xxbb.simplemvc.view.resolver;


import com.xxbb.simplemvc.util.RequestContextHolder;
import com.xxbb.simplemvc.view.RedirectView;
import com.xxbb.simplemvc.view.View;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

public class ContentNegotiatingViewResolver implements ViewResolver, InitializingBean {

    private List<ViewResolver> viewResolvers;
    private List<View> defaultViews;

    @Override
    public View resolveViewName(String viewName) throws Exception {
        List<View> candidateViews = getCandidateViews(viewName);
        View bestView = getBestView(candidateViews);
        if (Objects.nonNull(bestView)) {
            return bestView;
        }
        return null;
    }

    @Override
    public void afterPropertiesSet() throws Exception {

    }

    private View getBestView(List<View> candidateView) {
        Optional<View> viewOptional = candidateView.stream().filter(view -> view instanceof RedirectView).findAny();
        if (viewOptional.isPresent()) {
            return viewOptional.get();
        }

        HttpServletRequest request = RequestContextHolder.getRequest();
        Enumeration<String> acceptHeaders = request.getHeaders("Accept");
        while (acceptHeaders.hasMoreElements()) {
            for (View view : candidateView) {
                if (acceptHeaders.nextElement().equals(view.getContentType())) {
                    return view;
                }
            }
        }
        return null;
    }

    private List<View> getCandidateViews(String viewName) throws Exception {
        List<View> candidateViews = new ArrayList<>();
        for (ViewResolver resolver : viewResolvers) {
            View view = resolver.resolveViewName(viewName);
            if (Objects.nonNull(view)) {
                candidateViews.add(view);
            }
        }
        if (!CollectionUtils.isEmpty(defaultViews)) {
            candidateViews.addAll(defaultViews);
        }
        return candidateViews;
    }

    public List<ViewResolver> getViewResolvers() {
        return viewResolvers;
    }

    public void setViewResolvers(List<ViewResolver> viewResolvers) {
        this.viewResolvers = viewResolvers;
    }

    public List<View> getDefaultViews() {
        return defaultViews;
    }

    public void setDefaultViews(List<View> defaultViews) {
        this.defaultViews = defaultViews;
    }
}
