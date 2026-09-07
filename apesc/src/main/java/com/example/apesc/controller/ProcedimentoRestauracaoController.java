package com.example.apesc.controller;

import com.example.apesc.dto.ProcedimentoRestauracaoDTO;
import com.example.apesc.model.ProcedimentoRestauracao;
import com.example.apesc.service.procedimentorestauracao.ProcedimentoRestauracaoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/laboratorio/restauracao")
@RequiredArgsConstructor
public class ProcedimentoRestauracaoController {

    private final ProcedimentoRestauracaoService procedimentoRestauracaoService;

    @GetMapping
    public ResponseEntity<List<ProcedimentoRestauracaoDTO>> findAll() {
        List<ProcedimentoRestauracao> procedimentos = procedimentoRestauracaoService.findAll();
        return ResponseEntity.ok(procedimentos.stream().map(ProcedimentoRestauracaoDTO::fromEntity).collect(Collectors.toList()));
    }

    @PostMapping
    public ResponseEntity<ProcedimentoRestauracaoDTO> save(@RequestBody ProcedimentoRestauracaoDTO dto) {
        ProcedimentoRestauracao salvo = procedimentoRestauracaoService.save(dto.toEntity());
        return ResponseEntity.status(HttpStatus.CREATED).body(ProcedimentoRestauracaoDTO.fromEntity(salvo));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProcedimentoRestauracaoDTO> findById(@PathVariable Long id) {
        ProcedimentoRestauracao entidade = procedimentoRestauracaoService.findById(id);
        if (entidade != null) {
            return ResponseEntity.ok(ProcedimentoRestauracaoDTO.fromEntity(entidade));
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/numero-documento/{numeroDocumento}")
    public ResponseEntity<ProcedimentoRestauracaoDTO> findByNumeroDocumento(@PathVariable Integer numeroDocumento) {
        ProcedimentoRestauracao entidade = procedimentoRestauracaoService.findByNumeroDocumento(numeroDocumento);
        return ResponseEntity.ok(ProcedimentoRestauracaoDTO.fromEntity(entidade));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ProcedimentoRestauracaoDTO> update(@PathVariable Integer id, @RequestBody ProcedimentoRestauracaoDTO dto) {
        ProcedimentoRestauracao entity = dto.toEntity();
        entity.setId(id);
        ProcedimentoRestauracao atualizado = procedimentoRestauracaoService.update(entity);
        return ResponseEntity.ok(ProcedimentoRestauracaoDTO.fromEntity(atualizado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        procedimentoRestauracaoService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
