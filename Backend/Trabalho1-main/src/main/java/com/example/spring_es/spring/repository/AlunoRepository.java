package com.example.spring_es.spring.repository;

import com.example.spring_es.spring.domain.Aluno;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AlunoRepository extends JpaRepository<Aluno, Long> {
    // Ao usar JpaRepository, o findAll() passa a devolver List<Aluno> automaticamente.
    Aluno findByEmail(String email);
}