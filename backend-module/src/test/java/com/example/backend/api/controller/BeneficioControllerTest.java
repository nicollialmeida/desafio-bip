package com.example.backend.api.controller;

import com.example.backend.infra.ejb.BeneficioEjbService;
import com.example.backend.infra.repository.BeneficioRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Map;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(
    controllers = BeneficioController.class,
    excludeAutoConfiguration = {
        org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class,
        org.springframework.boot.autoconfigure.security.servlet.SecurityFilterAutoConfiguration.class
    }
)
class BeneficioControllerTest {

    @Autowired private MockMvc mvc;
    @Autowired private ObjectMapper mapper;

    @MockBean private BeneficioEjbService ejbService;
    @MockBean private BeneficioRepository beneficioRepository; // ✅ se o controller usa repository também

    @Test
    void deveRetornar409QuandoSaldoInsuficiente() throws Exception {
        doThrow(new IllegalStateException("Saldo insuficiente"))
            .when(ejbService).transfer(eq(1L), eq(2L), eq(new BigDecimal("999.99")));

        String body = mapper.writeValueAsString(Map.of(
            "fromId", 1,
            "toId", 2,
            "amount", 999.99
        ));

        mvc.perform(post("/api/v1/beneficios/transfer")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
            .andExpect(status().isConflict());
    }
}