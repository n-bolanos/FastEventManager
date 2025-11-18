/* NOT AN ACTUAL TEST FILE */

package com.fem.email;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.SendResult;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class KafkaSimpleProducer {

    public static void main(String[] args) throws Exception {

        // ---- Configuración del producer ----
        Map<String, Object> config = new HashMap<>();
        config.put("bootstrap.servers", "localhost:9092");
        config.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        config.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        ProducerFactory<String, String> pf = new DefaultKafkaProducerFactory<>(config);
        KafkaTemplate<String, String> kafkaTemplate = new KafkaTemplate<>(pf);

        String json = """
        {
          "to": "secorreae@unal.edu.co",
          "subject": "Kafka Test",
          "type": "EVENT_CONFIRMATION",
          "params": {
            "name": "John",
            "eventName": "Demo Event"
          }
        }
        """;

        System.out.println("Enviando mensaje Kafka...");

        CompletableFuture<SendResult<String, String>> future =
                kafkaTemplate.send("email.send", json);

        // Callback moderno usando CompletableFuture
        future.whenComplete((result, ex) -> {
            if (ex == null) {
                System.out.println("Mensaje enviado correctamente");
                System.out.println("Topic: " + result.getRecordMetadata().topic());
                System.out.println("Offset: " + result.getRecordMetadata().offset());
            } else {
                System.out.println("Error enviando mensaje");
                ex.printStackTrace();
            }
        });

        Thread.sleep(2000);
    }
}