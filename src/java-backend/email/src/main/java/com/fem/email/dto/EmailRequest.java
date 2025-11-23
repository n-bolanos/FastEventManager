package com.fem.email.dto;

import lombok.Data;
import java.util.Map;

/**
 * Data Transfer Object representing an incoming request to send an email.
 * 
 * This object contains all information required by the {@link com.fem.email.service.EmailService}
 * to construct and deliver an email based on a predefined template.
 * 
 */
@Data
public class EmailRequest {

    /** Recipient email address. */
    private String to;

    /** Subject line of the email. */
    private String subject;

    /** Type of email to be sent. Determines which template is used. */
    private EmailType type;

    /**
     * Set of dynamic parameters injected into the HTML template.
     * Keys represent placeholder names, values represent the replacement content.
     */
    private Map<String, Object> params;
}
