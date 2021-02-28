package com.xxbb.simplemvc.exception;

public class TestException extends RuntimeException {
    private final String name;

    public TestException(String message, String name) {
        super(message);
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
