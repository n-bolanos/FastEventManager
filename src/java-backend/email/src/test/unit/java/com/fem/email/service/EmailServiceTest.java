package com.fem.email.service;

import com.fem.email.dto.EmailRequest;
import com.fem.email.dto.EmailResponse;
import com.fem.email.dto.EmailType;
import jakarta.mail.MessagingException;
import jakarta.mail.Session;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.Test;
import org.springframework.mail.javamail.JavaMailSender;

import java.util.Map;
import java.util.Objects;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.*;

class EmailServiceTest {

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

        verify(sender).send(Objects.requireNonNull(any(MimeMessage.class)));
    }

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

    @Test
    void sendEmail_SmtpFailure_ReturnsErrorResponse() throws Exception {
        JavaMailSender sender = mock(JavaMailSender.class);

        MimeMessage message = mock(MimeMessage.class);
        when(sender.createMimeMessage()).thenReturn(message);

        doThrow(new MessagingException("SMTP down"))
        .when(sender).send(Objects.requireNonNull(isA(MimeMessage.class)));

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