package com.fem.email.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fem.email.dto.EmailRequest;
import com.fem.email.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * Kafka listener responsible for consuming messages from the "email.send" topic
 * and delegating email sending tasks to the EmailService.
 * 
 * The listener deserializes incoming JSON messages into {@link EmailRequest}
 * objects and safely handles parsing or processing errors without stopping
 * the Kafka consumer.
 * 
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class EmailKafkaListener {

    /** Service used to process and send emails coming from Kafka events. */
    private final EmailService emailService;

    /** JSON mapper used to deserialize Kafka message payloads. */
    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Consumes messages from the Kafka "email.send" topic.
     * 
     * The incoming message is expected to be a JSON representation of an
     * {@link EmailRequest}. Any parsing or processing errors are caught and logged
     * to ensure the listener continues functioning.
     * 
     *
     * @param message raw JSON string representing an email request
     */
    @KafkaListener(topics = "${email.topic}", groupId = "email-service")
    public void consume(String message) {
        try {
            EmailRequest req = objectMapper.readValue(message, EmailRequest.class);
            emailService.sendEmail(req);

        } catch (Exception e) {
            log.error("Error processing Kafka message: {}", e.getMessage(), e);
        }
    }
}