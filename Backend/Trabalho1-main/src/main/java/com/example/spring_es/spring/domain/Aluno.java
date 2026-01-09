package com.example.spring_es.spring.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "aluno")
public class Aluno {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    // O campo email é importante ser único para não haver dois alunos com o mesmo login
    @Column(unique = true)
    private String email;

    // ADICIONADO: O campo password que faltava
    private String password;

    // Construtor Vazio (Obrigatório para o JPA)
    public Aluno() {}

    // Construtor com dados (Atualizado para receber a password também)
    public Aluno(String nome, String email, String password) {
        this.nome = nome;
        this.email = email;
        this.password = password;
    }

    // --- Getters e Setters ---

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}