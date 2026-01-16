package com.example.spring_es.spring.repository;

import com.example.spring_es.spring.domain.Docente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DocenteRepository extends JpaRepository<Docente, Long> {
    Docente findByEmail(String email);
}