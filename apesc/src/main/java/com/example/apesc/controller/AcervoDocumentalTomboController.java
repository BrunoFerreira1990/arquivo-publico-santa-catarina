package com.example.apesc.controller;

import com.example.apesc.dto.AcervoDocumentalTomboDTO;
import com.example.apesc.model.AcervoDocumentalTombo;
import com.example.apesc.service.acervodocumentaltombo.AcervoDocumentalTomboService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/acervo-documental-tombo")
@RequiredArgsConstructor
public class AcervoDocumentalTomboController {

    private final AcervoDocumentalTomboService tomboService;

    @PostMapping
    public ResponseEntity<AcervoDocumentalTomboDTO> save(@RequestBody AcervoDocumentalTomboDTO dto) {
        AcervoDocumentalTombo salvo = tomboService.save(dto.toEntity());
        return ResponseEntity.status(HttpStatus.CREATED).body(AcervoDocumentalTomboDTO.fromEntity(salvo));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AcervoDocumentalTomboDTO> findById(@PathVariable Long id) {
        return tomboService.findByIdWithRelations(id)
                .map(t -> ResponseEntity.ok(AcervoDocumentalTomboDTO.fromEntity(t)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("/{id}")
    public ResponseEntity<AcervoDocumentalTomboDTO> update(@PathVariable Long id, @RequestBody AcervoDocumentalTomboDTO dto) {
        AcervoDocumentalTombo atualizado = dto.toEntity();
        atualizado.setId(id);
        AcervoDocumentalTombo salvo = tomboService.update(atualizado);
        return ResponseEntity.ok(AcervoDocumentalTomboDTO.fromEntity(salvo));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        tomboService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/acervo-documental/{acervoDocumentalId}")
    public ResponseEntity<List<AcervoDocumentalTomboDTO>> findByAcervoDocumento(@PathVariable Long acervoDocumentalId) {
        List<AcervoDocumentalTomboDTO> tombos = tomboService.findByAcervoDocumento(acervoDocumentalId).stream()
                .map(AcervoDocumentalTomboDTO::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(tombos);
    }
}
