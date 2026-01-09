package com.example.spring_es.spring.services;

import com.example.spring_es.spring.domain.Aluno;
import com.example.spring_es.spring.repository.AlunoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AlunoService {

    @Autowired
    private AlunoRepository alunoRepository;


    public Aluno findById(Long id) {
        Optional<Aluno> aluno = alunoRepository.findById(id);
        return aluno.orElse(null);
    }

    public List<Aluno> findAll() {
        return alunoRepository.findAll();
    }

    public Aluno save(Aluno aluno) {
        return alunoRepository.save(aluno);
    }

    // Se tiveres um método de delete, muda também para Long
    public void deleteById(Long id) {
        alunoRepository.deleteById(id);
    }
}