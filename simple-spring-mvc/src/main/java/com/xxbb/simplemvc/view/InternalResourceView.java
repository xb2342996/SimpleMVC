package com.xxbb.simplemvc.view;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.Objects;

public class InternalResourceView extends AbstractView{

    private final String url;

    public InternalResourceView(String url) {
        this.url = url;
    }

    @Override
    public String getContentType() {
        return "text/html";
    }

    @Override
    protected void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        exposeModelAsRequestAttributes(model, request);
        RequestDispatcher rd = request.getRequestDispatcher(url);
        rd.forward(request, response);
    }

    private void exposeModelAsRequestAttributes(Map<String, Object> model, HttpServletRequest request) {
        model.forEach((key, value) -> {
            if (Objects.nonNull(value)) {
                request.setAttribute(key, value);
            } else {
                request.removeAttribute(key);
            }
        });
    }

    public String getUrl() {
        return url;
    }
}
