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

/**
 * Unit tests for {@link EmailKafkaListener}, ensuring correct behavior
 * when consuming Kafka messages. Tests cover:
 *
 * - Proper parsing of valid JSON messages.
 * - Handling of invalid JSON without crashing.
 * - Ensuring the listener continues even when the service throws exceptions.
 */
class EmailKafkaListenerTest {

    /** Utility mapper used to serialize and deserialize JSON for test cases. */
    private final ObjectMapper mapper = new ObjectMapper();

    /**
     * Ensures that when a valid JSON message is consumed, the listener parses it
     * and invokes the {@link EmailService#sendEmail(EmailRequest)} method exactly once.
     */
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

    /**
     * Ensures that malformed JSON does not crash the listener.
     * The listener should catch the exception internally and NOT call the service.
     */
    @Test
    void consume_InvalidJson_ThrowsExceptionNotCrash() {
        EmailService service = mock(EmailService.class);
        EmailKafkaListener listener = new EmailKafkaListener(service);

        String invalidJson = "{not valid json";

        // Listener must not throw an exception.
        assertDoesNotThrow(() -> listener.consume(invalidJson));

        verify(service, never()).sendEmail(any());
    }

    /**
     * Ensures the listener continues processing even if
     * {@link EmailService#sendEmail(EmailRequest)} throws an exception.
     *
     * The exception should be caught internally and not propagate.
     */
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