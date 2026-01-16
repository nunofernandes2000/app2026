package com.example.spring_es;

// 1. IMPORTA A TUA APP PRINCIPAL (O IntelliJ pode pedir para confirmar o import)
import com.example.spring_es.spring.SpringEsApplication;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

// 2. DIZ AO TESTE ONDE ESTÁ A CONFIGURAÇÃO
@SpringBootTest(classes = SpringEsApplication.class)
class SpringEsApplicationTests {

    @Test
    void contextLoads() {
        // Teste simples de arranque
    }

}