package com.fem.email.service;

/**
 * Custom unchecked exception used to indicate errors during template
 * loading or rendering within the {@link TemplateEngine}.
 *
 * This exception represents issues such as:
 * - Missing template files
 * - Missing required placeholders
 * - Failures while reading or processing template content
 */
public class TemplateError extends RuntimeException {

    /**
     * Creates a new TemplateError with a descriptive message.
     *
     * @param message explanation of the failure
     */
    public TemplateError(String message) {
        super(message);
    }

    /**
     * Creates a new TemplateError with a message and an underlying cause.
     *
     * @param message explanation of the failure
     * @param cause   original exception causing this error
     */
    public TemplateError(String message, Throwable cause) {
        super(message, cause);
    }
}