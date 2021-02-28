package com.xxbb.simplemvc.view;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

public abstract class AbstractView implements View {
    @Override
    public void render(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        propareResponse(request, response);
        renderMergedOutputModel(model, request, response);
    }

    protected void propareResponse(HttpServletRequest request, HttpServletResponse response) {

    }

    protected abstract void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response) throws Exception;
}
