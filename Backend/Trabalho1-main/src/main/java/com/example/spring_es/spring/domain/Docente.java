package com.example.spring_es.spring.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "docente")
public class Docente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    @Column(unique = true)
    private String email;

    /**
     * A tua tabela "docente" (conforme o esquema que mostraste) não tem coluna password.
     * A autenticação é feita na tabela "user".
     * Mantemos este campo apenas para compatibilidade de código, mas não é persistido.
     */
    @Transient
    private String password;

    // Construtor vazio (obrigatório para JPA)
    public Docente() {}

    // Construtor com dados
    public Docente(String nome, String email, String password) {
        this.nome = nome;
        this.email = email;
        this.password = password;
    }

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}