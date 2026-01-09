package com.example.spring_es.spring.repository;

import com.example.spring_es.spring.domain.Exercicio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExercicioRepository extends JpaRepository<Exercicio, Long> {
    // Encontrar todos os exercícios de uma Unidade Curricular específica
    List<Exercicio> findByUnidadeCurricularId(Long ucId);
}