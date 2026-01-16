package com.example.spring_es.spring.dtos;

public class LoginRequest {
    private String email;
    private String password;

    // Estes Getters e Setters são OBRIGATÓRIOS
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}