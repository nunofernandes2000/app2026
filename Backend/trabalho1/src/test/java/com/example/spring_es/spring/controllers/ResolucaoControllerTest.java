package com.example.spring_es.spring.controllers;

import com.example.spring_es.spring.domain.Resolucao;
import com.example.spring_es.spring.services.ResolucaoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;


import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ResolucaoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ResolucaoService resolucaoService;

    // TESTE 3: Tentar avançar sem login (deve ser proibido)
    @Test
    void testeSemLogin() throws Exception {
        mockMvc.perform(post("/api/v1/resolucoes/1/avancar"))
                .andExpect(status().isUnauthorized());
    }

    // TESTE 4: Tentar avançar COM login (deve dar sucesso)
    @Test
    @WithMockUser(username = "admin")
    void testeComLogin() throws Exception {
        Resolucao resolucaoMock = new Resolucao();

        // CORREÇÃO AQUI: Usamos 1L em vez de anyLong() para evitar o erro de tipos
        when(resolucaoService.avancarEtapa(1L)).thenReturn(resolucaoMock);

        mockMvc.perform(post("/api/v1/resolucoes/1/avancar"))
                .andExpect(status().isOk());
    }
}