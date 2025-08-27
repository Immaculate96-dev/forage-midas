package com.jpmc.midascore.component;

import com.jpmc.midascore.entity.TransactionRecord;
import com.jpmc.midascore.repository.TransactionRepository;
import org.springframework.stereotype.Component;

@Component
public class TransactionConduit {
    private final TransactionRepository repo;

    public TransactionConduit(TransactionRepository repo) {
        this.repo = repo;
    }

    public void save(TransactionRecord record) {
        repo.save(record);
    }
}
