package com.xxbb.simplemvc;

import com.xxbb.simplemvc.http.HttpStatus;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;

public class ModelAndView {
    private Object view;
    private Model model = new ExtendedModelMap();
    private HttpStatus status;

    public Object getView() {
        return view;
    }

    public void setView(Object view) {
        this.view = view;
    }

    public Model getModel() {
        return model;
    }

    public void setModel(Model model) {
        this.model = model;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }

    public void setViewName(String viewName) {
        this.view = viewName;
    }

    public String getViewName() {
        return (this.view instanceof String ? (String) this.view : null);
    }
}
