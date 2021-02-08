package com.xxbb.simplemvc.handler;

import com.xxbb.simplemvc.http.HttpStatus;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;

import java.util.Objects;

public class ModelAndViewContainer {
    private Object view;
    private Model model;
    private HttpStatus status;
    private boolean requestHandled = false;

    public Object getView() {
        return view;
    }

    public String getViewName() {
        return (this.view instanceof String ? (String) this.view : null);
    }

    public void setViewName(String viewName) {
        this.view = viewName;
    }

    public void setView(Object view) {
        this.view = view;
    }

    public Model getModel() {
        if (Objects.isNull(this.model)) {
            this.model = new ExtendedModelMap();
        }
        return model;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }

    public boolean isRequestHandled() {
        return requestHandled;
    }

    public void setRequestHandled(boolean requestHandled) {
        this.requestHandled = requestHandled;
    }
}
