package com.example.spring_es.spring.controllers;

import com.example.spring_es.spring.domain.UnidadeCurricular;
import com.example.spring_es.spring.repository.UnidadeCurricularRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/unidades")
@CrossOrigin(origins = "http://localhost:5173")
public class UnidadeCurricularController {

    @Autowired
    private UnidadeCurricularRepository unidadeCurricularRepository;

    @GetMapping
    public List<UnidadeCurricular> listar() {
        return unidadeCurricularRepository.findAll();
    }
}
