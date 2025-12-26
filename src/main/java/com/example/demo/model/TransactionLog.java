package com.example.demo.model;

import jakarta.persistence.*;
import java.time.LocalDate;

import com.example.demo.exception.BadRequestException;

@Entity
public class TransactionLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User user;

    @ManyToOne
    private Category category;

    private Double amount;

    private String description;

    private LocalDate transactionDate;

    public TransactionLog() {
    }

    public TransactionLog(
            Long id,
            User user,
            Category category,
            double amount,
            String description,
            LocalDate date
    ) {
        this.id = id;
        this.user = user;
        this.category = category;
        this.amount = amount;
        this.description = description;
        this.transactionDate = date;
    }

    public void validate() {

        if (amount == null || amount <= 0) {
            throw new BadRequestException("Invalid amount");
        }

        if (transactionDate != null && transactionDate.isAfter(LocalDate.now())) {
            throw new BadRequestException("Future date not allowed");
        }
    }


    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public Category getCategory() { return category; }
    public void setCategory(Category category) { this.category = category; }

    public Double getAmount() { return amount; }
    public void setAmount(Double amount) { this.amount = amount; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public LocalDate getTransactionDate() { return transactionDate; }
    public void setTransactionDate(LocalDate transactionDate) { this.transactionDate = transactionDate; }
}
