package com.fem.authentication.service;

import com.fem.authentication.dto.EmailRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class EmailKafkaProducer {

    private final KafkaTemplate<String, EmailRequest> kafkaTemplate;
    private final String topic;

    public EmailKafkaProducer(KafkaTemplate<String, EmailRequest> kafkaTemplate,
                              @Value("${email.topic:email.send}") String topic) {
        this.kafkaTemplate = kafkaTemplate;
        this.topic = topic;
    }

    @SuppressWarnings("null")
    public void sendEmail(EmailRequest request) {
        kafkaTemplate.send(topic, request);
    }
}
