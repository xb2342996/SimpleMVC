package com.xxbb.simplemvc.web;

import com.xxbb.simplemvc.http.MediaType;
import org.springframework.lang.Nullable;

import java.util.List;

public class HttpMediaTypeNotSupportedException extends HttpMediaTypeException {
    @Nullable
    private final MediaType contentType;

    public HttpMediaTypeNotSupportedException(String message) {
        super(message);
        this.contentType = null;
    }

    public HttpMediaTypeNotSupportedException(@Nullable MediaType contentType, List<MediaType> supportedMediaTypes) {
        this(contentType, supportedMediaTypes, "Content type '" + (contentType != null ? contentType : "") + "' not supported");
    }

    public HttpMediaTypeNotSupportedException(@Nullable MediaType contentType, List<MediaType> supportedMediaTypes, String message) {
        super(message, supportedMediaTypes);
        this.contentType = contentType;
    }

    @Nullable
    public MediaType getContentType() {
        return contentType;
    }
}
