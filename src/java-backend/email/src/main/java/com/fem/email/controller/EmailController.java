package com.fem.email.controller;

import com.fem.email.dto.EmailRequest;
import com.fem.email.dto.EmailResponse;
import com.fem.email.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller responsible for handling email-related HTTP requests.
 * 
 * Exposes an endpoint for sending emails through the EmailService.
 * 
 */
@RestController
@RequestMapping("/email")
@RequiredArgsConstructor
public class EmailController {

    /** Service used to process and send emails. */
    private final EmailService emailService;

    /**
     * Sends an email using the information provided in the request body.
     *
     * @param request email content, recipient, type, and template parameters
     * @return a response indicating whether the email was successfully sent
     */
    @PostMapping("/send")
    public EmailResponse sendEmail(@RequestBody EmailRequest request) {
        return emailService.sendEmail(request);
    }
}