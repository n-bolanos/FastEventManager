package com.fem.email.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Represents the result of an email-sending operation.
 * 
 * This object is returned by the {@link com.fem.email.service.EmailService}
 * after attempting to send an email. It provides information about whether
 * the operation succeeded, the generated message ID (if any), and any
 * encountered error details.
 * 
 */
@Data
@AllArgsConstructor
public class EmailResponse {

    /** Indicates whether the email was successfully sent. */
    private boolean success;

    /**
     * Identifier assigned to the email message, typically provided by the
     * underlying mail sender.
     * 
     * This value is {@code null} if the email failed to send.
     * 
     */
    private String messageId;

    /**
     * Error description if the email could not be sent.
     * 
     * This value is {@code null} when the operation succeeds.
     * 
     */
    private String error;
}