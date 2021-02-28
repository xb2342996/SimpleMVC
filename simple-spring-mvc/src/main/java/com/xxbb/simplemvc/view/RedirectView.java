package com.xxbb.simplemvc.view;

import org.springframework.util.Assert;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

public class RedirectView extends AbstractView{

    private final String url;

    public RedirectView(String url) {
        this.url = url;
    }

    @Override
    protected void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String targetUrl = createTargetUrl(model, request);
        response.sendRedirect(targetUrl);
    }

    private String createTargetUrl(Map<String, Object> model, HttpServletRequest request) {
        Assert.notNull(url, "Target Url cannot be null");

        StringBuilder sb = new StringBuilder();
        model.forEach((key, value) -> {
            sb.append(key).append("=").append(value).append("&");
        });

        if (sb.length() > 0) {
            sb.deleteCharAt(sb.length() - 1);
        }
        StringBuilder targetUrl = new StringBuilder();
        if (url.startsWith("/")) {
            targetUrl.append(getContextPath(request));
        }

        targetUrl.append(url);
        if (sb.length() > 0) {
            targetUrl.append("?").append(sb.toString());
        }
        return targetUrl.toString();
    }

    private String getContextPath(HttpServletRequest request) {
        String contextPath = request.getContextPath();
        while (contextPath.startsWith("//")) {
            contextPath = contextPath.substring(1);
        }
        return contextPath;
    }

    public String getUrl() {
        return url;
    }
}
