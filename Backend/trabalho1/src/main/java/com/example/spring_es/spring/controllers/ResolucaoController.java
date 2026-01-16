package com.example.spring_es.spring.controllers;

import com.example.spring_es.spring.domain.Resolucao;
import com.example.spring_es.spring.services.ResolucaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/v1/resolucoes")
public class ResolucaoController {

    @Autowired
    private ResolucaoService resolucaoService;

    @GetMapping
    public List<Resolucao> listarTodas() {
        return resolucaoService.listarTodas();
    }

    @PostMapping("/iniciar")
    public Resolucao iniciar(@RequestParam Long alunoId, @RequestParam Long exercicioId) {
        return resolucaoService.iniciarResolucao(alunoId, exercicioId);
    }

    @PostMapping("/{id}/avancar")
    public Resolucao avancarEtapa(@PathVariable Long id) {
        return resolucaoService.avancarEtapa(id);
    }

    @PutMapping("/{id}/skip")
    public Resolucao skip(@PathVariable Long id) {
        return resolucaoService.skipEtapa(id);
    }

    @PostMapping("/{id}/ajuda")
    public Resolucao pedirAjuda(@PathVariable Long id) {
        return resolucaoService.solicitarAjuda(id);
    }

    @PostMapping("/{id}/avaliar")
    public Resolucao avaliar(@PathVariable Long id, @RequestParam Double nota) {
        return resolucaoService.atribuirNota(id, nota);
    }

    @GetMapping("/aluno/{alunoId}")
    public List<Resolucao> listarPorAluno(@PathVariable Long alunoId) {
        return resolucaoService.buscarPorAluno(alunoId);
    }
}
