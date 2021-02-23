package com.xxbb.simplemvc.web;

import com.xxbb.simplemvc.http.MediaType;

import javax.servlet.ServletException;
import java.util.Collections;
import java.util.List;

public abstract class HttpMediaTypeException extends ServletException {
    private final List<MediaType> supportedMediaTypes;

    public HttpMediaTypeException(String message) {
        super(message);
        this.supportedMediaTypes = Collections.emptyList();
    }

    public HttpMediaTypeException(String message, List<MediaType> supportedMediaTypes) {
        super(message);
        this.supportedMediaTypes = supportedMediaTypes;
    }

    public List<MediaType> getSupportedMediaTypes() {
        return supportedMediaTypes;
    }
}
