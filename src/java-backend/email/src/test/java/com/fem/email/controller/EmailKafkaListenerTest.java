package com.fem.email.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fem.email.dto.EmailRequest;
import com.fem.email.dto.EmailType;
import com.fem.email.service.EmailService;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class EmailKafkaListenerTest {

    private final ObjectMapper mapper = new ObjectMapper();

    @Test
    void consume_ValidJson_CallsService() throws Exception {
        EmailService service = mock(EmailService.class);
        EmailKafkaListener listener = new EmailKafkaListener(service);

        EmailRequest req = new EmailRequest();
        req.setTo("secorreae@unal.edu.co");
        req.setSubject("Test");
        req.setType(EmailType.WAITLIST_NOTIFICATION);
        req.setParams(Map.of("name", "Sergio"));

        String json = mapper.writeValueAsString(req);

        listener.consume(json);

        verify(service, times(1)).sendEmail(any(EmailRequest.class));
    }

    @Test
    void consume_InvalidJson_ThrowsExceptionNotCrash() {
        EmailService service = mock(EmailService.class);
        EmailKafkaListener listener = new EmailKafkaListener(service);

        String invalidJson = "{not valid json";

        // No debe lanzar excepción
        assertDoesNotThrow(() -> listener.consume(invalidJson));

        verify(service, never()).sendEmail(any());
    }

    @Test
    void consume_ServiceThrowsException_ListenerContinues() throws Exception {
        EmailService service = mock(EmailService.class);
        EmailKafkaListener listener = new EmailKafkaListener(service);

        doThrow(new RuntimeException("Email failed"))
                .when(service).sendEmail(any());

        EmailRequest req = new EmailRequest();
        req.setTo("secorreae@unal.edu.co");
        req.setSubject("Hello");
        req.setType(EmailType.EVENT_CONFIRMATION);
        req.setParams(Map.of());

        String json = mapper.writeValueAsString(req);

        listener.consume(json);

        verify(service, times(1)).sendEmail(any());
    }
}