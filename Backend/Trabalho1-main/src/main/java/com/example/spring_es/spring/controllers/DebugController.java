package com.example.spring_es.spring.controllers;

import com.example.spring_es.spring.domain.User;
import com.example.spring_es.spring.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
public class DebugController {

    @Autowired
    private UserRepository userRepository;

    // Acede a este link no browser para ver o que o Java encontra
    @GetMapping("/api/public/debug")
    public String verUtilizadores() {
        try {
            List<User> users = (List<User>) userRepository.findAll();
            if (users.isEmpty()) {
                return "O Java ligou à BD mas a tabela 'user' está VAZIA!";
            }

            StringBuilder sb = new StringBuilder();
            sb.append("Encontrei estes utilizadores na BD:<br>");
            for (User u : users) {
                sb.append("ID: ").append(u.getId())
                        .append(" | Email: ").append(u.getEmail())
                        .append(" | Password: ").append(u.getPassword())
                        .append(" | Roles: ").append(u.getRoles().size())
                        .append("<br>");
            }
            return sb.toString();
        } catch (Exception e) {
            return "Erro ao ler a BD: " + e.getMessage();
        }
    }
}