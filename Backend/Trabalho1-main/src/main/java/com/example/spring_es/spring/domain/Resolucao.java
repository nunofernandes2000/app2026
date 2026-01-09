package com.example.spring_es.spring.domain;

import jakarta.persistence.*;

@Entity
public class Resolucao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Aluno aluno;

    @ManyToOne
    private Exercicio exercicio;

    private int ultimaEtapaConcluida;

    // ✅ NOVO: número de etapas saltadas
    private int etapasSkip = 0;

    private Boolean terminado = false; // Importante: Boolean objeto
    private Double nota;
    private Boolean solicitarAjuda = false;

    public Resolucao() {}

    public Resolucao(Aluno aluno, Exercicio exercicio) {
        this.aluno = aluno;
        this.exercicio = exercicio;
        this.ultimaEtapaConcluida = 0;
        this.etapasSkip = 0;
        this.terminado = false;
        this.solicitarAjuda = false;
    }

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Aluno getAluno() { return aluno; }
    public void setAluno(Aluno aluno) { this.aluno = aluno; }

    public Exercicio getExercicio() { return exercicio; }
    public void setExercicio(Exercicio exercicio) { this.exercicio = exercicio; }

    public int getUltimaEtapaConcluida() { return ultimaEtapaConcluida; }
    public void setUltimaEtapaConcluida(int ultimaEtapaConcluida) { this.ultimaEtapaConcluida = ultimaEtapaConcluida; }

    public int getEtapasSkip() { return etapasSkip; }
    public void setEtapasSkip(int etapasSkip) { this.etapasSkip = etapasSkip; }

    public Boolean getTerminado() { return terminado; }
    public void setTerminado(Boolean terminado) { this.terminado = terminado; }

    public Double getNota() { return nota; }
    public void setNota(Double nota) { this.nota = nota; }

    public Boolean getSolicitarAjuda() { return solicitarAjuda; }
    public void setSolicitarAjuda(Boolean solicitarAjuda) { this.solicitarAjuda = solicitarAjuda; }
}
