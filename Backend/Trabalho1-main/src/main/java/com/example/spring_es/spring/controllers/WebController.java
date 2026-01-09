package com.example.spring_es.spring.controllers;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

// MUDANÇA IMPORTANTE: Usamos @RestController em vez de @Controller
// Assim ele escreve texto no ecrã em vez de procurar ficheiros HTML.
@RestController
@CrossOrigin(origins = "http://localhost:5173")
public class WebController {

    @GetMapping("/")
    public String index() {
        return "<h1>LOGIN COM SUCESSO! 🔓</h1>" +
                "<p>Parabéns, entraste no sistema.</p>" +
                "<br><a href='/api/v1/alunos'>Ver Lista de Alunos</a>";
    }
}