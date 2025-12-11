package com.fem.email.controller;

import com.fem.email.service.EmailService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.utility.DockerImageName;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.mockito.Mockito.*;

@SpringBootTest
@Testcontainers
class EmailKafkaListenerIntegrationTest {

	@Container
	static KafkaContainer kafka =
		new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:7.5.0"));

    @DynamicPropertySource
    static void kafkaProps(DynamicPropertyRegistry registry) {
        registry.add("spring.kafka.bootstrap-servers", kafka::getBootstrapServers);
    }

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @SuppressWarnings("removal")
	@MockBean
    private EmailService emailService;

    private final String TOPIC = "email.send";

    @Test
    @DisplayName("Should process a real Kafka message")
    void listener_processesRealMessage() throws Exception {

        when(emailService.sendEmail(any()))
                .thenReturn(new com.fem.email.dto.EmailResponse(true, "id", null));

        String msg = """
        {
            "to":"user@example.com",
            "subject":"Event Confirmation",
            "type":"EVENT_CONFIRMATION",
            "params":{"eventName":"Meetup"}
        }
        """;

        kafkaTemplate.send(TOPIC, msg).get();

        Thread.sleep(1000);

        verify(emailService, times(1)).sendEmail(any());
    }

    @Test
    @DisplayName("Should process all EmailType variants through real Kafka")
    void listener_processesAllEmailTypes() throws Exception {

        String[] emailTypes = {
                "SUCCESSFUL_REGISTER",
                "PASSWORD_RESET",
                "EVENT_CONFIRMATION",
                "WAITLIST_NOTIFICATION",
                "WAITLIST_PROMOTION",
                "CAPACITY_REACHED"
        };

        when(emailService.sendEmail(any()))
                .thenReturn(new com.fem.email.dto.EmailResponse(true, "id", null));

        for (String type : emailTypes) {

            String msg = """
            {
                "to":"test@example.com",
                "subject":"Test",
                "type":"%s",
                "params":{"key":"value"}
            }
            """.formatted(type);

            kafkaTemplate.send(TOPIC, msg).get();
        }

        Thread.sleep(1500);

        verify(emailService, times(emailTypes.length)).sendEmail(any());
    }

    @Test
    @DisplayName("Should process large params objects via Kafka")
    void listener_largeParamsObject() throws Exception {

        when(emailService.sendEmail(any()))
                .thenReturn(new com.fem.email.dto.EmailResponse(true, "id", null));

        StringBuilder params = new StringBuilder("{");
        for (int i = 0; i < 50; i++) {
            params.append("\"p").append(i).append("\":\"v").append(i).append("\"");
            if (i < 49) params.append(",");
        }
        params.append("}");

        String msg = """
        {
            "to":"large@example.com",
            "subject":"Test",
            "type":"SUCCESSFUL_REGISTER",
            "params":%s
        }
        """.formatted(params);

        kafkaTemplate.send(TOPIC, msg).get();

        Thread.sleep(1000);

        verify(emailService).sendEmail(any());
    }

    @Test
    @DisplayName("Should process special characters and Unicode")
    void listener_specialCharacters() throws Exception {

        when(emailService.sendEmail(any()))
                .thenReturn(new com.fem.email.dto.EmailResponse(true, "id", null));

        String msg = """
        {
            "to":"special@example.com",
            "subject":"Quotes: \\"test\\" and unicode ✓",
            "type":"EVENT_CONFIRMATION",
            "params":{"name":"José García","location":"北京"}
        }
        """;

        kafkaTemplate.send(TOPIC, msg).get();

        Thread.sleep(1000);

        verify(emailService).sendEmail(any());
    }

    @Test
    @DisplayName("Should process multiple consecutive Kafka messages")
    void listener_multipleMessages() throws Exception {

        when(emailService.sendEmail(any()))
                .thenReturn(new com.fem.email.dto.EmailResponse(true, "id", null));

        for (int i = 0; i < 5; i++) {
            String msg = """
            {
                "to":"user%d@example.com",
                "subject":"Msg %d",
                "type":"SUCCESSFUL_REGISTER",
                "params":{"id":"%d"}
            }
            """.formatted(i, i, i);

            kafkaTemplate.send(TOPIC, msg).get();
        }

        Thread.sleep(1500);

        verify(emailService, times(5)).sendEmail(any());
    }
}
