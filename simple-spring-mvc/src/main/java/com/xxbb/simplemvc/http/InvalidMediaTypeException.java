package com.xxbb.simplemvc.http;

import org.springframework.util.InvalidMimeTypeException;

public class InvalidMediaTypeException extends IllegalArgumentException {
    private final String mediaType;

    public InvalidMediaTypeException(String message, String mediaType) {
        super("invalid media type \"" + mediaType + "\":" + message);
        this.mediaType = mediaType;
    }

    InvalidMediaTypeException(InvalidMimeTypeException ex) {
        super(ex.getMessage(), ex);
        this.mediaType = ex.getMimeType();
    }

    public String getMediaType() {
        return mediaType;
    }
}
