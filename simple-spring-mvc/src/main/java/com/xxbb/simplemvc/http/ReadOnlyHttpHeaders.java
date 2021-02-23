package com.xxbb.simplemvc.http;

import org.springframework.lang.Nullable;

import java.util.List;

public class ReadOnlyHttpHeaders extends HttpHeaders {
    private static final long serialVersionUID = 1L;

    @Nullable
    private MediaType cachedContentType;

    @Nullable
    private List<MediaType> cachedAccept;

    public ReadOnlyHttpHeaders(HttpHeaders headers) {
        super(headers.headers);
    }
}
