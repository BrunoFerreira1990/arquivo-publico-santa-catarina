package com.example.apesc.controller;

import com.example.apesc.dto.AcervoDocumentalProcessosDTO;
import com.example.apesc.model.AcervoDocumentalProcessos;
import com.example.apesc.service.acervodocumentalprocessos.AcervoDocumentalProcessosService;
import com.example.apesc.specification.AcervoDocumentalProcessosSearchFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/acervo-documental/processos")
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

    @GetMapping("/search")
    public ResponseEntity<List<AcervoDocumentalProcessosDTO>> search(@ModelAttribute AcervoDocumentalProcessosSearchFilter filtro) {
        List<AcervoDocumentalProcessosDTO> processos = processoService.search(filtro).stream()
                .map(AcervoDocumentalProcessosDTO::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(processos);
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
}
