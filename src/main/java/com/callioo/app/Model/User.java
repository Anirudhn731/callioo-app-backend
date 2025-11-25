package com.callioo.app.Model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity(name = "User")
public class User {

    @Id
    private String email;

    @Column(nullable = false, unique = false)
    private String fullName;
    @Column(nullable = false, unique = false)
    private String password;

    private String avatarImage;

    // No-arg constructor
    public User() {
    }

    // Getters and Setters
    public String getFullName() {
        return this.fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAvatarImage() {
        return this.avatarImage;
    }

    public void setAvatarImage(String avatarImage) {
        this.avatarImage = avatarImage;
    }

}
