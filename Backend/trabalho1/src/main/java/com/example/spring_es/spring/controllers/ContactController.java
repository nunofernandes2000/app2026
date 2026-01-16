package com.example.spring_es.spring.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
public class ContactController {

    // Mostra o formulário quando vais a /contato
    @GetMapping("/contato")
    public String showForm() {
        return "contact"; // Procura contact.html
    }

    // Recebe os dados do formulário
    @PostMapping("/contato")
    public String submitForm(@RequestParam String nome, @RequestParam String mensagem, Model model) {
        // Envia a mensagem de sucesso para o HTML
        model.addAttribute("sucesso", true);
        model.addAttribute("mensagemFeedback", "Obrigado " + nome + "! A mensagem foi recebida.");

        return "contact"; // Mantém na mesma página
    }
}