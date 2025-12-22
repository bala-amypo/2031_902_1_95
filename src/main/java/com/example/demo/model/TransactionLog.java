package com.example.demo.model;

import com.example.demo.exception.BadRequestException;
import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "transaction_logs")
public class TransactionLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private User user;

    @ManyToOne(optional = false)
    private Category category;

    private Double amount;

    private String description;

    private LocalDate transactionDate;

    public TransactionLog() {}

    public TransactionLog(User user, Category category, Double amount, String description, LocalDate date) {
        if (amount <= 0) throw new BadRequestException("Amount must be > 0");
        if (date.isAfter(LocalDate.now())) throw new BadRequestException("Future date not allowed");

        this.user = user;
        this.category = category;
        this.amount = amount;
        this.description = description;
        this.transactionDate = date;
    }

    public Long getId() { return id; }

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
