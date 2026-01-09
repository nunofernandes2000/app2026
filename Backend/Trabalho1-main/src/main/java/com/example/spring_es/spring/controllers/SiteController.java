package com.example.spring_es.spring.controllers;

import com.example.spring_es.spring.repository.ResolucaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController // Nota: Aqui é @Controller, NÃO é @RestController
@CrossOrigin(origins = "http://localhost:5173")
public class SiteController {

    @Autowired
    private ResolucaoRepository resolucaoRepository;

    // Esta função abre a página do Dashboard do Docente
    // URL: http://localhost:8080/docente/dashboard
    @GetMapping("/docente/dashboard")
    public String verDashboard(Model model) {
        // Vamos buscar TODAS as resoluções à base de dados
        // e enviá-las para o HTML com o nome "listaResolucoes"
        model.addAttribute("listaResolucoes", resolucaoRepository.findAll());
        return "dashboard-docente"; // O nome do ficheiro HTML que vamos criar a seguir
    }
}