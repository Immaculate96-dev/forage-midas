package com.jpmc.midascore.entity;

import jakarta.persistence.*;


@Entity
@Table(name = "users")
public class UserRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    private String email;

    // Other user fields

    // Constructors, getters, setters

    public UserRecord(String userDatum, float v) {}

    public UserRecord(String username, String email) {
        this.username = username;
        this.email = email;
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public float getBalance() {
        return 0;
    }

    public void setBalance(float v) {

    }
}