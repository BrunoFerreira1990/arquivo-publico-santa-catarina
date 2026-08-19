package com.example.apesc.controller;

import com.example.apesc.dto.BibliotecaApoioDTO;
import com.example.apesc.model.BibliotecaApoio;
import com.example.apesc.service.bibliotecaapoio.BibliotecaApoioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/biblioteca-apoio")
@RequiredArgsConstructor
public class BibliotecaApoioController {

    private final BibliotecaApoioService apoioService;

    @PostMapping
    public ResponseEntity<BibliotecaApoioDTO> save(@RequestBody BibliotecaApoioDTO dto) {
        BibliotecaApoio salvo = apoioService.save(dto.toEntity());
        return ResponseEntity.status(HttpStatus.CREATED).body(BibliotecaApoioDTO.fromEntity(salvo));
    }

    @GetMapping
    public ResponseEntity<List<BibliotecaApoioDTO>> listAll() {
        List<BibliotecaApoioDTO> apoios = apoioService.findAllWithRelations().stream()
                .map(BibliotecaApoioDTO::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(apoios);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BibliotecaApoioDTO> findById(@PathVariable Long id) {
        return apoioService.findByIdWithRelations(id)
                .map(a -> ResponseEntity.ok(BibliotecaApoioDTO.fromEntity(a)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("/{id}")
    public ResponseEntity<BibliotecaApoioDTO> update(@PathVariable Long id, @RequestBody BibliotecaApoioDTO dto) {
        BibliotecaApoio atualizado = dto.toEntity();
        atualizado.setId(id);
        BibliotecaApoio salvo = apoioService.update(atualizado);
        return ResponseEntity.ok(BibliotecaApoioDTO.fromEntity(salvo));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        apoioService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/tipo-documento/{tipoDocumentoId}")
    public ResponseEntity<List<BibliotecaApoioDTO>> findByTipoDocumento(@PathVariable Long tipoDocumentoId) {
        List<BibliotecaApoioDTO> apoios = apoioService.findByTipoDocumento(tipoDocumentoId).stream()
                .map(BibliotecaApoioDTO::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(apoios);
    }
}
