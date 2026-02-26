package com.example.backend.infra.ejb;

import com.example.backend.model.Beneficio;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class BeneficioEjbServiceTest {

    @Autowired
    private BeneficioEjbService service;

    @Autowired
    private EntityManager em;

    @Test
    void deveTransferirQuandoTemSaldo() {
        Beneficio a = new Beneficio();
        a.setNome("A");
        a.setDescricao("origem");
        a.setValor(new BigDecimal("100.00"));
        a.setAtivo(true);

        Beneficio b = new Beneficio();
        b.setNome("B");
        b.setDescricao("destino");
        b.setValor(new BigDecimal("50.00"));
        b.setAtivo(true);

        em.persist(a);
        em.persist(b);
        em.flush();

        service.transfer(a.getId(), b.getId(), new BigDecimal("30.00"));
        em.flush();
        em.clear();

        Beneficio aDb = em.find(Beneficio.class, a.getId());
        Beneficio bDb = em.find(Beneficio.class, b.getId());

        assertEquals(new BigDecimal("70.00"), aDb.getValor());
        assertEquals(new BigDecimal("80.00"), bDb.getValor());
    }

    @Test
    void deveFalharQuandoSaldoInsuficiente() {
        Beneficio a = new Beneficio();
        a.setNome("A");
        a.setDescricao("origem");
        a.setValor(new BigDecimal("10.00"));
        a.setAtivo(true);

        Beneficio b = new Beneficio();
        b.setNome("B");
        b.setDescricao("destino");
        b.setValor(new BigDecimal("0.00"));
        b.setAtivo(true);

        em.persist(a);
        em.persist(b);
        em.flush();

        assertThrows(IllegalStateException.class, () ->
                service.transfer(a.getId(), b.getId(), new BigDecimal("30.00"))
        );
    }
}