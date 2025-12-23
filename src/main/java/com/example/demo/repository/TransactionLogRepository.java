package com.example.demo.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.TransactionLog;
import com.example.demo.model.User;

public interface TransactionLogRepository extends JpaRepository<TransactionLog, Long> {

    List<TransactionLog> findByUser(User user);

    List<TransactionLog> findByUserAndTransactionDateBetween(
            User user,
            LocalDate start,
            LocalDate end
    );
}
