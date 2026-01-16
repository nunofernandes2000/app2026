package com.example.spring_es.spring.repository;

import com.example.spring_es.spring.domain.Resolucao; // Confirma se a pasta é 'domain'
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ResolucaoRepository extends JpaRepository<Resolucao, Long> {

    // 1. Para o ALUNO ver os seus exercícios
    // Usado no Service: buscarPorAluno(alunoId)
    List<Resolucao> findByAlunoId(Long alunoId);

    // 2. Para encontrar uma resolução específica (Evitar duplicados ao iniciar)
    // Usado no Service: iniciarResolucao(alunoId, exercicioId)
    Optional<Resolucao> findByAlunoIdAndExercicioId(Long alunoId, Long exercicioId);

    // 3. (EXTRA ÚTIL) Para o PROFESSOR ver quem pediu ajuda 🚨
    // Isto procura automaticamente onde solicitarAjuda = true
    List<Resolucao> findBySolicitarAjudaTrue();

    // 4. (EXTRA) Para o PROFESSOR ver estatísticas por exercício
    List<Resolucao> findByExercicioId(Long exercicioId);
}