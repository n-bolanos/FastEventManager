package com.fem.email.service;

import com.fem.email.dto.EmailRequest;
import com.fem.email.dto.EmailResponse;
import com.fem.email.dto.EmailType;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.Test;
import org.springframework.mail.javamail.JavaMailSender;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class EmailServiceTest {

    @Test
    void sendEmail_Success() throws Exception {
        JavaMailSender sender = mock(JavaMailSender.class);

        jakarta.mail.Session session = jakarta.mail.Session.getInstance(System.getProperties());
        MimeMessage mimeMessage = new MimeMessage(session);

        when(sender.createMimeMessage()).thenReturn(mimeMessage);

        EmailService service = new EmailService(sender);

        EmailRequest req = new EmailRequest();
        req.setTo("secorreae@unal.edu.co");
        req.setSubject("Hello");
        req.setType(EmailType.EVENT_CONFIRMATION);
        req.setParams(Map.of("name", "Sergio", "eventName", "Tech"));

        EmailResponse res = service.sendEmail(req);

        assertTrue(res.isSuccess());
        assertNotNull(res.getMessageId());
        assertNull(res.getError());

        verify(sender, times(1)).send(mimeMessage);
    }


    @Test
    void sendEmail_TemplateError_ReturnsErrorResponse() throws Exception {
        JavaMailSender sender = mock(JavaMailSender.class);
        EmailService service = spy(new EmailService(sender));

        doThrow(new RuntimeException("Template failed"))
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

        jakarta.mail.Session session = jakarta.mail.Session.getInstance(System.getProperties());
        MimeMessage mime = new MimeMessage(session);

        when(sender.createMimeMessage()).thenReturn(mime);

        doThrow(new MessagingException("SMTP down"))
                .when(sender).send(any(MimeMessage[].class));

        EmailService service = new EmailService(sender);

        EmailRequest req = new EmailRequest();
        req.setTo("a");
        req.setSubject("b");
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
        EmailService service = spy(new EmailService(sender));

        doThrow(new IllegalStateException("Boom"))
                .when(sender).createMimeMessage();

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