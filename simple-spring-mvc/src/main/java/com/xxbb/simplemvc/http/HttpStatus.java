package com.xxbb.simplemvc.http;

public enum HttpStatus {
    OK(200, "OK");

    private final int value;

    private final String reasonPharse;

    HttpStatus(int value, String reasonPharse) {
        this.value = value;
        this.reasonPharse = reasonPharse;
    }

    public int getValue() {
        return value;
    }

    public String getReasonPharse() {
        return reasonPharse;
    }
}
