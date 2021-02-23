package com.xxbb.simplemvc.http.converter;

import com.xxbb.simplemvc.http.HttpInputMessage;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

public class HttpMessageNotReadableException extends HttpMessageConversionException{

    @Nullable
    private final HttpInputMessage httpInputMessage;

    public HttpMessageNotReadableException(String msg, HttpInputMessage httpInputMessage) {
        super(msg);
        this.httpInputMessage = httpInputMessage;
    }

    public HttpMessageNotReadableException(String msg, Throwable cause, @Nullable HttpInputMessage httpInputMessage) {
        super(msg, cause);
        this.httpInputMessage = httpInputMessage;
    }

    @Nullable
    public HttpInputMessage getHttpInputMessage() {
        Assert.state(this.httpInputMessage != null, "No HttpInputMessage available - use non-deprecated constructors");
        return httpInputMessage;
    }
}
