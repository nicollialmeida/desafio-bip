package com.example.backend.api.controller;

import com.example.backend.api.dto.TransferRequest;
import com.example.backend.infra.ejb.BeneficioEjbService;
import com.example.backend.infra.repository.BeneficioRepository;
import com.example.backend.model.Beneficio;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/v1/beneficios")
public class BeneficioController {

    private final BeneficioRepository repository;
    private final BeneficioEjbService ejbService;

    public BeneficioController(BeneficioRepository repository, BeneficioEjbService ejbService) {
        this.repository = repository;
        this.ejbService = ejbService;
    }

    // LISTAR
    @GetMapping
    public List<Beneficio> list() {
        return repository.findAll();
    }

    // BUSCAR POR ID
    @GetMapping("/{id}")
    public Beneficio get(@PathVariable Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Benefício não encontrado: " + id));
    }

    // CRIAR
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Beneficio create(@RequestBody Beneficio body) {
        body.setId(null);
        if (body.getAtivo() == null) body.setAtivo(true);
        return repository.save(body);
    }

    // ATUALIZAR
    @PutMapping("/{id}")
    public Beneficio update(@PathVariable Long id, @RequestBody Beneficio body) {
        Beneficio current = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Benefício não encontrado: " + id));

        current.setNome(body.getNome());
        current.setDescricao(body.getDescricao());
        current.setValor(body.getValor());
        if (body.getAtivo() != null) current.setAtivo(body.getAtivo());

        return repository.save(current);
    }

    // DELETAR
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("Benefício não encontrado: " + id);
        }
        repository.deleteById(id);
    }

    // TRANSFERÊNCIA (chama o EJB)
    @PostMapping("/transfer")
    public ResponseEntity<Void> transfer(@RequestBody TransferRequest req) {
        ejbService.transfer(req.getFromId(), req.getToId(), req.getAmount());
        return ResponseEntity.noContent().build();
    }
}