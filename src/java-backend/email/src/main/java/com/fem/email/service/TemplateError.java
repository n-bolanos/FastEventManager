package com.fem.email.service;

public class TemplateError extends RuntimeException {
    public TemplateError(String message) {
        super(message);
    }

    public TemplateError(String message, Throwable cause) {
        super(message, cause);
    }
}
