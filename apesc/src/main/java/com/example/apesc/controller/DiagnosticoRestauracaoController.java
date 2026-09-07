package com.example.apesc.controller;

import com.example.apesc.dto.DiagnosticoRestauracaoDTO;
import com.example.apesc.model.DiagnosticoRestauracao;
import com.example.apesc.service.diagnosticorestauracao.DiagnosticoRestauracaoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/laboratorio/diagnostico")
@RequiredArgsConstructor
public class DiagnosticoRestauracaoController {

    private final DiagnosticoRestauracaoService diagnosticoRestauracaoService;

    @GetMapping
    public ResponseEntity<List<DiagnosticoRestauracaoDTO>> findAll() {
        List<DiagnosticoRestauracao> diagnosticos = diagnosticoRestauracaoService.findAll();
        return ResponseEntity.ok(diagnosticos.stream().map(DiagnosticoRestauracaoDTO::fromEntity).collect(Collectors.toList()));
    }

    @PostMapping
    public ResponseEntity<DiagnosticoRestauracaoDTO> save(@RequestBody DiagnosticoRestauracaoDTO dto) {
        DiagnosticoRestauracao salvo = diagnosticoRestauracaoService.save(dto.toEntity());
        return ResponseEntity.status(HttpStatus.CREATED).body(DiagnosticoRestauracaoDTO.fromEntity(salvo));
    }

    @GetMapping("/{id}")
    public ResponseEntity<DiagnosticoRestauracaoDTO> findById(@PathVariable Long id) {
        DiagnosticoRestauracao entidade = diagnosticoRestauracaoService.findById(id);
        if (entidade != null) {
            return ResponseEntity.ok(DiagnosticoRestauracaoDTO.fromEntity(entidade));
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/numero-documento/{numeroDocumento}")
    public ResponseEntity<DiagnosticoRestauracaoDTO> findByNumeroDocumento(@PathVariable Integer numeroDocumento) {
        DiagnosticoRestauracao entidade = diagnosticoRestauracaoService.findByNumeroDocumento(numeroDocumento);
        return ResponseEntity.ok(DiagnosticoRestauracaoDTO.fromEntity(entidade));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<DiagnosticoRestauracaoDTO> update(@PathVariable Integer id, @RequestBody DiagnosticoRestauracaoDTO dto) {
        DiagnosticoRestauracao entity = dto.toEntity();
        entity.setId(id);
        DiagnosticoRestauracao atualizado = diagnosticoRestauracaoService.update(entity);
        return ResponseEntity.ok(DiagnosticoRestauracaoDTO.fromEntity(atualizado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        diagnosticoRestauracaoService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
