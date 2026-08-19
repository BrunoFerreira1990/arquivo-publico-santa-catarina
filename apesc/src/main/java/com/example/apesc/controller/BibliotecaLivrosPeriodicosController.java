package com.example.apesc.controller;

import com.example.apesc.dto.BibliotecaLivrosPeriodicosDTO;
import com.example.apesc.model.BibliotecaLivrosPeriodicos;
import com.example.apesc.service.bibliotecalivrosperiodicos.BibliotecaLivrosPeriodicosService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/biblioteca-livros-periodicos")
@RequiredArgsConstructor
public class BibliotecaLivrosPeriodicosController {

    private final BibliotecaLivrosPeriodicosService livroService;

    @PostMapping
    public ResponseEntity<BibliotecaLivrosPeriodicosDTO> save(@RequestBody BibliotecaLivrosPeriodicosDTO dto) {
        BibliotecaLivrosPeriodicos salvo = livroService.save(dto.toEntity());
        return ResponseEntity.status(HttpStatus.CREATED).body(BibliotecaLivrosPeriodicosDTO.fromEntity(salvo));
    }

    @GetMapping
    public ResponseEntity<List<BibliotecaLivrosPeriodicosDTO>> listAll() {
        List<BibliotecaLivrosPeriodicosDTO> livros = livroService.findAllWithRelations().stream()
                .map(BibliotecaLivrosPeriodicosDTO::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(livros);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BibliotecaLivrosPeriodicosDTO> findById(@PathVariable Long id) {
        return livroService.findByIdWithRelations(id)
                .map(l -> ResponseEntity.ok(BibliotecaLivrosPeriodicosDTO.fromEntity(l)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("/{id}")
    public ResponseEntity<BibliotecaLivrosPeriodicosDTO> update(@PathVariable Long id, @RequestBody BibliotecaLivrosPeriodicosDTO dto) {
        BibliotecaLivrosPeriodicos atualizado = dto.toEntity();
        atualizado.setId(id);
        BibliotecaLivrosPeriodicos salvo = livroService.update(atualizado);
        return ResponseEntity.ok(BibliotecaLivrosPeriodicosDTO.fromEntity(salvo));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        livroService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/tipo-documento/{tipoDocumentoId}")
    public ResponseEntity<List<BibliotecaLivrosPeriodicosDTO>> findByTipoDocumento(@PathVariable Long tipoDocumentoId) {
        List<BibliotecaLivrosPeriodicosDTO> livros = livroService.findByTipoDocumento(tipoDocumentoId).stream()
                .map(BibliotecaLivrosPeriodicosDTO::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(livros);
    }
}
