package com.example.spring_es.spring.controllers;

import com.example.spring_es.spring.domain.Aluno;
import com.example.spring_es.spring.repository.AlunoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.spring_es.spring.dtos.LoginRequest;

import java.util.List;

@RestController
@RequestMapping("/api/v1/alunos") // Define o endereço base
@CrossOrigin(origins = "http://localhost:5173")
public class AlunoController {

    @Autowired
    private AlunoRepository alunoRepository;

    // 1. LISTAR ALUNOS (GET) -> Era aqui que tinhas o erro ou a falta de método
    @GetMapping
    public List<Aluno> listarAlunos() {
        return alunoRepository.findAll();
    }

    // 2. CRIAR ALUNO (POST) - Exemplo extra caso queiras usar no futuro
    @PostMapping
    public Aluno criarAluno(@RequestBody Aluno aluno) {
        return alunoRepository.save(aluno);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        System.out.println("--- INÍCIO DO LOGIN ---");
        System.out.println("1. Email recebido do React: [" + request.getEmail() + "]");
        System.out.println("2. Password recebida do React: [" + request.getPassword() + "]");

        // Tenta buscar o aluno
        Aluno aluno = alunoRepository.findByEmail(request.getEmail());

        if (aluno == null) {
            System.out.println("3. ERRO: O alunoRepository devolveu NULL. O email não existe na BD.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciais inválidas (Email não encontrado)");
        }

        System.out.println("3. Aluno encontrado na BD: [" + aluno.getEmail() + "]");
        System.out.println("4. Password gravada na BD: [" + aluno.getPassword() + "]");

        // Verifica a password
        boolean passwordBateCerto = aluno.getPassword().equals(request.getPassword());
        System.out.println("5. As passwords são iguais? " + passwordBateCerto);

        if (!passwordBateCerto) {
            System.out.println("ERRO: Password errada.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciais inválidas (Password errada)");
        }

        System.out.println("SUCESSO: Login autorizado!");

        // Se usares AuthResponse usa esse, senão devolve o aluno simples
        // return ResponseEntity.ok(new AuthResponse(aluno.getNome(), "ALUNO", aluno));
        return ResponseEntity.ok(aluno);
    }
}