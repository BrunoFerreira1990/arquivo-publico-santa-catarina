package com.example.apesc.controller;

import com.example.apesc.dto.PermissoesDTO;
import com.example.apesc.model.Permissoes;
import com.example.apesc.service.permissoes.PermissoesService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/permissoes")
@RequiredArgsConstructor
public class PermissoesController {

    private final PermissoesService permissoesService;

    @PostMapping
    public ResponseEntity<PermissoesDTO> save(@RequestBody PermissoesDTO dto) {
        Permissoes salvo = permissoesService.save(dto.toEntity());
        return ResponseEntity.status(HttpStatus.CREATED).body(PermissoesDTO.fromEntity(salvo));
    }

    @GetMapping
    public ResponseEntity<List<PermissoesDTO>> listAll() {
        List<PermissoesDTO> permissoes = permissoesService.findAll().stream()
                .map(PermissoesDTO::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(permissoes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PermissoesDTO> findById(@PathVariable Long id) {
        return permissoesService.findById(id)
                .map(p -> ResponseEntity.ok(PermissoesDTO.fromEntity(p)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("/{id}")
    public ResponseEntity<PermissoesDTO> update(@PathVariable Long id, @RequestBody PermissoesDTO dto) {
        Permissoes atualizado = dto.toEntity();
        atualizado.setId(id);
        Permissoes salvo = permissoesService.update(atualizado);
        return ResponseEntity.ok(PermissoesDTO.fromEntity(salvo));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        permissoesService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
