package com.example.apesc.controller;

import com.example.apesc.dto.AcervoIconograficoAssuntosDTO;
import com.example.apesc.model.AcervoIconograficoAssuntos;
import com.example.apesc.service.acervoiconograficoassuntos.AcervoIconograficoAssuntosService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/acervo-iconografico-assuntos")
@RequiredArgsConstructor
public class AcervoIconograficoAssuntosController {

    private final AcervoIconograficoAssuntosService assuntosService;

    @PostMapping
    public ResponseEntity<AcervoIconograficoAssuntosDTO> save(@RequestBody AcervoIconograficoAssuntosDTO dto) {
        AcervoIconograficoAssuntos salvo = assuntosService.save(dto.toEntity());
        return ResponseEntity.status(HttpStatus.CREATED).body(AcervoIconograficoAssuntosDTO.fromEntity(salvo));
    }

    @GetMapping
    public ResponseEntity<List<AcervoIconograficoAssuntosDTO>> listAll() {
        List<AcervoIconograficoAssuntosDTO> assuntos = assuntosService.findAll().stream()
                .map(AcervoIconograficoAssuntosDTO::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(assuntos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AcervoIconograficoAssuntosDTO> findById(@PathVariable Long id) {
        return assuntosService.findById(id)
                .map(a -> ResponseEntity.ok(AcervoIconograficoAssuntosDTO.fromEntity(a)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("/{id}")
    public ResponseEntity<AcervoIconograficoAssuntosDTO> update(@PathVariable Long id, @RequestBody AcervoIconograficoAssuntosDTO dto) {
        AcervoIconograficoAssuntos atualizado = dto.toEntity();
        atualizado.setId(id);
        AcervoIconograficoAssuntos salvo = assuntosService.update(atualizado);
        return ResponseEntity.ok(AcervoIconograficoAssuntosDTO.fromEntity(salvo));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        assuntosService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
