package com.example.demo.model;

import jakarta.persistence.*;

import com.example.demo.exception.BadRequestException;

@Entity
public class Category {

    public static final String TYPE_INCOME = "INCOME";
    public static final String TYPE_EXPENSE = "EXPENSE";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;

    private String type;

    public Category() {
    }

    public Category(Long id, String name, String type) {
        this.id = id;
        this.name = name;
        this.type = type;
    }

    public void validateType() {
        if (type == null ||
            (!TYPE_INCOME.equals(type) && !TYPE_EXPENSE.equals(type))) {
            throw new BadRequestException("Invalid category type");
        }
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
}
