package com.fem.email.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fem.email.dto.EmailRequest;
import com.fem.email.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class EmailKafkaListener {

    private final EmailService emailService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @KafkaListener(topics = "email.send", groupId = "email-service")
    public void consume(String message) {
        try {
            EmailRequest req = objectMapper.readValue(message, EmailRequest.class);
            emailService.sendEmail(req);

        } catch (Exception e) {
            log.error("Error processing Kafka message: {}", e.getMessage(), e);
        }
    }
}