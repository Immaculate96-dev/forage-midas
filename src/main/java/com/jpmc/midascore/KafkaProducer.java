package com.jpmc.midascore;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jpmc.midascore.foundation.Transaction;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class KafkaProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;

    @Value("${general.kafka-topic}")
    private String topic;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public KafkaProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void send(String csvLine) {
        try {
            String[] parts = csvLine.split(",");
            Transaction transaction = new Transaction();
            transaction.setSenderId(Long.parseLong(parts[0].trim()));
            transaction.setRecipientId(Long.parseLong(parts[1].trim()));
            transaction.setAmount((float) Double.parseDouble(parts[2].trim()));

            String jsonMessage = objectMapper.writeValueAsString(transaction);
            kafkaTemplate.send(topic, jsonMessage);
        } catch (Exception e) {
            System.err.println("Failed to send line: " + csvLine);
            e.printStackTrace();
        }
    }
}
