package com.example.spring_es.spring.services;

import com.example.spring_es.spring.domain.Aluno;
import com.example.spring_es.spring.domain.Exercicio;
import com.example.spring_es.spring.domain.Resolucao;
import com.example.spring_es.spring.repository.AlunoRepository;
import com.example.spring_es.spring.repository.ExercicioRepository;
import com.example.spring_es.spring.repository.ResolucaoRepository;
import com.example.spring_es.spring.services.exceptions.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ResolucaoService {

    @Autowired
    private ResolucaoRepository resolucaoRepository;

    @Autowired
    private AlunoRepository alunoRepository;

    @Autowired
    private ExercicioRepository exercicioRepository;

    // 1. INICIAR EXERCÍCIO
    public Resolucao iniciarResolucao(Long alunoId, Long exercicioId) {
        Optional<Resolucao> existente = resolucaoRepository.findByAlunoIdAndExercicioId(alunoId, exercicioId);
        if (existente.isPresent()) return existente.get();

        Aluno aluno = alunoRepository.findById(alunoId)
                .orElseThrow(() -> new RuntimeException("Aluno não encontrado"));
        Exercicio exercicio = exercicioRepository.findById(exercicioId)
                .orElseThrow(() -> new RuntimeException("Exercício não encontrado"));

        return resolucaoRepository.save(new Resolucao(aluno, exercicio));
    }

    // 2. AVANÇAR ETAPA
    public Resolucao avancarEtapa(Long resolucaoId) {
        Resolucao resolucao = resolucaoRepository.findById(resolucaoId)
                .orElseThrow(() -> new EntityNotFoundException("Resolução não encontrada"));

        if (Boolean.TRUE.equals(resolucao.getTerminado())) {
            return resolucao;
        }

        Exercicio exercicio = resolucao.getExercicio();
        int total = exercicio.getTotalEtapas();

        // avança 1 etapa (máximo = total)
        int proxima = resolucao.getUltimaEtapaConcluida() + 1;
        if (proxima >= total) {
            resolucao.setUltimaEtapaConcluida(total);
            resolucao.setTerminado(true);
            resolucao.setSolicitarAjuda(false);
        } else {
            resolucao.setUltimaEtapaConcluida(proxima);
        }

        return resolucaoRepository.save(resolucao);
    }

    // 2B. SKIP ETAPA (novo) — avança e incrementa etapasSkip
    public Resolucao skipEtapa(Long resolucaoId) {
        Resolucao resolucao = resolucaoRepository.findById(resolucaoId)
                .orElseThrow(() -> new EntityNotFoundException("Resolução não encontrada"));

        if (Boolean.TRUE.equals(resolucao.getTerminado())) {
            return resolucao;
        }

        Exercicio exercicio = resolucao.getExercicio();
        int total = exercicio.getTotalEtapas();

        int atual = resolucao.getUltimaEtapaConcluida();

        if (atual < total) {
            resolucao.setUltimaEtapaConcluida(atual + 1);
            resolucao.setEtapasSkip(resolucao.getEtapasSkip() + 1);
        }

        if (resolucao.getUltimaEtapaConcluida() >= total) {
            resolucao.setUltimaEtapaConcluida(total);
            resolucao.setTerminado(true);
            resolucao.setSolicitarAjuda(false);
        }

        return resolucaoRepository.save(resolucao);
    }

    // 3. PEDIR AJUDA
    public Resolucao solicitarAjuda(Long resolucaoId) {
        Resolucao resolucao = resolucaoRepository.findById(resolucaoId)
                .orElseThrow(() -> new RuntimeException("Resolução não encontrada"));
        resolucao.setSolicitarAjuda(true);
        return resolucaoRepository.save(resolucao);
    }

    // 4. ATRIBUIR NOTA (manual com penalização por SKIP)
    public Resolucao atribuirNota(Long resolucaoId, Double notaMax) {
        Resolucao resolucao = resolucaoRepository.findById(resolucaoId)
                .orElseThrow(() -> new RuntimeException("Resolução não encontrada"));

        double notaFinal = aplicarPenalizacaoSkip(resolucao, notaMax);

        resolucao.setNota(notaFinal);
        resolucao.setTerminado(true);
        resolucao.setSolicitarAjuda(false);

        return resolucaoRepository.save(resolucao);
    }

    private double aplicarPenalizacaoSkip(Resolucao r, double notaMax) {
        int total = r.getExercicio().getTotalEtapas();
        int skip = r.getEtapasSkip();

        if (total <= 0) return 0.0;

        // cada etapa vale notaMax/total e cada skip perde esse valor
        double perda = (notaMax / (double) total) * (double) skip;
        double nota = notaMax - perda;

        if (nota < 0) nota = 0;

        // arredonda a 1 casa decimal
        return Math.round(nota * 10.0) / 10.0;
    }

    // 5. BUSCAR POR ALUNO
    public List<Resolucao> buscarPorAluno(Long alunoId) {
        return resolucaoRepository.findByAlunoId(alunoId);
    }

    // Extra: Listar todas
    public List<Resolucao> listarTodas() {
        return resolucaoRepository.findAll();
    }
}
