package com.example.spring_es.spring.controllers;

import com.example.spring_es.spring.domain.Exercicio;
import com.example.spring_es.spring.domain.Resolucao;
import com.example.spring_es.spring.domain.UnidadeCurricular;
import com.example.spring_es.spring.repository.ExercicioRepository;
import com.example.spring_es.spring.repository.ResolucaoRepository;
import com.example.spring_es.spring.repository.UnidadeCurricularRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/exercicios")
@CrossOrigin(origins = "http://localhost:5173")
public class ExercicioController {

    @Autowired
    private ExercicioRepository exercicioRepository;

    @Autowired
    private UnidadeCurricularRepository unidadeCurricularRepository;

    @Autowired
    private ResolucaoRepository resolucaoRepository;

    /**
     * LISTAR EXERCÍCIOS
     * - Sem parâmetros: devolve todos
     * - Com ?ucId=1: devolve apenas os exercícios dessa UC
     */
    @GetMapping
    public List<Exercicio> listar(@RequestParam(required = false) Long ucId) {
        if (ucId != null) {
            return exercicioRepository.findByUnidadeCurricularId(ucId);
        }
        return exercicioRepository.findAll();
    }

    /**
     * CRIAR EXERCÍCIO (docente cria a partir de uma UC)
     * Body esperado:
     * {
     *   "enunciado": "...",
     *   "totalEtapas": 5,
     *   "unidadeCurricularId": 1
     * }
     */
    @PostMapping
    public ResponseEntity<?> criar(@RequestBody CreateExercicioBody body) {
        if (body == null || body.enunciado == null || body.enunciado.isBlank() || body.totalEtapas == null || body.totalEtapas < 1 || body.unidadeCurricularId == null) {
            return ResponseEntity.badRequest().body("Dados inválidos");
        }

        UnidadeCurricular uc = unidadeCurricularRepository.findById(body.unidadeCurricularId)
                .orElse(null);
        if (uc == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Unidade Curricular não encontrada");
        }

        Exercicio ex = new Exercicio();
        ex.setEnunciado(body.enunciado);
        ex.setTotalEtapas(body.totalEtapas);
        ex.setUnidadeCurricular(uc);

        return ResponseEntity.status(HttpStatus.CREATED).body(exercicioRepository.save(ex));
    }

    /**
     * PROGRESSO POR EXERCÍCIO (para o docente ver a turma)
     */
    @GetMapping("/{exercicioId}/resolucoes")
    public List<Resolucao> listarResolucoes(@PathVariable Long exercicioId) {
        return resolucaoRepository.findByExercicioId(exercicioId);
    }

    // DTO interno simples para criar exercício
    public static class CreateExercicioBody {
        public String enunciado;
        public Integer totalEtapas;
        public Long unidadeCurricularId;
    }
}
