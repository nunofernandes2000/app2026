package com.example.spring_es.spring.domain;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "unidade_curricular")
public class UnidadeCurricular {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    // Relacionamento: Uma UC tem um Docente responsável
    @ManyToOne
    @JoinColumn(name = "docente_id")
    private Docente docente;

    // Uma UC tem vários exercícios (Ponto 1.b da imagem)
    @OneToMany(mappedBy = "unidadeCurricular")
    private List<Exercicio> exercicios;

    public UnidadeCurricular() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public Docente getDocente() { return docente; }
    public void setDocente(Docente docente) { this.docente = docente; }
}