package com.example.apesc.controller;

import com.example.apesc.dto.AcervoDocumentalProcessosDTO;
import com.example.apesc.model.AcervoDocumentalProcessos;
import com.example.apesc.service.acervodocumentalprocessos.AcervoDocumentalProcessosService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/acervo-documental-processos")
@RequiredArgsConstructor
public class AcervoDocumentalProcessosController {

    private final AcervoDocumentalProcessosService processoService;

    @PostMapping
    public ResponseEntity<AcervoDocumentalProcessosDTO> save(@RequestBody AcervoDocumentalProcessosDTO dto) {
        AcervoDocumentalProcessos salvo = processoService.save(dto.toEntity());
        return ResponseEntity.status(HttpStatus.CREATED).body(AcervoDocumentalProcessosDTO.fromEntity(salvo));
    }

    @GetMapping
    public ResponseEntity<List<AcervoDocumentalProcessosDTO>> listAll() {
        List<AcervoDocumentalProcessosDTO> processos = processoService.findAllWithRelations().stream()
                .map(AcervoDocumentalProcessosDTO::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(processos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AcervoDocumentalProcessosDTO> findById(@PathVariable Long id) {
        return processoService.findByIdWithRelations(id)
                .map(p -> ResponseEntity.ok(AcervoDocumentalProcessosDTO.fromEntity(p)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("/{id}")
    public ResponseEntity<AcervoDocumentalProcessosDTO> update(@PathVariable Long id, @RequestBody AcervoDocumentalProcessosDTO dto) {
        AcervoDocumentalProcessos atualizado = dto.toEntity();
        atualizado.setId(id);
        AcervoDocumentalProcessos salvo = processoService.update(atualizado);
        return ResponseEntity.ok(AcervoDocumentalProcessosDTO.fromEntity(salvo));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        processoService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/acervo-documental/{acervoDocumentalId}")
    public ResponseEntity<List<AcervoDocumentalProcessosDTO>> findByAcervoDocumento(@PathVariable Long acervoDocumentalId) {
        List<AcervoDocumentalProcessosDTO> processos = processoService.findByAcervoDocumento(acervoDocumentalId).stream()
                .map(AcervoDocumentalProcessosDTO::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(processos);
    }
}
