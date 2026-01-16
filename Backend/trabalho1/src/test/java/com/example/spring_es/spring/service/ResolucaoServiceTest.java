package com.example.spring_es.spring.service;

import com.example.spring_es.spring.domain.Aluno;
import com.example.spring_es.spring.domain.Exercicio;
import com.example.spring_es.spring.domain.Resolucao;
import com.example.spring_es.spring.repository.AlunoRepository;
import com.example.spring_es.spring.repository.ExercicioRepository;
import com.example.spring_es.spring.repository.ResolucaoRepository;
import com.example.spring_es.spring.services.ResolucaoService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ResolucaoServiceTest {

    @Mock
    private ResolucaoRepository resolucaoRepository;

    @Mock
    private AlunoRepository alunoRepository;

    @Mock
    private ExercicioRepository exercicioRepository;

    @InjectMocks
    private ResolucaoService resolucaoService;

    // TESTE 1: Verificar se inicia a resolução corretamente
    @Test
    void testeIniciarResolucao() {
        Aluno aluno = new Aluno();
        aluno.setId(1L);
        Exercicio exercicio = new Exercicio();
        exercicio.setId(1L);
        exercicio.setTotalEtapas(5);

        Resolucao resolucaoEsperada = new Resolucao(aluno, exercicio);

        when(alunoRepository.findById(1L)).thenReturn(Optional.of(aluno));
        when(exercicioRepository.findById(1L)).thenReturn(Optional.of(exercicio));
        when(resolucaoRepository.save(any(Resolucao.class))).thenReturn(resolucaoEsperada);

        Resolucao resultado = resolucaoService.iniciarResolucao(1L, 1L);

        assertNotNull(resultado);
        assertEquals("João", resultado.getAluno().getNome());
    }

    // TESTE 2: Verificar se avança etapa
    @Test
    void testeAvancarEtapa() {
        Exercicio exercicio = new Exercicio();
        exercicio.setTotalEtapas(5);

        Resolucao resolucao = new Resolucao();
        resolucao.setId(10L);
        resolucao.setExercicio(exercicio);
        resolucao.setUltimaEtapaConcluida(1);

        when(resolucaoRepository.findById(10L)).thenReturn(Optional.of(resolucao));
        when(resolucaoRepository.save(any(Resolucao.class))).thenReturn(resolucao);

        Resolucao resultado = resolucaoService.avancarEtapa(10L);

        assertEquals(2, resultado.getUltimaEtapaConcluida());
    }
}