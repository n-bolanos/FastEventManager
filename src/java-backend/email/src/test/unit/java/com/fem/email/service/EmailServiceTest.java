package com.fem.email.service;

import com.fem.email.dto.EmailRequest;
import com.fem.email.dto.EmailResponse;
import com.fem.email.dto.EmailType;
import jakarta.mail.MessagingException;
import jakarta.mail.Session;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.Test;
import org.springframework.mail.MailParseException;
import org.springframework.mail.javamail.JavaMailSender;

import java.util.Map;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link EmailService}, validating email delivery logic and
 * ensuring correct error handling for template failures, SMTP issues,
 * and unexpected exceptions.
 */
class EmailServiceTest {

    /**
     * Verifies that a valid request with a working template and functioning SMTP sender
     * results in a successful {@link EmailResponse}. Ensures:
     */
    @SuppressWarnings("null")
    @Test
    void sendEmail_Success() throws Exception {
        JavaMailSender sender = mock(JavaMailSender.class);

        Session session = Session.getInstance(new Properties());
        MimeMessage message = new MimeMessage(session);

        when(sender.createMimeMessage()).thenReturn(message);

        EmailService service = spy(new EmailService(sender));
        doReturn("<h1>OK</h1>").when(service).renderTemplate(any(), any());

        EmailRequest req = new EmailRequest();
        req.setTo("test@mail.com");
        req.setSubject("Hello");
        req.setType(EmailType.EVENT_CONFIRMATION);
        req.setParams(Map.of("x", "y"));

        EmailResponse res = service.sendEmail(req);

        assertTrue(res.isSuccess());
        assertNotNull(res.getMessageId());
        assertNull(res.getError());

        verify(sender).send(any(MimeMessage.class));
    }

    /**
     * Ensures that if template rendering throws {@link TemplateError},
     * the service does not attempt to send an email and returns an error response.
     */
    @Test
    void sendEmail_TemplateError_ReturnsErrorResponse() throws Exception {
        JavaMailSender sender = mock(JavaMailSender.class);
        EmailService service = spy(new EmailService(sender));

        doThrow(new TemplateError("Template failed"))
                .when(service).renderTemplate(any(), any());

        EmailRequest req = new EmailRequest();
        req.setTo("a");
        req.setSubject("b");
        req.setType(EmailType.EVENT_CONFIRMATION);
        req.setParams(Map.of());

        EmailResponse res = service.sendEmail(req);

        assertFalse(res.isSuccess());
        assertNull(res.getMessageId());
        assertTrue(res.getError().contains("Template processing failed"));
    }

    /**
     * Ensures that SMTP transport failures (e.g., {@link MessagingException})
     * are caught and returned as an "Email sending failed" response.
     */
    @SuppressWarnings("null")
    @Test
    void sendEmail_SmtpFailure_ReturnsErrorResponse() throws Exception {
        JavaMailSender sender = mock(JavaMailSender.class);

        MimeMessage message = mock(MimeMessage.class);
        when(sender.createMimeMessage()).thenReturn(message);

        doThrow(new MailParseException("SMTP down"))
                .when(sender).send(isA(MimeMessage.class));

        EmailService service = spy(new EmailService(sender));
        doReturn("<h1>OK</h1>").when(service).renderTemplate(any(), any());

        EmailRequest req = new EmailRequest();
        req.setTo("x");
        req.setSubject("y");
        req.setType(EmailType.PASSWORD_RESET);
        req.setParams(Map.of());

        EmailResponse res = service.sendEmail(req);

        assertFalse(res.isSuccess());
        assertNull(res.getMessageId());
        assertTrue(res.getError().contains("Email sending failed"));
    }

    /**
     * Ensures unexpected exceptions (e.g., IllegalStateException) occurring
     * before sending (such as during creation of the MimeMessage)
     * are caught and returned as an "Unexpected error" response.
     */
    @Test
    void sendEmail_UnexpectedException_ReturnsErrorResponse() throws Exception {
        JavaMailSender sender = mock(JavaMailSender.class);
        EmailService service = new EmailService(sender);

        when(sender.createMimeMessage()).thenThrow(new IllegalStateException("Boom"));

        EmailRequest req = new EmailRequest();
        req.setTo("a");
        req.setSubject("b");
        req.setType(EmailType.CAPACITY_REACHED);
        req.setParams(Map.of());

        EmailResponse res = service.sendEmail(req);

        assertFalse(res.isSuccess());
        assertNull(res.getMessageId());
        assertTrue(res.getError().contains("Unexpected error"));
    }
}