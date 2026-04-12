package com.example.apesc.controller;

import com.example.apesc.dto.PesquisadorDTO;
import com.example.apesc.model.Pesquisador;
import com.example.apesc.service.researchers.PesquisadorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/nome/{nome}")
    public ResponseEntity<PesquisadorDTO> findByName(@PathVariable String nome) {
        // Assume retrieving the first one or adapting as the service returns a list.
        // Assuming your service might return a single object or list,
        // Since the interface says findByNome, and it's a list typically, let's just return the first one or modify to return list if it's a list
        // Based on PesquisadorService impl it returns List<Pesquisador>. Let's return the first for simplicity or an array.
        // I will just use the first one if it exists.
        java.util.List<Pesquisador> pesquisadores = pesquisadorService.findByNome(nome);
        if (pesquisadores != null && !pesquisadores.isEmpty()) {
            return ResponseEntity.ok(PesquisadorDTO.fromEntity(pesquisadores.get(0)));
        }
        return ResponseEntity.notFound().build();
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
