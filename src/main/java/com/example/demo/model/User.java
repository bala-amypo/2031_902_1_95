package com.example.demo.model;

import jakarta.persistence.*;

@Entity
@Table(name = "users", uniqueConstraints = @UniqueConstraint(columnNames = "email"))
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    private String password;

    public User() {}

    // REQUIRED BY TESTCASES → EXACT PARAM ORDER
    public User(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }

    // validate() REQUIRED BY TESTCASES
    public void validate() {
        if (name == null || name.trim().isEmpty())
            throw new IllegalArgumentException("Name cannot be empty");

        if (email == null || !email.contains("@"))
            throw new IllegalArgumentException("Invalid email");

        if (password == null || password.length() < 6)
            throw new IllegalArgumentException("Password too short");
    }

    // Getters & Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}
