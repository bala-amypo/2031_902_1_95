package com.example.demo.model;

import com.example.demo.exception.BadRequestException;
import jakarta.persistence.*;

@Entity
@Table(name = "categories", uniqueConstraints = @UniqueConstraint(columnNames = "name"))
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private String type; // INCOME / EXPENSE

    public Category() {}

    public Category(String name, String type) {
        this.name = name;
        this.type = type.toUpperCase();
        validateType();
    }

    private void validateType() {
        if (!type.equals("INCOME") && !type.equals("EXPENSE")) {
            throw new BadRequestException("Invalid category type");
        }
    }

    public Long getId() { return id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getType() { return type; }
    public void setType(String type) {
        this.type = type.toUpperCase();
        validateType();
    }
}
