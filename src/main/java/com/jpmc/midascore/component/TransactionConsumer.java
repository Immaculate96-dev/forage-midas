package com.jpmc.midascore.component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jpmc.midascore.foundation.Transaction;
import com.jpmc.midascore.entity.TransactionRecord;
import com.jpmc.midascore.entity.UserRecord;
import com.jpmc.midascore.repository.UserRepository;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class TransactionConsumer {

    private final TransactionConduit conduit;
    private final UserRepository userRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public TransactionConsumer(TransactionConduit conduit, UserRepository userRepository) {
        this.conduit = conduit;
        this.userRepository = userRepository;
    }

    @KafkaListener(topics = "${general.kafka-topic}", groupId = "midas")
    @Transactional
    public void listen(String message) {
        try {
            Transaction transaction = objectMapper.readValue(message, Transaction.class);

            UserRecord sender = (UserRecord) userRepository.findById(transaction.getSenderId()).orElse(null);
            UserRecord recipient = (UserRecord) userRepository.findById(transaction.getRecipientId()).orElse(null);

            if (sender == null || recipient == null) {
                return; // discard invalid transaction
            }

            if (sender.getBalance() >= transaction.getAmount()) {
                sender.setBalance(sender.getBalance() - transaction.getAmount());
                recipient.setBalance(recipient.getBalance() + transaction.getAmount());

                userRepository.save(sender);
                userRepository.save(recipient);

                TransactionRecord record = new TransactionRecord(sender, recipient, transaction.getAmount());
                conduit.save(record);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
