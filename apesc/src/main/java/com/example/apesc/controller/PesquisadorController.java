package com.example.apesc.controller;

import com.example.apesc.dto.PesquisadorDTO;
import com.example.apesc.model.Pesquisador;
import com.example.apesc.service.researchers.PesquisadorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/pesquisador")
@RequiredArgsConstructor
public class PesquisadorController {
    
    private final PesquisadorService pesquisadorService;

    @PostMapping
    public ResponseEntity<PesquisadorDTO> save(@RequestBody PesquisadorDTO dto) {
        Pesquisador salvo = pesquisadorService.save(dto.toEntity());
        return ResponseEntity.status(HttpStatus.CREATED).body(PesquisadorDTO.fromEntity(salvo));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PesquisadorDTO> findById(@PathVariable Long id) {
        Pesquisador pesquisador = pesquisadorService.findById(id);
        if (pesquisador != null) {
            return ResponseEntity.ok(PesquisadorDTO.fromEntity(pesquisador));
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/nome/{nome}")
    public ResponseEntity<PesquisadorDTO> findByName(@PathVariable String nome) {
        List<Pesquisador> pesquisadores = pesquisadorService.findByNome(nome);
        if (pesquisadores != null && !pesquisadores.isEmpty()) {
            return ResponseEntity.ok(PesquisadorDTO.fromEntity(pesquisadores.get(0)));
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/search")
    public ResponseEntity<List<PesquisadorDTO>> search(
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) String cpf) {
        List<PesquisadorDTO> pesquisadores = pesquisadorService.search(nome, cpf).stream()
                .map(PesquisadorDTO::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(pesquisadores);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<PesquisadorDTO> update(@PathVariable Long id, @RequestBody PesquisadorDTO dto) {
        Pesquisador entity = dto.toEntity();
        entity.setId(id);
        Pesquisador atualizado = pesquisadorService.update(entity);
        return ResponseEntity.ok(PesquisadorDTO.fromEntity(atualizado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        pesquisadorService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
