package com.example.backend.infra.ejb;

import com.example.backend.model.Beneficio;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.LockModeType;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;

@Service
public class BeneficioEjbService {

    @PersistenceContext
    private EntityManager em;

    @Transactional
    public void transfer(Long fromId, Long toId, BigDecimal amount) {
       

        // BUG: sem validações, sem locking, pode gerar saldo negativo e lost update
         if (fromId == null || toId == null) {
            throw new IllegalArgumentException("IDs não podem ser nulos.");
        }
        if (fromId.equals(toId)) {
            throw new IllegalArgumentException("ID do remetente e ID do destinatário não podem ser iguais.");
        }
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Quantia transferida deve ser maior que 0.");
        }

        Beneficio from = em.find(Beneficio.class, fromId, LockModeType.OPTIMISTIC_FORCE_INCREMENT);
        Beneficio to   = em.find(Beneficio.class, toId, LockModeType.OPTIMISTIC_FORCE_INCREMENT);

        if (from == null || to == null) {
            throw new EntityNotFoundException("Benefício(s) não encontrado(s)");
        }

        if (from.getValor().compareTo(amount) < 0) {
            throw new IllegalStateException("Saldo insuficiente");
        }
        
        from.setValor(from.getValor().subtract(amount));
        to.setValor(to.getValor().add(amount));

        em.merge(from);
        em.merge(to);
    }
}
