package com.example.spring_es.spring.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "exercicio")
public class Exercicio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String enunciado;


    private Integer totalEtapas;

    // Relacionamento: O exercício pertence a uma Unidade Curricular
    @ManyToOne
    @JoinColumn(name = "unidade_curricular_id")
    private UnidadeCurricular unidadeCurricular;

    public Exercicio() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getEnunciado() { return enunciado; }
    public void setEnunciado(String enunciado) { this.enunciado = enunciado; }

    public Integer getTotalEtapas() { return totalEtapas; }
    public void setTotalEtapas(Integer totalEtapas) { this.totalEtapas = totalEtapas; }

    public UnidadeCurricular getUnidadeCurricular() { return unidadeCurricular; }
    public void setUnidadeCurricular(UnidadeCurricular unidadeCurricular) {
        this.unidadeCurricular = unidadeCurricular;
    }
}