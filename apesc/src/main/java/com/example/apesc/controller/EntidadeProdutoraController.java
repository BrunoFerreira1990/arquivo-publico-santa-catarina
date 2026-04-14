package com.example.apesc.controller;

import com.example.apesc.dto.EntidadeProdutoraDTO;
import com.example.apesc.model.EntidadeProdutora;
import com.example.apesc.service.productionentity.EntidadeProdutoraService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/entidade-produtora")
@RequiredArgsConstructor
public class EntidadeProdutoraController {
    
    private final EntidadeProdutoraService entidadeProdutoraService;

    @PostMapping
    public ResponseEntity<EntidadeProdutoraDTO> save(@RequestBody EntidadeProdutoraDTO dto) {
        EntidadeProdutora salvo = entidadeProdutoraService.save(dto.toEntity());
        return ResponseEntity.status(HttpStatus.CREATED).body(EntidadeProdutoraDTO.fromEntity(salvo));
    }

    @GetMapping
    public ResponseEntity<List<EntidadeProdutoraDTO>> findAll() {
        List<EntidadeProdutoraDTO> lista = entidadeProdutoraService.findAll().stream()
                .map(EntidadeProdutoraDTO::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntidadeProdutoraDTO> findById(@PathVariable Long id) {
        EntidadeProdutora entidade = entidadeProdutoraService.findById(id);
        if (entidade != null) {
            return ResponseEntity.ok(EntidadeProdutoraDTO.fromEntity(entidade));
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<List<EntidadeProdutoraDTO>> findByName(@PathVariable String name) {
        List<EntidadeProdutoraDTO> lista = entidadeProdutoraService.findByNome(name).stream()
                .map(EntidadeProdutoraDTO::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/abreviacao/{abreviacao}")
    public ResponseEntity<List<EntidadeProdutoraDTO>> findByAbreviacao(@PathVariable String abreviacao) {
        List<EntidadeProdutoraDTO> lista = entidadeProdutoraService.findByAbreviacao(abreviacao).stream()
                .map(EntidadeProdutoraDTO::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(lista);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<EntidadeProdutoraDTO> update(@PathVariable Long id, @RequestBody EntidadeProdutoraDTO dto) {
        EntidadeProdutora entity = dto.toEntity();
        entity.setId(id);
        EntidadeProdutora atualizado = entidadeProdutoraService.update(entity);
        return ResponseEntity.ok(EntidadeProdutoraDTO.fromEntity(atualizado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        entidadeProdutoraService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
